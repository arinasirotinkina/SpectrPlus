package com.example.spectrplus.dto.profile.plans

import java.time.LocalDate

data class CreatePlanRequest(
    val date: LocalDate,
    val title: String,
    val time: String
)
