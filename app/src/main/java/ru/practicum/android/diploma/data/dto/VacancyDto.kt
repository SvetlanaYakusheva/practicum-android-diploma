package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val address: AddressDto,
    val experience: ExperienceDto?,
    val schedule: ScheduleDto?,
    val employment: EmploymentDto?,
    val contacts: ContactsDto?,
    val employer: EmployerDto?,
    val area: AreaDto,
    @SerializedName("response_url")
    val responseUrl: Any,
    val url: String

)
