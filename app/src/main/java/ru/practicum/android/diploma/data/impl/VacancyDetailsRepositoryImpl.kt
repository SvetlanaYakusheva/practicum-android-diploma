package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.VacancyRequest
import ru.practicum.android.diploma.data.network.VacancyResponse
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.toVacancy

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val favoritesDatabase: AppDatabase
) : VacancyDetailsRepository {
    override suspend fun getVacancyById(id: String): Vacancy {
        val response = networkClient.doRequest(VacancyRequest(id))
        when (response.resultCode) {
            200 -> {

                val result = (response as VacancyResponse).vacancy.toVacancy()
                result.isFavorite = favoritesDatabase.favoriteVacancyDao().findVacancyById(id).isNotEmpty()
            }
            404 -> {
                emit(Resource.Success(emptyList()))
            }
            else -> {
                emit(Resource.Error("Произошла сетевая ошибка"))
            }
        }
    }
}
