package com.example.spectrplus.presentation.viewmodel.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.model.social.Message
import com.example.spectrplus.domain.interactor.social.ChatInteractor
import com.example.spectrplus.domain.interactor.profile.ProfileInteractor
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val socket: WebSocketManager,
    private val profileInteractor: ProfileInteractor
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private var chatId: Long = 0

    private var myId: Long = 0

    private var otherUserId: Long = 0

    private var socketJob: Job? = null

    fun init(userId: Long) {

        if (socketJob != null) return

        socketJob = viewModelScope.launch {

            otherUserId = userId
            myId = profileInteractor.getProfile().id
            chatId = chatInteractor.openChat(userId)
            _messages.value = chatInteractor.getMessages(chatId)
            socket.connect(myId)
            socket.messages.collect { msg ->

                if (msg.chatId == chatId) {
                    _messages.value = _messages.value + msg
                }
            }
        }
    }

    fun send(text: String) {
        if (text.isBlank()) return
        socket.send(chatId, otherUserId, text)
    }

    fun isMyMessage(message: Message): Boolean {
        return message.senderId == myId
    }

    override fun onCleared() {
        super.onCleared()
        socket.close()
        socketJob?.cancel()
    }
}



class WebSocketManager @Inject constructor() {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _messages = MutableSharedFlow<Message>()
    val messages = _messages.asSharedFlow()

    fun connect(userId: Long) {

        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/ws/chat?userId=$userId")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onMessage(webSocket: WebSocket, text: String) {

                val msg = Gson().fromJson(text, Message::class.java)

                CoroutineScope(Dispatchers.Main).launch {
                    _messages.emit(msg)
                }
            }
        })
    }

    fun send(chatId: Long, receiverId: Long, text: String) {

        val json = """
            {
                "chatId": $chatId,
                "receiverId": $receiverId,
                "content": "$text"
            }
        """.trimIndent()

        webSocket?.send(json)
    }

    fun close() {
        webSocket?.close(1000, null)
    }
}