package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.VacancyRequest
import ru.practicum.android.diploma.data.network.VacancyResponse
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.toVacancy

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyDetailsRepository {
    override suspend fun getVacancyById(vacancyId: String): Resource<Vacancy> {
        val response = networkClient.doRequest(VacancyRequest(vacancyId))
        when (response.resultCode) {
            NetworkClient.HTTP_SUCCESS -> {
                val result = (response as VacancyResponse).vacancy.toVacancy()
                return Resource.Success(result)
            }

            else -> {
                val message = if (response.resultCode == NetworkClient.HTTP_NOTHING_FOUND) {
                    "Вакансия не найдена"
                } else {
                    "Ошибка сервера"
                }
                return Resource.Error(ErrorType.ServerError, message)
            }
        }
    }
}
