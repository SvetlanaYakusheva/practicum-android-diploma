package ru.practicum.android.diploma.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.country.CountryUiState

class CountryViewModel (
    val dictionariesInteractor: DictionariesInteractor,
    val filterInteractor: FilterInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<CountryUiState>()
    fun observeState(): LiveData<CountryUiState> = stateLiveData
    private var countriesList: List<Area>? = null

    init {
        loadCountries()
    }

    private fun loadCountries() {
        stateLiveData.postValue(CountryUiState.Loading)
        viewModelScope.launch {
            dictionariesInteractor.getAreas().collect {
                if (it.errorType != null) {
                    countriesList = null
                    stateLiveData.postValue(CountryUiState.Error(it.errorType))
                } else if (it.data != null) {
                    countriesList = it.data
                    stateLiveData.postValue(CountryUiState.Content(countriesList!!))
                }
            }
        }
    }

    fun setCountryToFilter(country: Area?) {
        filterInteractor.setCountry(country)
        if (filterInteractor.currentFilter().region?.parentId != country?.id) {
            filterInteractor.setRegion(null)
        }
    }
}
