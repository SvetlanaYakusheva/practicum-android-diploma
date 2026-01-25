package ru.practicum.android.diploma.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancy_table", indices = [Index("vacancy_id")])
data class FavoriteVacancyEntity(
    @PrimaryKey @ColumnInfo(name = "vacancy_id")
    val vacancyId: String
)
