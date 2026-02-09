package ru.practicum.android.diploma.data.impl

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.util.Constant.Companion.APPLIED_FILTER
import ru.practicum.android.diploma.util.Constant.Companion.CURRENT_FILTER
import ru.practicum.android.diploma.util.Constant.Companion.STORAGE_FILTER

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
            gson.fromJson(json, Filter::class.java) ?: Filter()
        }
    }

    private fun saveToPrefs(key: String, filter: Filter) {
        sharedPreferences.edit {
            putString(key, gson.toJson(filter))
        }
    }
}
