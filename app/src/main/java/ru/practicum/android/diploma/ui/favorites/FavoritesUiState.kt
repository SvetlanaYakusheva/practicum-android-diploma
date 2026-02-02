package ru.practicum.android.diploma.ui.favorites

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface FavoritesUiState {

    object Empty : FavoritesUiState

    object Error : FavoritesUiState

    data class Content(
        val vacancies: List<Vacancy>
    ) : FavoritesUiState
}
