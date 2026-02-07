package ru.practicum.android.diploma.ui.region

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.util.ErrorType

sealed interface RegionUiState {

//    data class Content(
//        val regionsList: List<Area>
//    ) : RegionUiState
//
//    data object Error : RegionUiState
//
//    data object Empty : RegionUiState
//
//    data object NoRegionUi : RegionUiState

    data class Filtered(
        val regionsList: List<Area>
    ) : RegionUiState

    data class NotFiltered(
        val regionsList: List<Area>
    ) : RegionUiState

    data class Error(
        val error: ErrorType
    ) : RegionUiState

}
