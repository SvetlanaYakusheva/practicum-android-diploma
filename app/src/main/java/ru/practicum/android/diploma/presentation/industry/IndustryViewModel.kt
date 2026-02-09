package ru.practicum.android.diploma.presentation.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.industry.IndustryUiState
import ru.practicum.android.diploma.util.Constant.Companion.SEARCH_DEBOUNCE_DELAY_DICTIONARY
import ru.practicum.android.diploma.util.UtilFunctions.debounce

class IndustryViewModel(
    val dictionariesInteractor: DictionariesInteractor,
    val filterInteractor: FilterInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<IndustryUiState>()
    private var latestSearchText: String? = null
    private var industriesList: List<Industry>? = null
    fun observeState(): LiveData<IndustryUiState> = stateLiveData

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        stateLiveData.postValue(IndustryUiState.Loading)
        viewModelScope.launch {
            dictionariesInteractor.getIndustries().collect {
                if (it.errorType != null) {
                    industriesList = null
                    stateLiveData.postValue(IndustryUiState.Error(it.errorType))
                } else if (it.data != null) {
                    industriesList = it.data
                    postFilteredIndustries(industriesList!!, latestSearchText)
                }
            }
        }
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            industrySearchDebounce(changedText)
        }
    }

    private val industrySearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_DICTIONARY, viewModelScope, true) { changedText ->
            searchIndustries(changedText)
        }

    private fun searchIndustries(searchText: String) {
        if (industriesList == null) {
            loadIndustries()
        } else {
            postFilteredIndustries(industriesList!!, searchText)
        }
    }

    private fun postFilteredIndustries(industries: List<Industry>, filterString: String?) {
        var filtered = industries
        if (filterString != null) {
            filtered = filtered.filter {
                it.name.contains(filterString, ignoreCase = true)
            }
        }
        if (filtered.isEmpty()) {
            stateLiveData.postValue(IndustryUiState.Empty)
        } else {
            stateLiveData.postValue(IndustryUiState.Content(filtered))
        }
    }

    fun setIndustryToFilter(industry: Industry?) {
        filterInteractor.setIndustry(industry)
    }
}
