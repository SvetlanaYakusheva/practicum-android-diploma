package ru.practicum.android.diploma.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.ui.location.LocationUiState

class LocationViewModel(
    val filterInteractor: FilterInteractor
) : ViewModel() {
    private var currentFilter = filterInteractor.currentFilter()
    private val stateLiveData = MutableLiveData<LocationUiState>()
    fun observeState(): LiveData<LocationUiState> = stateLiveData

    init {
        postCurrentFilter()
    }

    private fun postCurrentFilter() {
        if (currentFilter == Filter()) {
            stateLiveData.postValue(LocationUiState.Empty)
        } else {
            stateLiveData.postValue(LocationUiState.Filled(currentFilter))
        }
    }

    fun checkFilter() {
        currentFilter = filterInteractor.currentFilter()
        postCurrentFilter()
    }

    fun currentFilterChanged() = currentFilter != filterInteractor.appliedFilter()

    fun setLocationToFilter(country: Area?, region: Area?) {
        filterInteractor.setCountry(country)
        filterInteractor.setRegion(region)
    }

    fun clearCountry() {
        filterInteractor.setCountry(null)
        filterInteractor.setRegion(null)
        checkFilter()
    }

    fun clearRegion() {
        filterInteractor.setRegion(null)
        checkFilter()
    }
}
