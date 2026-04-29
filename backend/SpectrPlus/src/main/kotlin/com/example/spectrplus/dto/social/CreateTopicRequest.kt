package com.example.spectrplus.dto.social

data class CreateTopicRequest(
    val title: String,
    val category: String,
    val content: String
)