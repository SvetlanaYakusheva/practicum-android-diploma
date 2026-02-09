package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.ui.filter.FilterUiState

class FilterViewModel(private val filterInteractor: FilterInteractor) :
    ViewModel() {
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

    fun clearFilter() {
        filterInteractor.flushCurrentFilter()
        currentFilter = filterInteractor.currentFilter()
        postCurrentFilter()
    }

    fun setSalaryIsRequired(required: Boolean) {
        filterInteractor.setOnlyWithSalary(required)
        currentFilter = filterInteractor.currentFilter()
    }

    fun setSalary(salary: String) {
        if (salary.isEmpty()) {
            clearSalary()
        } else {
            filterInteractor.setSalary(salary)
            currentFilter = filterInteractor.currentFilter()
        }
    }

    fun clearSalary() {
        filterInteractor.setSalary(null)
        currentFilter = filterInteractor.currentFilter()
    }

    fun clearIndustry() {
        filterInteractor.setIndustry(null)
        currentFilter = filterInteractor.currentFilter()
    }

    fun clearWorkplace() {
        filterInteractor.setCountry(null)
        filterInteractor.setRegion(null)
        currentFilter = filterInteractor.currentFilter()
    }

    fun applyFilter() {
        filterInteractor.apply()
    }

    fun checkFilter() {
        currentFilter = filterInteractor.currentFilter()
        postCurrentFilter()
    }

    fun currentFilterIsEmpty() = currentFilter == Filter()

    fun currentFilterChanged() = currentFilter != filterInteractor.appliedFilter()
}
