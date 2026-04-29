package com.example.spectrplus.repository.social

import com.example.spectrplus.entity.social.ChatEntity
import com.example.spectrplus.entity.social.MessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface MessageRepository : JpaRepository<MessageEntity, Long> {

    fun findByChatIdOrderByCreatedAtAsc(chatId: Long): List<MessageEntity>
}