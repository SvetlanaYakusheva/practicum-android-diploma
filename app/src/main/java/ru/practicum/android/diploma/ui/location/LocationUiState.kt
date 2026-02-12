package ru.practicum.android.diploma.ui.location

import ru.practicum.android.diploma.domain.models.Filter

sealed interface LocationUiState {
    data object Empty : LocationUiState
    data class Filled(
        val filter: Filter
    ) : LocationUiState
}
