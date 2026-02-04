package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {

    @Query("SELECT * FROM favorite_vacancy_table")
    suspend fun getVacancies(): List<FavoriteVacancyEntity>

    @Query("SELECT vacancy_id FROM favorite_vacancy_table")
    fun getVacanciesIds(): Flow<List<String>>

    @Insert(entity = FavoriteVacancyEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVacancy(vacancy: FavoriteVacancyEntity)

    @Delete
    suspend fun deleteVacancy(vacancy: FavoriteVacancyEntity)

    @Query("select * from favorite_vacancy_table where vacancy_id = :vacancyId")
    suspend fun getVacancyById(vacancyId: String): FavoriteVacancyEntity
}
