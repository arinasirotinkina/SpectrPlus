package com.example.spectrplus.entity.social

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chats")
data class ChatEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val user1Id: Long,
    val user2Id: Long,

    val createdAt: LocalDateTime = LocalDateTime.now()
)