package ru.practicum.android.diploma.ui.industry

import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.ErrorType

sealed interface IndustryUiState {
    data class Content(val industries: List<Industry>) : IndustryUiState

    data object Loading : IndustryUiState

    data class Error(val error: ErrorType) : IndustryUiState

    data object Empty: IndustryUiState
}
