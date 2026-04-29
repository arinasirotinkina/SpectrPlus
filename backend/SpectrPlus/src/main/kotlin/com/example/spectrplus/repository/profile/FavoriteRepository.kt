package com.example.spectrplus.repository.profile

import com.example.spectrplus.entity.profile.Favorite
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteRepository : JpaRepository<Favorite, Long> {

    fun findByUserId(userId: Long): List<Favorite>

    fun existsByUserIdAndArticleId(userId: Long, articleId: Long): Boolean

    fun deleteByUserIdAndArticleId(userId: Long, articleId: Long)
}