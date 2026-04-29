package com.example.spectrplus.service.education

import com.example.spectrplus.entity.education.Article
import com.example.spectrplus.entity.education.ArticleCategory
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object ArticleSpecification {

    fun build(
        query: String?,
        category: ArticleCategory?
    ): Specification<Article> {

        return Specification { root, _, cb ->

            val predicates = mutableListOf<Predicate>()

            if (!query.isNullOrBlank()) {
                val like = "%${query.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("content")), like)
                    )
                )
            }

            if (category != null) {
                predicates.add(
                    cb.equal(root.get<ArticleCategory>("category"), category)
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}