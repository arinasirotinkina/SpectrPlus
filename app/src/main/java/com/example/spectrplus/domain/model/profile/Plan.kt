package com.example.spectrplus.domain.model.profile

import java.time.LocalDate

data class Plan(
    val id: Long,
    val date: LocalDate,
    val title: String,
    val time: String,
    val done: Boolean
)
