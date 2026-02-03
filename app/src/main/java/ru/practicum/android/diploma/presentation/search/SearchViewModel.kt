package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.ui.search.SearchUiState
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.UtilFunctions

class SearchViewModel(
    private val searchVacanciesInteractor: SearchVacanciesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private val searchDebounce = UtilFunctions.debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY_MS,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { query ->
        performSearch(query)
    }

    fun onQueryChanged(query: String) {
        if (query.isBlank()) {
            _state.value = SearchUiState.Initial
            return
        }
        searchDebounce(query)
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _state.value = SearchUiState.Loading

            when (val result = searchVacanciesInteractor.searchVacancies(query)) {
                is Resource.Success -> {
                    _state.value = SearchUiState.Content(
                        vacancies = result.data
                    )
                }

                is Resource.Error -> {
                    val message = when (result.errorType) {
                        ErrorType.NoConnection -> "Нет интернета"
                        ErrorType.NothingFound -> "Ничего не найдено"
                        ErrorType.ServerError -> "Произошла ошибка"
                        ErrorType.SQLError -> "Ошибка базы данных"
                        else -> "Произошла ошибка"
                    }
                    _state.value = SearchUiState.Error(message)
                }
            }
        }
    }

    fun clearSearch() {
        _state.value = SearchUiState.Initial
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MS = 2_000L
    }
}
