package com.example.spectrplus.data.mapper

import com.example.spectrplus.data.dto.articles.ArticleCommentDto
import com.example.spectrplus.data.dto.articles.ArticleDto
import com.example.spectrplus.domain.model.education.Article
import com.example.spectrplus.domain.model.education.ArticleComment

fun ArticleDto.toEntity() = Article(
    id, title, content, author, category, isFavorite
)

fun Article.toDomain() = Article(
    id, title, content, author, category, isFavorite
)

fun ArticleCommentDto.toDomain() = ArticleComment(
    id = id,
    authorId = authorId,
    authorName = authorName,
    content = content,
    createdAt = createdAt
)
