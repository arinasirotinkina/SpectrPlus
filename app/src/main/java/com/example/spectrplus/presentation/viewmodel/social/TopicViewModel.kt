package com.example.spectrplus.presentation.viewmodel.social

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.model.social.Post
import com.example.spectrplus.domain.model.social.Topic
import com.example.spectrplus.domain.interactor.social.ForumInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val forumInteractor: ForumInteractor
) : ViewModel() {

    var topic by mutableStateOf<Topic?>(null)
        private set

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    var isSubscribed by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun load(topicId: Long) {
        viewModelScope.launch {
            isLoading = true
            try {
                topic = forumInteractor.getTopic(topicId)
                posts = forumInteractor.getPosts(topicId)
                isSubscribed = forumInteractor.isSubscribed(topicId)
            } catch (_: Exception) {
            }
            isLoading = false
        }
    }

    fun send(topicId: Long, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            try {
                val newPost = forumInteractor.addPost(topicId, text)
                posts = posts + newPost
                topic = topic?.copy(postsCount = topic!!.postsCount + 1)
            } catch (_: Exception) {
            }
        }
    }

    fun editPost(postId: Long, newContent: String) {
        if (newContent.isBlank()) return
        viewModelScope.launch {
            try {
                val updated = forumInteractor.editPost(postId, newContent)
                posts = posts.map { if (it.id == postId) updated else it }
            } catch (_: Exception) {
            }
        }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch {
            try {
                forumInteractor.deletePost(postId)
                posts = posts.filter { it.id != postId }
                topic = topic?.copy(postsCount = (topic!!.postsCount - 1).coerceAtLeast(0))
            } catch (_: Exception) {
            }
        }
    }

    fun toggleSubscribe(topicId: Long) {
        viewModelScope.launch {
            try {
                val current = topic
                if (isSubscribed) {
                    forumInteractor.unsubscribe(topicId)
                    isSubscribed = false
                    topic = current?.copy(
                        subscribersCount = (current.subscribersCount - 1).coerceAtLeast(0)
                    )
                } else {
                    forumInteractor.subscribe(topicId)
                    isSubscribed = true
                    topic = current?.copy(
                        subscribersCount = current.subscribersCount + 1
                    )
                }
            } catch (_: Exception) {
            }
        }
    }

    fun subscribe(topicId: Long) {
        viewModelScope.launch {
            try {
                forumInteractor.subscribe(topicId)
                isSubscribed = true
            } catch (_: Exception) {
            }
        }
    }
}

val forumCategories = listOf(
    "QUESTIONS",
    "METHODS",
    "DAILY_LIFE",
    "EDUCATION",
    "EMOTIONAL_SUPPORT",
    "REGIONAL"
)
