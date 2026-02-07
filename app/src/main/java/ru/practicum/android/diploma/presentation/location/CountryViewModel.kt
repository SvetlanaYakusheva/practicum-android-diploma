package ru.practicum.android.diploma.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.country.CountryUiState
import ru.practicum.android.diploma.ui.industry.IndustryUiState

class CountryViewModel (
    val dictionariesInteractor: DictionariesInteractor,
    val filterInteractor: FilterInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<CountryUiState>()
    private var latestSearchText: String? = null
    private var countriesList: List<Area>? = null
    fun observeState(): LiveData<CountryUiState> = stateLiveData

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
        filterInteractor.setArea(country)
    }

}
