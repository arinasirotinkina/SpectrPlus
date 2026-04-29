package com.example.spectrplus.repository.social

import com.example.spectrplus.entity.social.TopicSubscription
import org.springframework.data.jpa.repository.JpaRepository

interface SubscriptionRepository : JpaRepository<TopicSubscription, Long> {
    fun findByUserId(userId: Long): List<TopicSubscription>
    fun existsByUserIdAndTopicId(userId: Long, topicId: Long): Boolean
    fun deleteByUserIdAndTopicId(userId: Long, topicId: Long)
    fun countByTopicId(topicId: Long): Long
}
