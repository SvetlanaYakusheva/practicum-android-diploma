package ru.practicum.android.diploma.ui.filter

import ru.practicum.android.diploma.domain.models.Filter

sealed interface FilterUiState {
    data object Empty : FilterUiState
    data class Filled(
        val filter: Filter
    ) : FilterUiState
}
