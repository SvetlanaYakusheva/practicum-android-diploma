package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsInteractorImpl(private val repository: VacancyDetailsRepository) : VacancyDetailsInteractor {
    override suspend fun getVacancyById(vacancyId: String): Resource<Vacancy> {
        return repository.getVacancyById(vacancyId)
    }
}
