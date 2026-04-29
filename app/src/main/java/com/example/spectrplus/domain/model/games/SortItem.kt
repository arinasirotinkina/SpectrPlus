package com.example.spectrplus.domain.model.games

data class SortItem(
    val id: Int,
    val name: String,
    val category: String,
    val emoji: String = "❓",
    val bgColor: Long = 0xFFFFFFFF
)