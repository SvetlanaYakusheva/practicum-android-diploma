package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface VacanciesRepository {
    suspend fun searchVacancies(
        query: String,
        page: Int = 0,
        // todo: сразу задел под пагинацию
        perPage: Int = DEFAULT_PAGE_SIZE
    ): Resource<List<Vacancy>>

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
