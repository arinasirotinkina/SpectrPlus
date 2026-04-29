package com.example.spectrplus.presentation.viewmodel.education


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.model.education.Article
import com.example.spectrplus.domain.model.education.ArticleComment
import com.example.spectrplus.domain.interactor.education.ArticleInteractor
import com.example.spectrplus.presentation.state.ArticlesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articleInteractor: ArticleInteractor
) : ViewModel() {

    var state by mutableStateOf(ArticlesState())
        private set

    var selectedArticle by mutableStateOf<Article?>(null)
        private set

    var comments by mutableStateOf<List<ArticleComment>>(emptyList())
        private set

    var related by mutableStateOf<List<Article>>(emptyList())
        private set

    var favorites by mutableStateOf<List<Article>>(emptyList())
        private set

    var isDetailLoading by mutableStateOf(false)
        private set

    var detailError by mutableStateOf<String?>(null)
        private set

    private var searchJob: Job? = null

    init {
        loadArticles()
    }

    private fun loadArticles(showFullLoading: Boolean = true) {
        viewModelScope.launch {
            if (showFullLoading) state = state.copy(isLoading = true)
            try {
                val data = articleInteractor.getArticles(state.search, state.category)
                state = state.copy(articles = data, isLoading = false)
            } catch (_: Exception) {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun onSearchChange(query: String) {
        state = state.copy(search = query)

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            loadArticles()
        }
    }

    fun onCategoryChange(category: String?) {
        state = state.copy(category = category)
        loadArticles()
    }

    fun onFavoriteClick(id: Long) {
        viewModelScope.launch {
            val baselineFavorite = when {
                state.articles.any { it.id == id } ->
                    state.articles.first { it.id == id }.isFavorite
                selectedArticle?.id == id ->
                    selectedArticle!!.isFavorite
                favorites.any { it.id == id } -> true
                else -> false
            }
            val newFavorite = !baselineFavorite
            val prevFavorite = baselineFavorite
            try {
                state = state.copy(
                    articles = state.articles.map {
                        if (it.id == id) it.copy(isFavorite = newFavorite) else it
                    }
                )
                articleInteractor.toggleFavorite(id)
                loadArticles(showFullLoading = false)
                selectedArticle?.let { current ->
                    if (current.id == id) {
                        selectedArticle = current.copy(isFavorite = newFavorite)
                    }
                }
                loadFavorites()
            } catch (_: Exception) {
                state = state.copy(
                    articles = state.articles.map {
                        if (it.id == id) it.copy(isFavorite = prevFavorite) else it
                    }
                )
                loadArticles(showFullLoading = false)
            }
        }
    }

    fun loadArticleDetail(id: Long) {
        viewModelScope.launch {
            isDetailLoading = true
            detailError = null
            try {
                selectedArticle = articleInteractor.getById(id)
                comments = articleInteractor.getComments(id)
                related = articleInteractor.getRelated(id)
            } catch (e: Exception) {
                detailError = e.message
            }
            isDetailLoading = false
        }
    }

    fun addComment(articleId: Long, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            try {
                val newComment = articleInteractor.addComment(articleId, text)
                comments = comments + newComment
            } catch (e: Exception) {
                detailError = e.message
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                favorites = articleInteractor.getFavorites()
            } catch (e: Exception) {
                favorites = emptyList()
            }
        }
    }
}
