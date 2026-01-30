package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.VacancyRequest
import ru.practicum.android.diploma.data.network.VacancyResponse
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.toVacancy

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : VacancyDetailsRepository {
    override suspend fun getVacancyById(vacancyId: String): Flow<Resource<Vacancy>> = flow {
        val response = networkClient.doRequest(VacancyRequest(vacancyId))
        when (response.resultCode) {
            200 -> {
                val result = (response as VacancyResponse).vacancy.toVacancy()
                result.isFavorite = appDatabase.favoriteVacancyDao().findVacancyById(vacancyId).isNotEmpty()
                emit(Resource.Success(result))
            }
            404 -> {
                emit(Resource.Error(ErrorType.ServerError, "Вакансия не найдена"))
            }
            else -> {
                emit(Resource.Error(ErrorType.ServerError, "Произошла сетевая ошибка"))
            }
        }
    }
}
