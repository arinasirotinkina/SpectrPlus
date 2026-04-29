package com.example.spectrplus.repository.social

import com.example.spectrplus.entity.social.ForumCategory
import com.example.spectrplus.entity.social.TopicEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TopicRepository : JpaRepository<TopicEntity, Long> {
    fun findByCategory(category: ForumCategory): List<TopicEntity>
    fun countByAuthorId(authorId: Long): Long
}
