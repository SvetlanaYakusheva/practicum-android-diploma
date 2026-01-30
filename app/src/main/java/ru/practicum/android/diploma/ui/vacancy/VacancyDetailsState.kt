package ru.practicum.android.diploma.ui.vacancy

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface VacancyDetailsState {
    data object Loading: VacancyDetailsState

    data class Content(val vacancy: Vacancy) : VacancyDetailsState

    data object VacancyNotFoundError : VacancyDetailsState

    data object VacancyServerError : VacancyDetailsState
}
