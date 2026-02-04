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

    data class ServerError(
        val errorMessage: String
    ) : SearchUiState

    data class InternetNotAvailable(
        val errorMessage: String
    ) : SearchUiState

    data class EmptyQuery(
        val message: String
    ) : SearchUiState
}
