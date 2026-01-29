package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class EmployerDto(
    val id: String?,
    val name: String,
    @SerializedName("logo")
    val logoUrl: String?
)
