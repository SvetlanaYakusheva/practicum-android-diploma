package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {

    @Query("SELECT * FROM favorite_vacancy_table ORDER BY vacancyId DESC")
    fun getVacancies(): Flow<List<FavoriteVacancyEntity>>
}
