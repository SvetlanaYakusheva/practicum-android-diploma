package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.SearchState
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.UtilFunctions

class SearchViewModel(
    private val searchVacanciesInteractor: SearchVacanciesInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    private var vacanciesList = mutableListOf<Vacancy>()
    private val showToast = SingleLiveEvent<String>()
    fun observeShowToast(): LiveData<String> = showToast
    private var isNextPageLoading: Boolean = false
    private var currentPage: Int = 0
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
            vacancySearchDebounce(changedText)
        }
    }

    private fun searchVacancies(searchText: String) {
        if (searchText.isNotBlank()) {
            if (this.currentPage == maxPage) {
                return
            } else {
                if (currentPage == 0) {
                    renderState(SearchState.LoadingNewExpression)
                } else {
                    isNextPageLoading = true
                    renderState(SearchState.NextPageLoading)
                }
                searchRequest(searchText, currentPage)
                currentPage += 1
            }
        } else {
            renderState(SearchState.Default)
        }
    }

    fun clearSearch() {
        renderState(SearchState.Default)
        currentPage = 0
        maxPage = null
        vacanciesList.clear()
    }

    fun onLastItemReached() {
        if (isNextPageLoading) {
            return
        } else {
            searchVacancies(latestSearchText!!)
        }
    }

    private fun searchRequest(searchText: String, currentPage: Int) {
        if (searchText.isNotEmpty()) {
            viewModelScope.launch {
                searchVacanciesInteractor.searchVacancies(
                    searchText,
                    currentPage,
                    PER_PAGE_SIZE
                )
                    .collect { resource ->
                        processResult(
                            resource.data?.vacancies,
                            resource.data?.found,
                            resource.errorType,
                            resource.message
                        )
                        maxPage = resource.data?.count
                    }
            }
        }
    }

    private fun processResult(
        foundVacancies: List<Vacancy>?,
        countOfVacancies: Int?,
        errorType: ErrorType?,
        errorMessage: String?
    ) {
        val messageServerError = "server_error"
        val messageNoInternet = "internet_is_not_available"
        val messageCheckConnection = "check_connection_message"

        if (foundVacancies != null) {
            vacanciesList.addAll(foundVacancies)
        }
        when {
            errorType != null -> {
                if (errorType == ErrorType.NoConnection) {
                    if (isNextPageLoading) {
                        renderState(SearchState.Content(vacanciesList, null))
                    } else {
                        renderState(SearchState.InternetNotAvailable(messageNoInternet))
                    }

                    showToast(messageCheckConnection)
                } else {
                    if (isNextPageLoading) {
                        renderState(SearchState.Content(vacanciesList, null))
                    } else {
                        renderState(SearchState.ServerError(messageServerError))
                    }
                    showToast(errorMessage ?: messageServerError)
                }
                isNextPageLoading = false
            }
            vacanciesList.isEmpty() -> {
                renderState(
                    SearchState.Empty(
                        message = "not_get_a_list_of_vacancies",
                    )
                )
            }
            else -> {
                renderState(SearchState.Content(vacanciesList.distinct(), countOfVacancies))
                isNextPageLoading = false
            }
        }
    }

    private fun showToast(message: String) {
        showToast.postValue(message)
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MS = 2_000L
        private const val PER_PAGE_SIZE = 20
    }
}
