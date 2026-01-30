package ru.practicum.android.diploma.util

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.data.dto.AddressDto
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.ContactsDto
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.EmploymentDto
import ru.practicum.android.diploma.data.dto.ExperienceDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.KeySkillDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.ScheduleDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.VacancyFullDto
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
    schedule = schedule?.name,
    isFavorite = false
)

fun VacancyFullDto.toVacancy() = Vacancy(
    id = id,
    name = name,
    employerName = employer?.name ?: "",
    employerLogoPath = employer?.logoUrl ?: "",
    employment = employment?.name ?: "",
    description = description,
    contactsEmail = contacts?.email,
    contactsName = contacts?.name,
    contactsPhones = contacts?.phones?.joinToString(),
    experienceName = experience?.name,
    keySkills = keySkills.toString(),
    salaryFrom = salary?.salaryFrom,
    salaryTo = salary?.salaryTo,
    salaryCurrencyName = salary?.currency,
    schedule = schedule?.name,
    isFavorite = false
)



