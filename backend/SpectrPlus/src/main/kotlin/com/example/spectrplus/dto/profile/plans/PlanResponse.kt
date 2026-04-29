package com.example.spectrplus.dto.profile.plans

import java.time.LocalDate

data class PlanResponse(
    val id: Long,
    val date: LocalDate,
    val title: String,
    val time: String,
    val done: Boolean
)