package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private var currentQuery: String = ""

    fun onQueryChanged(query: String) {
        currentQuery = query
    }

    fun search() {
        if (currentQuery.isBlank()) return

        viewModelScope.launch {
            _state.value = SearchUiState.Loading

            when (val result = searchVacanciesInteractor.searchVacancies(currentQuery)) {
                is Resource.Success -> {
                    _state.value = SearchUiState.Content(
                        vacancies = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    // todo: добавить Error state
                    _state.value = SearchUiState.Initial
                }
            }
        }
    }

    fun clearSearch() {
        currentQuery = ""
        _state.value = SearchUiState.Initial
    }
}
