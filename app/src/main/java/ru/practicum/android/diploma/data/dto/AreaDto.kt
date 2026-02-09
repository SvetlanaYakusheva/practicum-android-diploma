package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDto(
    val id: String,
    val name: String,
    @SerializedName("parentId")
    val parentId: String? = null,
    val areas: List<AreaDto> = emptyList()
)
