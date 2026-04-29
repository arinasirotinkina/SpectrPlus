package com.example.spectrplus.repository.education

import com.example.spectrplus.entity.education.Article
import com.example.spectrplus.entity.education.ArticleCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ArticleRepository : JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    fun findTop5ByCategoryAndIdNot(category: ArticleCategory, id: Long): List<Article>
}
