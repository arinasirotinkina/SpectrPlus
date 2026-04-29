package com.example.spectrplus.presentation.viewmodel.social

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.model.social.Chat
import com.example.spectrplus.domain.interactor.social.ChatInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor
) : ViewModel() {

    var chats by mutableStateOf<List<Chat>>(emptyList())
        private set

    fun load() {
        viewModelScope.launch {
            chats = chatInteractor.getChats()
        }
    }
}