package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SearchUiState {

    object Initial : SearchUiState

    object Loading : SearchUiState

    data class Content(
        val vacancies: List<Vacancy>
    ) : SearchUiState
}
