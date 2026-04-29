package com.example.spectrplus.data.dto.profile

data class PlanResponseDto(
    val id: Long,
    val date: String,
    val title: String,
    val time: String,
    val done: Boolean
)

data class CreatePlanRequestDto(
    val date: String,
    val title: String,
    val time: String
)

data class UpdatePlanRequestDto(
    val date: String,
    val title: String,
    val time: String,
    val done: Boolean
)
