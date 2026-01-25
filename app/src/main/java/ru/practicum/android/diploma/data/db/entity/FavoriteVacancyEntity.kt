package ru.practicum.android.diploma.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancy_table")
data class FavoriteVacancyEntity(
    @PrimaryKey @ColumnInfo(name = "vacancy_id")
    val vacancyId: String
)
