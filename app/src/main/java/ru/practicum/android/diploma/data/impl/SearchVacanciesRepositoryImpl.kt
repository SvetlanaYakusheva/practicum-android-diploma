package ru.practicum.android.diploma.data.impl

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.Mapper
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.network.VacanciesSearchRequest
import ru.practicum.android.diploma.data.network.VacanciesSearchResponse
import ru.practicum.android.diploma.domain.api.SearchVacanciesRepository
import ru.practicum.android.diploma.domain.api.VacanciesSearchResult
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class SearchVacanciesRepositoryImpl(
    private val context: Context,
    private val networkClient: NetworkClient,
    private val mapper: Mapper
) : SearchVacanciesRepository {
    private fun makeErrorMessage(response: Response): String {
        val header = context.getString(R.string.server_error_message)
        return "$header : ${response.resultCode}"
    }

    override fun searchVacancies(
        expression: String,
        page: Int,
        perPage: Int,
    ): Flow<Resource<VacanciesSearchResult>> = flow {

        val response = networkClient.doRequest(
            VacanciesSearchRequest(
                text = expression,
                page = page,
                perPage = perPage,
            )
        )

        emit(
            when (response.resultCode) {
                NetworkClient.HTTP_NO_CONNECTION -> Resource.Error(ErrorType.NoConnection)
                NetworkClient.HTTP_SUCCESS -> {
                    with(response as VacanciesSearchResponse) {
                        Resource.Success(
                            VacanciesSearchResult(
                                vacancies = this.items.map {
                                    with(mapper) { it.toVacancy() }
                                },
                                page = this.page,
                                found = this.found,
                                count = this.pages
                            )
                        )
                    }
                }

                else -> Resource.Error(
                    errorType = ErrorType.ServerError,
                    message = makeErrorMessage(response)
                )
            }
        )
    }
}
