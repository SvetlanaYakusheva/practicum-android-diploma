package ru.practicum.android.diploma.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.ui.country.CountryUiState
import ru.practicum.android.diploma.ui.filter.FilterUiState
import ru.practicum.android.diploma.ui.region.RegionUiState

class RegionViewModel (
    val dictionariesInteractor: DictionariesInteractor,
    val filterInteractor: FilterInteractor
) : ViewModel() {
    private var currentFilter = filterInteractor.currentFilter()
    private val stateLiveData = MutableLiveData<RegionUiState>()
    fun observeState(): LiveData<RegionUiState> = stateLiveData

    init {
        //loadCountries()
        postCurrentFilter()
    }

//    private fun loadCountries() {
//        stateLiveData.postValue(CountryUiState.Loading)
//        viewModelScope.launch {
//            dictionariesInteractor.getAreas().collect {
//                if (it.errorType != null) {
//                    countriesList = null
//                    stateLiveData.postValue(CountryUiState.Error(it.errorType))
//                } else if (it.data != null) {
//                    countriesList = it.data
//                    stateLiveData.postValue(CountryUiState.Content(countriesList!!))
//                }
//            }
//        }
//    }

    private fun postCurrentFilter() {
        if (currentFilter.area == null) {
            //stateLiveData.postValue(RegionUiState.Loading)
            viewModelScope.launch {
                dictionariesInteractor.getAreas().collect {
                    if (it.errorType != null) {
                        //countriesList = null
                        stateLiveData.postValue(RegionUiState.Error(it.errorType))
                    } else if (it.data != null) {
                        //countriesList = it.data
                        stateLiveData.postValue(RegionUiState.NotFiltered(it.data!!))
                    }
                }
            }
        } else {
            stateLiveData.postValue(RegionUiState.Filtered(currentFilter.area!!.areas!!))
        }
    }

    fun setCountryToFilter(country: Area?) {
        filterInteractor.setArea(country)
    }

}
