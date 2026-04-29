package com.example.spectrplus.entity.profile

import jakarta.persistence.*

@Entity
@Table(
    name = "favorites",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "article_id"])]
)
class Favorite(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "article_id")
    val articleId: Long
)