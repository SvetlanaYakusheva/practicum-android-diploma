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

        return when (response.resultCode) {
            NetworkClient.HTTP_SUCCESS -> {
                val result = (response as VacancyResponse).vacancy.toVacancy()
                Resource.Success(result)
            }

            NetworkClient.HTTP_NOTHING_FOUND -> Resource.Error(ErrorType.NothingFound, MESSAGE_VACANCY_NOT_FOUND)
            else -> Resource.Error(ErrorType.ServerError, MESSAGE_SERVER_ERROR)
        }
    }

    companion object {
        const val MESSAGE_VACANCY_NOT_FOUND = "Вакансия не найдена"
        const val MESSAGE_SERVER_ERROR = "Ошибка сервера"
    }
}
