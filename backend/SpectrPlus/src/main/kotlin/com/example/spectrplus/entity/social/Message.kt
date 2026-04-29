package com.example.spectrplus.entity.social

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "messages")
data class MessageEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val chatId: Long,

    val senderId: Long,

    val senderProfessionalLabel: String? = null,

    val content: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)