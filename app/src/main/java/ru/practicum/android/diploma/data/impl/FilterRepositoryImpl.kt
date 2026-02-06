package ru.practicum.android.diploma.data.impl

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Filter

class FilterRepositoryImpl(context: Context, private val gson: Gson) : FilterRepository {
    private val sharedPreferences = context.applicationContext
        .getSharedPreferences(STORAGE_FILTER, Context.MODE_PRIVATE)

    override fun loadFilter(): Filter = loadFromPrefs(CURRENT_FILTER)

    override fun saveFilter(filter: Filter) = saveToPrefs(CURRENT_FILTER, filter)

    override fun loadAppliedFilter(): Filter = loadFromPrefs(APPLIED_FILTER)

    override fun saveAppliedFilter(filter: Filter) = saveToPrefs(APPLIED_FILTER, filter)

    private fun loadFromPrefs(key: String): Filter {
        val json = sharedPreferences.getString(key, null)
        return if (json.isNullOrEmpty()) {
            Filter()
        } else {
            // Use the injected gson instance
            gson.fromJson(json, Filter::class.java) ?: Filter()
        }
    }

    private fun saveToPrefs(key: String, filter: Filter) {
        sharedPreferences.edit {
            putString(key, gson.toJson(filter))
        }
    }

    companion object {
        private const val APPLIED_FILTER = "APPLIED_FILTER"
        private const val CURRENT_FILTER = "CURRENT_FILTER"
        private const val STORAGE_FILTER = "STORAGE_FILTER"
    }
}
