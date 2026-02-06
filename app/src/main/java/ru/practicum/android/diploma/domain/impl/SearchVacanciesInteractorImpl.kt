package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.api.SearchVacanciesRepository
import ru.practicum.android.diploma.domain.models.Filter

class SearchVacanciesInteractorImpl(private val repository: SearchVacanciesRepository) : SearchVacanciesInteractor {

    override fun searchVacancies(
        expression: String,
        filter: Filter,
        page: Int,
        perPage: Int
    ) = repository.searchVacancies(expression, filter, page, perPage)
}
