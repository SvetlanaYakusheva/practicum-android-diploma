package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SearchUiState {
    data object NextPageLoading : SearchUiState
    data object LoadingNewQuery : SearchUiState
    data object Default : SearchUiState

    data class Content(
        val vacanciesList: List<Vacancy>,
        val countOfVacancies: Int?,
    ) : SearchUiState

    object ServerError : SearchUiState

    object InternetNotAvailable : SearchUiState

    object EmptyQuery : SearchUiState
}
