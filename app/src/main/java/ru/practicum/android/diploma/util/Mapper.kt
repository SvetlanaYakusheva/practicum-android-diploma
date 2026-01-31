package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Vacancy

fun AreaDto.toArea(): Area = Area(
    id = id,
    name = name,
    parentId = parentId,
    areas = areas.map {
        it.toArea()
    }
)

fun FavoriteVacancyEntity.toVacancy() = Vacancy(
    id = vacancyId,
    name = name,
    employerName = employerName,
    employerLogoPath = employerLogoPath,
    employment = employment,
    description = description,
    contactsEmail = contactsEmail,
    contactsName = contactsName,
    contactsPhones = contactsPhones,
    experienceName = experienceName,
    keySkills = keySkills,
    salaryFrom = salaryFrom,
    salaryTo = salaryTo,
    salaryCurrencyName = salaryCurrencyName,
    addressCity = addressCity,
    schedule = schedule,
    isFavorite = true
)

fun IndustryDto.toIndustry() = Industry(
    id = id,
    name = name,
)

fun Vacancy.toEntity() = FavoriteVacancyEntity(
    vacancyId = id,
    name = name,
    employerName = employerName,
    employerLogoPath = employerLogoPath ?: "",
    employment = employment,
    description = description,
    contactsEmail = contactsEmail,
    contactsName = contactsName,
    contactsPhones = contactsPhones,
    experienceName = experienceName ?: "",
    keySkills = keySkills ?: "",
    salaryFrom = salaryFrom,
    salaryTo = salaryTo,
    salaryCurrencyName = salaryCurrencyName,
    addressCity = addressCity,
    schedule = schedule ?: ""
)

fun VacancyDto.toVacancy() = Vacancy(
    id = id,
    name = name,
    employerName = employer?.name ?: "",
    employerLogoPath = employer?.logoUrl ?: "",
    employment = employment?.name ?: "",
    description = "",
    contactsEmail = contacts?.email,
    contactsName = contacts?.name,
    contactsPhones = contacts?.phones?.joinToString(),
    experienceName = experience?.name,
    keySkills = "",
    salaryFrom = salary?.salaryFrom,
    salaryTo = salary?.salaryTo,
    salaryCurrencyName = salary?.currency,
    addressCity = address.city,
    schedule = schedule?.name,
    isFavorite = false
)
