package com.example.spectrplus.service.education


import com.example.spectrplus.dto.education.article.ArticleCommentResponse
import com.example.spectrplus.dto.education.article.ArticleResponse
import com.example.spectrplus.dto.education.article.CreateArticleCommentRequest
import com.example.spectrplus.entity.education.Article
import com.example.spectrplus.entity.education.ArticleCategory
import com.example.spectrplus.entity.education.ArticleComment
import com.example.spectrplus.entity.profile.AccountRole
import com.example.spectrplus.entity.profile.Favorite
import com.example.spectrplus.repository.education.ArticleCommentRepository
import com.example.spectrplus.repository.education.ArticleRepository
import com.example.spectrplus.repository.profile.FavoriteRepository
import com.example.spectrplus.repository.profile.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class ArticleService(

    private val articleRepository: ArticleRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository,
    private val commentRepository: ArticleCommentRepository

) {

    fun getArticles(
        email: String,
        query: String?,
        category: String?
    ): List<ArticleResponse> {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val categoryEnum = category?.let {
            ArticleCategory.valueOf(it)
        }

        val spec = ArticleSpecification.build(query, categoryEnum)

        val articles = articleRepository.findAll(spec)

        val favorites = favoriteRepository.findByUserId(user.id)
            .map { it.articleId }
            .toSet()

        return articles.map {
            ArticleResponse(
                id = it.id,
                title = it.title,
                content = it.content,
                author = it.author,
                category = it.category.name,
                isFavorite = favorites.contains(it.id),
                sourceAttribution = it.sourceAttribution,
                coverImageUrl = it.coverImageUrl
            )
        }
    }

    fun getById(email: String, id: Long): ArticleResponse {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val article = articleRepository.findById(id)
            .orElseThrow { RuntimeException("Article not found") }

        val isFavorite = favoriteRepository.existsByUserIdAndArticleId(user.id, article.id)

        return ArticleResponse(
            id = article.id,
            title = article.title,
            content = article.content,
            author = article.author,
            category = article.category.name,
            isFavorite = isFavorite,
            sourceAttribution = article.sourceAttribution,
            coverImageUrl = article.coverImageUrl
        )
    }

    fun getFavorites(email: String): List<ArticleResponse> {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val favoriteIds = favoriteRepository.findByUserId(user.id).map { it.articleId }

        if (favoriteIds.isEmpty()) return emptyList()

        return articleRepository.findAllById(favoriteIds).map {
            ArticleResponse(
                id = it.id,
                title = it.title,
                content = it.content,
                author = it.author,
                category = it.category.name,
                isFavorite = true,
                sourceAttribution = it.sourceAttribution,
                coverImageUrl = it.coverImageUrl
            )
        }
    }

    fun getRelated(email: String, id: Long): List<ArticleResponse> {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val article = articleRepository.findById(id)
            .orElseThrow { RuntimeException("Article not found") }

        val related = articleRepository.findTop5ByCategoryAndIdNot(article.category, article.id)

        val favorites = favoriteRepository.findByUserId(user.id)
            .map { it.articleId }
            .toSet()

        return related.map {
            ArticleResponse(
                id = it.id,
                title = it.title,
                content = it.content,
                author = it.author,
                category = it.category.name,
                isFavorite = favorites.contains(it.id),
                sourceAttribution = it.sourceAttribution,
                coverImageUrl = it.coverImageUrl
            )
        }
    }

    fun toggleFavorite(email: String, articleId: Long) {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val exists = favoriteRepository.existsByUserIdAndArticleId(user.id, articleId)

        if (exists) {
            favoriteRepository.deleteByUserIdAndArticleId(user.id, articleId)
        } else {
            favoriteRepository.save(
                Favorite(userId = user.id, articleId = articleId)
            )
        }
    }

    fun getComments(articleId: Long): List<ArticleCommentResponse> {
        return commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId).map {
            ArticleCommentResponse(
                id = it.id,
                authorId = it.authorId,
                authorName = it.authorName,
                content = it.content,
                createdAt = it.createdAt
            )
        }
    }

    fun createBySpecialist(
        email: String,
        title: String,
        content: String,
        category: String,
        sourceAttribution: String?,
        coverImageUrl: String?
    ): ArticleResponse {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
        if (user.accountRole != AccountRole.SPECIALIST) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Только для специалистов")
        }
        val cat = try {
            ArticleCategory.valueOf(category)
        } catch (_: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Неизвестная категория")
        }
        val authorName = "${user.firstName} ${user.lastName}"
        val saved = articleRepository.save(
            Article(
                title = title.trim(),
                content = content.trim(),
                author = authorName,
                category = cat,
                sourceAttribution = sourceAttribution?.trim()?.takeIf { it.isNotEmpty() },
                coverImageUrl = coverImageUrl?.trim()?.takeIf { it.isNotEmpty() }
            )
        )
        return ArticleResponse(
            id = saved.id,
            title = saved.title,
            content = saved.content,
            author = saved.author,
            category = saved.category.name,
            isFavorite = false,
            sourceAttribution = saved.sourceAttribution,
            coverImageUrl = saved.coverImageUrl
        )
    }

    fun addComment(email: String, articleId: Long, req: CreateArticleCommentRequest): ArticleCommentResponse {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val saved = commentRepository.save(
            ArticleComment(
                articleId = articleId,
                authorId = user.id,
                authorName = "${user.firstName} ${user.lastName}",
                content = req.content
            )
        )

        return ArticleCommentResponse(
            id = saved.id,
            authorId = saved.authorId,
            authorName = saved.authorName,
            content = saved.content,
            createdAt = saved.createdAt
        )
    }
}
