package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class FavoriteVacanciesInteractorImpl(
    private val repository: FavoriteVacanciesRepository
) : FavoriteVacanciesInteractor {

    override fun getFavoriteVacancies(): Flow<Resource<List<Vacancy>>> {
        return repository.getFavoriteVacancies()
    }

    override suspend fun addToFavoriteVacancies(vacancy: Vacancy) {
        repository.addToFavoriteVacancies(vacancy)
    }

    override suspend fun deleteFromFavoriteVacancies(vacancy: Vacancy) {
        repository.deleteFromFavoriteVacancies(vacancy)
    }

    override fun getFavoriteVacanciesIds(): Flow<List<String>> {
        return repository.getFavoriteVacanciesIds()
    }
}
