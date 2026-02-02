package ru.practicum.android.diploma.data.impl

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.Mapper
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.VacancyRequest
import ru.practicum.android.diploma.data.network.VacancyResponse
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context,
    private val mapper: Mapper
) : VacancyDetailsRepository {
    override fun getVacancyById(vacancyId: String): Flow<Resource<Vacancy>> = flow {
        val response = networkClient.doRequest(VacancyRequest(vacancyId))

        emit(
            when (response.resultCode) {
                NetworkClient.HTTP_SUCCESS -> {
                    val result = with(mapper) { (response as VacancyResponse).vacancy.toVacancy() }
                    Resource.Success(result)
                }

                NetworkClient.HTTP_NOTHING_FOUND -> Resource.Error(
                    ErrorType.NothingFound,
                    makeErrorMessage(ErrorType.NothingFound)
                )

                else -> Resource.Error(ErrorType.ServerError, makeErrorMessage(ErrorType.ServerError))
            }
        )
    }

    private fun makeErrorMessage(errorType: ErrorType): String {
        return when (errorType) {
            ErrorType.NothingFound -> context.getString(R.string.vacancy_not_found)
            else -> context.getString(R.string.server_error_message)
        }
    }
}
