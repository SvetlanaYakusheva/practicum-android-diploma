package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.VacancyDto

interface VacanciesAPI {

    @Headers(
        "Authorization: Bearer ${BuildConfig.API_ACCESS_TOKEN}",
        "HH-User-Agent: FindJobApp/1.0 (i@bergolz.ru)"
    )
    @GET("/vacancies")
    suspend fun getVacancies(@QueryMap options: Map<String, String>): VacanciesSearchResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.API_ACCESS_TOKEN}",
        "HH-User-Agent: FindJobApp/1.0 (i@bergolz.ru)"
    )
    @GET("/vacancies/{vacancyId}")
    suspend fun getVacancyFull(
        @Path("vacancyId") vacancyId: String
    ): VacancyDto
}
