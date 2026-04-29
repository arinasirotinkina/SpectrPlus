package com.example.spectrplus.mapper

import com.example.spectrplus.dto.social.PostResponse
import com.example.spectrplus.dto.social.TopicResponse
import com.example.spectrplus.entity.social.PostEntity
import com.example.spectrplus.entity.social.TopicEntity

fun TopicEntity.toDto(postsCount: Int = 0, subscribersCount: Int = 0) = TopicResponse(
    id = id,
    title = title,
    category = category.name,
    authorId = authorId,
    authorName = authorName,
    createdAt = createdAt,
    postsCount = postsCount,
    subscribersCount = subscribersCount
)

fun PostEntity.toDto() = PostResponse(
    id = id,
    authorId = authorId,
    authorName = authorName,
    authorProfessionalLabel = authorProfessionalLabel,
    content = content,
    createdAt = createdAt
)
