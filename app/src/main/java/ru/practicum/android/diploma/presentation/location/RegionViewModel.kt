package ru.practicum.android.diploma.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.region.RegionUiState
import ru.practicum.android.diploma.util.UtilFunctions.debounce

class RegionViewModel (
    val dictionariesInteractor: DictionariesInteractor,
    val filterInteractor: FilterInteractor
) : ViewModel() {
    private var currentFilter = filterInteractor.currentFilter()
    private var latestSearchText: String? = null
    private var areasList: List<Area>? = null
    private var filteredRegions: List<Area>? = null

    private val stateLiveData = MutableLiveData<RegionUiState>()
    fun observeState(): LiveData<RegionUiState> = stateLiveData

    init {
        loadRegions()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            industrySearchDebounce(changedText)
        }
    }

    private val industrySearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchIndustries(changedText)
        }

    private fun searchIndustries(searchText: String) {
        if (areasList == null) {
            loadRegions()
        } else {
            postFilteredIndustries(filteredRegions!!, searchText)
        }
    }

    private fun loadRegions() {
        stateLiveData.postValue(RegionUiState.Loading)
        if (currentFilter.country == null) {
        viewModelScope.launch {
            dictionariesInteractor.getRegionsFlatMap().collect {
                if (it.errorType != null) {
                    areasList = null
                    stateLiveData.postValue(RegionUiState.Error(it.errorType))
                } else if (it.data != null) {
                    areasList = it.data
                    filteredRegions = areasList?.filter { !it.parentId.isNullOrBlank() }
                    postFilteredIndustries(filteredRegions!!, latestSearchText)
                }
            }
        }
        } else {
            postFilteredIndustries(currentFilter.country!!.areas!!, latestSearchText)
        }
    }

    private fun postFilteredIndustries(industries: List<Area>, filterString: String?) {
        var filtered = industries
        if (filterString != null) {
            filtered = filtered.filter {
                it.name.contains(filterString, ignoreCase = true)
            }
        }
        if (filtered.isEmpty()) {
            stateLiveData.postValue(RegionUiState.Empty)
        } else {
            stateLiveData.postValue(RegionUiState.Content(filtered))
        }
    }

    fun setRegionToFilter(region: Area?) {
        filterInteractor.setRegion(region)
        if (currentFilter.country?.id != region?.parentId) {
            val country = areasList?.filter { it.id == region?.parentId }?.get(0)
                    filterInteractor.setCountry(country)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }
}
