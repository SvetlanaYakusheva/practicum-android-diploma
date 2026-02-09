package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.SearchUiState
import ru.practicum.android.diploma.util.Constant.NEXT_PAGE_LOADING_START
import ru.practicum.android.diploma.util.Constant.PER_PAGE_SIZE
import ru.practicum.android.diploma.util.Constant.SEARCH_DEBOUNCE_DELAY_MS
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.UtilFunctions

class SearchViewModel(
    private val searchVacanciesInteractor: SearchVacanciesInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var appliedFilter: Filter = filterInteractor.appliedFilter()
    private val stateLiveData = MutableLiveData<SearchUiState>()
    fun observeState(): LiveData<SearchUiState> = stateLiveData
    private var vacanciesList = mutableListOf<Vacancy>()
    private val showToast = SingleLiveEvent<ErrorType>()
    fun observeShowToast(): LiveData<ErrorType> = showToast
    private var isNextPageLoading: Boolean = false
    private var currentPage: Int = 1
    private var maxPage: Int? = null
    private var latestSearchText: String? = null
    private val vacancySearchDebounce = UtilFunctions.debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY_MS,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { query ->
        clearSearch()
        searchVacancies(query)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            appliedFilter = filterInteractor.appliedFilter()
            vacancySearchDebounce(changedText)
        }
    }

    private fun searchVacancies(searchText: String) {
        if (searchText.isNotBlank()) {
            if (currentPage == maxPage) {
                return
            } else {
                if (currentPage == 0) {
                    renderState(SearchUiState.LoadingNewQuery)
                } else {
                    isNextPageLoading = true
                    renderState(SearchUiState.NextPageLoading)
                }
                searchRequest(searchText, currentPage)
                if (currentPage == 0) {
                    currentPage = NEXT_PAGE_LOADING_START
                } else {
                    currentPage += 1
                }
            }
        } else {
            renderState(SearchUiState.Default)
        }
    }

    fun clearSearch() {
        renderState(SearchUiState.Default)
        currentPage = 0
        maxPage = null
        vacanciesList.clear()
    }

    fun onLastItemReached() {
        if (isNextPageLoading) {
            return
        } else {
            latestSearchText?.let { searchVacancies(it) }
        }
    }

    private fun searchRequest(searchText: String, currentPage: Int) {
        if (searchText.isNotEmpty()) {
            viewModelScope.launch {
                searchVacanciesInteractor.searchVacancies(
                    searchText,
                    appliedFilter,
                    currentPage,
                    PER_PAGE_SIZE
                )
                    .collect { resource ->
                        processResult(
                            resource.data?.vacancies,
                            resource.data?.found,
                            resource.errorType
                        )
                        maxPage = resource.data?.count
                    }
            }
        }
    }

    private fun processResult(
        foundVacancies: List<Vacancy>?,
        countOfVacancies: Int?,
        errorType: ErrorType?
    ) {
        if (foundVacancies != null) {
            vacanciesList.addAll(foundVacancies)
        }
        when {
            errorType != null -> {
                if (errorType == ErrorType.NoConnection) {
                    if (isNextPageLoading) {
                        renderState(SearchUiState.Content(vacanciesList, null))
                    } else {
                        renderState(SearchUiState.InternetNotAvailable)
                    }
                    showToast(ErrorType.NoConnection)
                } else {
                    if (isNextPageLoading) {
                        renderState(SearchUiState.Content(vacanciesList, null))
                    } else {
                        renderState(SearchUiState.ServerError)
                    }
                    showToast(ErrorType.ServerError)
                }
                isNextPageLoading = false
            }

            vacanciesList.isEmpty() -> {
                renderState(
                    SearchUiState.EmptyQuery
                )
            }

            else -> {
                renderState(SearchUiState.Content(vacanciesList.distinct(), countOfVacancies))
                isNextPageLoading = false
            }
        }
    }

    private fun showToast(errorType: ErrorType) {
        showToast.postValue(errorType)
    }

    private fun renderState(state: SearchUiState) {
        stateLiveData.postValue(state)
    }

    fun checkFilters() {
        val newFilter = filterInteractor.appliedFilter()
        if (newFilter != appliedFilter) {
            appliedFilter = newFilter
            currentPage = 0
            maxPage = null
            vacanciesList.clear()
            latestSearchText?.let { searchText ->
                searchVacancies(searchText)
            }
        }
    }

    fun hasFilter() = filterInteractor.currentFilter() != Filter()

}
