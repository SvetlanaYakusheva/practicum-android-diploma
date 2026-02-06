package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

class FilterInteractorImpl(private val repository: FilterRepository) : FilterInteractor {
    private var currentFilter: Filter = repository.loadFilter()
    private var appliedFilter: Filter = currentFilter

    override fun currentFilter(): Filter = currentFilter
    override fun appliedFilter(): Filter = appliedFilter

    override fun setCountry(country: Area?) {
        currentFilter = currentFilter.copy(country = country)
        saveCurrentFilter()
    }

    override fun setArea(area: Area?) {
        currentFilter = currentFilter.copy(area = area)
        saveCurrentFilter()
    }

    override fun setIndustry(industry: Industry?) {
        currentFilter = currentFilter.copy(industry = industry)
        saveCurrentFilter()
    }

    override fun setSalary(salary: String?) {
        currentFilter = currentFilter.copy(salary = salary)
        saveCurrentFilter()
    }

    override fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        currentFilter = currentFilter.copy(onlyWithSalary = onlyWithSalary)
        saveCurrentFilter()
    }

    private fun saveCurrentFilter() {
        repository.saveFilter(currentFilter)
    }

    override fun apply() {
        if (appliedFilter != currentFilter) {
            appliedFilter = currentFilter
            repository.saveAppliedFilter(appliedFilter)
        }
    }

    override fun flushCurrentFilter() {
        currentFilter = Filter()
        saveCurrentFilter()
    }

}
