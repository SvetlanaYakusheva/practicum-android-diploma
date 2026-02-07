package ru.practicum.android.diploma.ui.location

import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.industry.IndustryUiState
import ru.practicum.android.diploma.util.ErrorType

sealed interface LocationUiState {
    data class Content(val countries: List<Country>) : LocationUiState

    data object Loading : LocationUiState

    data class Error(val error: ErrorType) : LocationUiState
}
