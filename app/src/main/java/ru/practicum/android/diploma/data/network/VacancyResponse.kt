package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.VacancyDto

data class VacancyResponse(
    val vacancy: VacancyDto,
) : Response()
