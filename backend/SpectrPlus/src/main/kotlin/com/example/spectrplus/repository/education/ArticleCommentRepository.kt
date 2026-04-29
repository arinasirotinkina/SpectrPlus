package com.example.spectrplus.repository.education

import com.example.spectrplus.entity.education.ArticleComment
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleCommentRepository : JpaRepository<ArticleComment, Long> {
    fun findByArticleIdOrderByCreatedAtAsc(articleId: Long): List<ArticleComment>
}
