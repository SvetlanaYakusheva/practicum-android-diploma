package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacanciesRepository

class SearchVacanciesInteractorImpl(
    private val repository: VacanciesRepository
) : SearchVacanciesInteractor {

    override suspend fun searchVacancies(query: String) =
        repository.searchVacancies(query)
}
