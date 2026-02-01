package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.Mapper
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class FavoriteVacanciesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val mapper: Mapper
) : FavoriteVacanciesRepository {

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> = flow {
        val favoriteVacancyEntities = appDatabase.favoriteVacancyDao().getVacancies()
        val vacancies = with(mapper) {
            favoriteVacancyEntities.map { it.toVacancy() }
        }
        emit(vacancies)
    }

    override suspend fun addToFavoriteVacancies(vacancy: Vacancy) {
        val favoriteVacancyEntity = with(mapper) {
            vacancy.toEntity()
        }
        appDatabase.favoriteVacancyDao().insertVacancy(favoriteVacancyEntity)
    }

    override suspend fun deleteFromFavoriteVacancies(vacancy: Vacancy) {
        val favoriteVacancyEntity = with(mapper) {
            vacancy.toEntity()
        }
        appDatabase.favoriteVacancyDao().deleteVacancy(favoriteVacancyEntity)
    }

    override suspend fun getFavoriteVacanciesIds() : List<Int> {
        return appDatabase.favoriteVacancyDao().getVacanciesIds()
    }

}
