package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.Vacancy

data class SearchUiState(
    val query: String = "",
    val vacancies: List<Vacancy> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
