package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.api.SearchVacanciesRepository

class SearchVacanciesInteractorImpl(
    private val repository: SearchVacanciesRepository
) : SearchVacanciesInteractor {

    override suspend fun searchVacancies(query: String) =
        repository.searchVacancies(query)
}
