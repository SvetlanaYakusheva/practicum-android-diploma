package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavoriteVacanciesRepository {

    fun getFavoriteVacancies() : Flow<List<Vacancy>>

    suspend fun addToFavoriteVacancies(vacancy: Vacancy)

    suspend fun deleteFromFavoriteVacancies(vacancy: Vacancy)

    suspend fun getFavoriteVacanciesIds() : List<String>
}
