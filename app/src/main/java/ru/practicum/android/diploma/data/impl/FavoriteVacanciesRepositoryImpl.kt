package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.Mapper
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class FavoriteVacanciesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val mapper: Mapper
) : FavoriteVacanciesRepository {

    override fun getFavoriteVacancies(): Flow<Resource<List<Vacancy>>> = flow<Resource<List<Vacancy>>> {
        val favoriteVacancyEntities = appDatabase.favoriteVacancyDao().getVacancies()
        val vacancies = with(mapper) {
            favoriteVacancyEntities.map { it.toVacancy() }
        }
        emit(Resource.Success(vacancies))

    }.catch { e ->
        // Ловим только реальные ошибки, не мешая корутинам отменяться
        emit(Resource.Error(ErrorType.SQLError))

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

    override fun getFavoriteVacanciesIds(): Flow<List<String>> {
        return appDatabase.favoriteVacancyDao().getVacanciesIds()
    }

}
