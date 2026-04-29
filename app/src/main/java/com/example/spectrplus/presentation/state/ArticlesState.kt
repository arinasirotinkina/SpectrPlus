package com.example.spectrplus.presentation.state

import com.example.spectrplus.domain.model.education.Article

data class ArticlesState(
    val articles: List<Article> = emptyList(),
    val search: String = "",
    val category: String? = null,
    val isLoading: Boolean = false
)