package com.example.spectrplus.presentation.viewmodel.social

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.model.social.Topic
import com.example.spectrplus.domain.interactor.social.ForumInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val forumInteractor: ForumInteractor
) : ViewModel() {

    var state by mutableStateOf(ForumState())
        private set

    fun load(category: String? = null) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val topics = forumInteractor.getTopics(category)
            state = state.copy(topics = topics, isLoading = false)
        }
    }

    fun createTopic(
        title: String,
        category: String,
        content: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            forumInteractor.createTopic(title, category, content)
            onDone()
        }
    }
}
data class ForumState(
    val topics: List<Topic> = emptyList(),
    val isLoading: Boolean = false
)