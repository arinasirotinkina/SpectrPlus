package com.example.spectrplus.entity.social

import jakarta.persistence.*


@Entity
@Table(name = "topic_subscriptions")
data class TopicSubscription(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val userId: Long,

    val topicId: Long
)