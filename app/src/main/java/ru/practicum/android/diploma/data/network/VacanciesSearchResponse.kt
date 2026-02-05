package ru.practicum.android.diploma.data.network

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.VacancyDto

data class VacanciesSearchResponse(
    val found: Int,
    val items: List<VacancyDto>,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page")
    val perPage: Int
) : Response()
