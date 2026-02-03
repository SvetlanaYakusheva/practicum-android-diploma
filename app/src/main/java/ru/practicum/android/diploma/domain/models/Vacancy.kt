package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrencyName: String?,
    val addressCity: String?,
    val addressFull: String?,
    val experienceName: String?,
    val schedule: String?,
    val employment: String,
    val contactsName: String?,
    val contactsEmail: String?,
    val contactsPhones: List<Phone>?,
    val description: String,
    val employerName: String,
    val employerLogoPath: String?,
    val skills: List<String>?,
    val areaId: String?,
    val areaName: String?,
    val areaParentId: String?,
    var isFavorite: Boolean
)
