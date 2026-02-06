package ru.practicum.android.diploma.data.impl

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

class FilterRepositoryImpl(context: Context, private val gson: Gson) : FilterRepository {
    private val sharedPreferences = context.applicationContext
        .getSharedPreferences(STORAGE_FILTER, Context.MODE_PRIVATE)

    private var currentFilter: Filter = Filter()
    private var appliedFilter: Filter = Filter()

    init {
        currentFilter = loadFilter(CURRENT_FILTER)
        appliedFilter = loadFilter(APPLIED_FILTER)
    }

    private fun loadFilter(key: String): Filter {
        val json = sharedPreferences.getString(key, null)
        return if (json.isNullOrEmpty()) Filter() else gson.fromJson(json, Filter::class.java)
    }

    private fun saveFilter(key: String, filter: Filter) {
        sharedPreferences.edit {
            putString(key, gson.toJson(filter))
        }
    }

    override fun currentFilter(): Filter = currentFilter
    override fun appliedFilter(): Filter = appliedFilter

    override fun setCountry(country: Area?) {
        // Business Logic: If country changes, we might want to clear the specific area
        currentFilter = currentFilter.copy(country = country, area = null)
        saveFilter(CURRENT_FILTER, currentFilter)
    }

    override fun setArea(area: Area?) {
        currentFilter = currentFilter.copy(area = area)
        saveFilter(CURRENT_FILTER, currentFilter)
    }

    override fun setIndustry(industry: Industry?) {
        currentFilter = currentFilter.copy(industry = industry)
        saveFilter(CURRENT_FILTER, currentFilter)
    }

    override fun setSalary(salary: String?) {
        currentFilter = currentFilter.copy(salary = salary)
        saveFilter(CURRENT_FILTER, currentFilter)
    }

    override fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        currentFilter = currentFilter.copy(onlyWithSalary = onlyWithSalary)
        saveFilter(CURRENT_FILTER, currentFilter)
    }

    override fun apply() {
        appliedFilter = currentFilter
        saveFilter(APPLIED_FILTER, appliedFilter)
    }

    override fun flushCurrentFilter() {
        currentFilter = Filter()
        appliedFilter = Filter()
        saveFilter(CURRENT_FILTER, currentFilter)
        saveFilter(APPLIED_FILTER, appliedFilter)
    }

    companion object {
        private const val APPLIED_FILTER = "APPLIED_FILTER"
        private const val CURRENT_FILTER = "CURRENT_FILTER"
        private const val STORAGE_FILTER = "STORAGE_FILTER"
    }
}
