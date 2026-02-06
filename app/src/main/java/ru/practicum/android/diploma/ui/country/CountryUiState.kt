package ru.practicum.android.diploma.ui.country

import ru.practicum.android.diploma.domain.models.Area

sealed interface CountryUiState {

    data class Content(
        val countriesList: List<Area>
    ) : CountryUiState

    data object Error : CountryUiState

    data object Empty : CountryUiState
}
