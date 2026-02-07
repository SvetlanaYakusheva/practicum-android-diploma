package ru.practicum.android.diploma.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.ui.filter.FilterUiState

class LocationViewModel (
    val filterInteractor: FilterInteractor
) : ViewModel() {
    private var currentFilter = filterInteractor.currentFilter()
    private val stateLiveData = MutableLiveData<FilterUiState>()
    fun observeState(): LiveData<FilterUiState> = stateLiveData

    init {
        postCurrentFilter()
    }

    private fun postCurrentFilter() {
        if (currentFilter == Filter()) {
            stateLiveData.postValue(FilterUiState.Empty)
        } else {
            stateLiveData.postValue(FilterUiState.Filled(currentFilter))
        }
    }
}
