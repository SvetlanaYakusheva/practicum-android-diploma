package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface SearchVacanciesInteractor {
    suspend fun searchVacancies(query: String): Resource<List<Vacancy>>
}
