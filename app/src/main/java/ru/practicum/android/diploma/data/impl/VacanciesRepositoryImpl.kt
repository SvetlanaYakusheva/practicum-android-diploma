package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.Mapper
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.VacanciesSearchRequest
import ru.practicum.android.diploma.data.network.VacanciesSearchResponse
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: Mapper
) : VacanciesRepository {

    override suspend fun searchVacancies(
        query: String,
        page: Int,
        perPage: Int
    ): Resource<List<Vacancy>> {

        val response = networkClient.doRequest(
            VacanciesSearchRequest(
                text = query,
                filter = Filter(),
                page = page,
                perPage = perPage
            )
        )

        return when (response.resultCode) {
            NetworkClient.HTTP_SUCCESS -> {
                val vacancies = with(mapper) {
                    (response as VacanciesSearchResponse)
                        .items
                        .map { it.toVacancy() }
                }
                Resource.Success(vacancies)
            }

            NetworkClient.HTTP_NOTHING_FOUND -> {
                Resource.Success(emptyList())
            }

            NetworkClient.HTTP_NO_CONNECTION ->
                Resource.Error(ErrorType.NoConnection, MESSAGE_NO_CONNECTION)

            else ->
                Resource.Error(ErrorType.ServerError, MESSAGE_SERVER_ERROR)
        }
    }

    companion object {
        const val MESSAGE_NO_CONNECTION = "Нет интернета"
        const val MESSAGE_SERVER_ERROR = "Внутренняя ошибка сервера"
    }
}
