package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.util.Resource

interface SearchVacanciesRepository {
    fun searchVacancies(
        expression: String,
        page: Int = 0,
        perPage: Int = DEFAULT_PAGE_SIZE
    ): Flow<Resource<VacanciesSearchResult>>

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
