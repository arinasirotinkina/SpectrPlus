package com.example.spectrplus.data.mapper

import com.example.spectrplus.data.dto.social.ChatDto
import com.example.spectrplus.data.dto.social.MessageDto
import com.example.spectrplus.data.dto.social.PostDto
import com.example.spectrplus.data.dto.social.TopicDto
import com.example.spectrplus.domain.model.social.Chat
import com.example.spectrplus.domain.model.social.Message
import com.example.spectrplus.domain.model.social.Post
import com.example.spectrplus.domain.model.social.Topic

fun TopicDto.toDomain(): Topic {
    return Topic(
        id = id,
        title = title,
        category = category,
        authorId = authorId,
        authorName = authorName,
        createdAt = createdAt.toString(),
        postsCount = postsCount,
        subscribersCount = subscribersCount
    )
}

fun PostDto.toDomain(): Post {
    return Post(
        id = id,
        authorId = authorId,
        authorName = authorName,
        authorProfessionalLabel = authorProfessionalLabel,
        content = content,
        createdAt = createdAt
    )
}

fun ChatDto.toDomain() = Chat(
    id, otherUserId, otherUserName, lastMessage
)

fun MessageDto.toDomain() = Message(
    id = id,
    chatId = chatId,
    senderId = senderId,
    senderProfessionalLabel = senderProfessionalLabel,
    content = content,
    createdAt = createdAt
)