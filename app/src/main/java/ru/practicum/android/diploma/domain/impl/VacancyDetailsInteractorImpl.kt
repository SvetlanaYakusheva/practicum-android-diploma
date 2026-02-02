package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsInteractorImpl(private val repository: VacancyDetailsRepository) : VacancyDetailsInteractor {
    override fun getVacancyById(vacancyId: String): Flow<Resource<Vacancy>> {
        return repository.getVacancyById(vacancyId)
    }
}
