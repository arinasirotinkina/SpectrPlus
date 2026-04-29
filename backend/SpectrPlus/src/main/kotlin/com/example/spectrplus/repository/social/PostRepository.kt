package com.example.spectrplus.repository.social

import com.example.spectrplus.entity.social.PostEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<PostEntity, Long> {
    fun findByTopicId(topicId: Long): List<PostEntity>
    fun countByTopicId(topicId: Long): Long
    fun countByAuthorId(authorId: Long): Long
}
