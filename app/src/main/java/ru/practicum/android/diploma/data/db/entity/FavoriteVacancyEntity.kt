package ru.practicum.android.diploma.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancy_table", indices = [Index("vacancy_id")])
data class FavoriteVacancyEntity(
    @PrimaryKey @ColumnInfo(name = "vacancy_id") val vacancyId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "salary_from") val salaryFrom: Int?,
    @ColumnInfo(name = "salary_to") val salaryTo: Int?,
    @ColumnInfo(name = "salary_currency_name") val salaryCurrencyName: String?,
    @ColumnInfo(name = "experience_name") val experienceName: String,
    @ColumnInfo(name = "schedule") val schedule: String,
    @ColumnInfo(name = "employment") val employment: String,
    @ColumnInfo(name = "contacts_name") val contactsName: String?,
    @ColumnInfo(name = "contacts_email") val contactsEmail: String?,
    @ColumnInfo(name = "contacts_phones") val contactsPhones: String?,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "employer_name") val employerName: String,
    @ColumnInfo(name = "employer_logo_path") val employerLogoPath: String,
    @ColumnInfo(name = "key_skills") val keySkills: String
)
