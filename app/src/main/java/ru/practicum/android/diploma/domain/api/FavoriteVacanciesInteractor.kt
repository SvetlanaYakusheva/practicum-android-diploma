package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface FavoriteVacanciesInteractor {

    fun getFavoriteVacancies(): Flow<Resource<List<Vacancy>>>

    suspend fun addToFavoriteVacancies(vacancy: Vacancy)

    suspend fun deleteFromFavoriteVacancies(vacancy: Vacancy)

    fun getFavoriteVacanciesIds(): Flow<List<String>>
}
