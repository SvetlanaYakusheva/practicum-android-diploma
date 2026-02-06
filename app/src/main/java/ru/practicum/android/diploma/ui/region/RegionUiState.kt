package ru.practicum.android.diploma.ui.region

import ru.practicum.android.diploma.domain.models.Area

sealed interface RegionUiState {

    data class Content(
        val regionsList: List<Area>
    ) : RegionUiState

    data object Error : RegionUiState

    data object Empty : RegionUiState

    data object NoRegionUi : RegionUiState
}
