package ru.practicum.android.diploma.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Vacancy

class Mapper(private val gson: Gson) {
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
        skills = gson.fromJson(skills, object : TypeToken<List<String>>() {}.type),
        salaryFrom = salaryFrom,
        salaryTo = salaryTo,
        salaryCurrencyName = salaryCurrencyName,
        addressCity = addressCity,
        addressFull = addressFull,
        schedule = schedule,
        isFavorite = true,
        areaId = areaId,
        areaName = areaName,
        areaParentId = areaParentId
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
        skills = gson.toJson(skills),
        salaryFrom = salaryFrom,
        salaryTo = salaryTo,
        salaryCurrencyName = salaryCurrencyName,
        addressCity = addressCity,
        addressFull = addressFull,
        schedule = schedule ?: "",
        areaId = areaId ?: "",
        areaName = areaName ?: "",
        areaParentId = areaParentId ?: ""
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
        skills = skills,
        salaryFrom = salary?.salaryFrom,
        salaryTo = salary?.salaryTo,
        salaryCurrencyName = salary?.currency,
        addressCity = address.city,
        addressFull = address.raw,
        schedule = schedule?.name,
        areaId = area.id,
        areaName = area.name,
        areaParentId = area.parentId,
        isFavorite = false
    )
}
