package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface VacancyDetailsInteractor {
    suspend fun getVacancyById(vacancyId: String): Resource<Vacancy>
}
