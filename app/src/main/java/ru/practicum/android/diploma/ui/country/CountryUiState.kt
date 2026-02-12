package ru.practicum.android.diploma.ui.country

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.util.ErrorType

sealed interface CountryUiState {

    data class Content(
        val countriesList: List<Area>
    ) : CountryUiState

    data object Loading : CountryUiState

    data class Error(val error: ErrorType) : CountryUiState
}
