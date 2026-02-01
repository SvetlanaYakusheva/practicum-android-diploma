package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.util.Resource

class SearchViewModel(
    private val searchVacanciesInteractor: SearchVacanciesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private var currentQuery: String = ""

    fun onQueryChanged(query: String) {
        currentQuery = query

        searchJob?.cancel()

        if (query.isBlank()) {
            _state.value = SearchUiState.Initial
            return
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MS)

            _state.value = SearchUiState.Loading

            when (val result = searchVacanciesInteractor.searchVacancies(query)) {
                is Resource.Success -> {
                    _state.value = SearchUiState.Content(
                        vacancies = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    // todo: добавить обработку ошибок
                    _state.value = SearchUiState.Initial
                }
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        currentQuery = ""
        _state.value = SearchUiState.Initial
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MS = 2_000L
    }
}
