package com.example.spectrplus.repository.social

import com.example.spectrplus.entity.social.ChatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatRepository : JpaRepository<ChatEntity, Long> {

    @Query("""
        SELECT c FROM ChatEntity c
        WHERE c.user1Id = :userId OR c.user2Id = :userId
    """)
    fun findAllByUser(userId: Long): List<ChatEntity>

    fun findByUser1IdAndUser2Id(user1Id: Long, user2Id: Long): ChatEntity?

    fun findByUser2IdAndUser1Id(user1Id: Long, user2Id: Long): ChatEntity?
}