package com.example.spectrplus.presentation.screen.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.InitialsAvatar
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.presentation.viewmodel.social.ChatListViewModel
import com.example.spectrplus.presentation.viewmodel.social.ChatViewModel

@Composable
fun ChatsSection(navController: NavController) {
    val viewModel: ChatListViewModel = hiltViewModel()

    LaunchedEffect(Unit) { viewModel.load() }

    if (viewModel.chats.isEmpty()) {
        EmptyState(
            emoji = "💬",
            title = "Пока нет диалогов",
            subtitle = "Начните общение из карточек пользователей"
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(viewModel.chats) { chat ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SpectrColors.Card)
                    .clickable {
                        navController.navigate("chat/${chat.otherUserId}")
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InitialsAvatar(name = chat.otherUserName, size = 48)
                Spacer(Modifier.size(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        chat.otherUserName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SpectrColors.Text
                    )
                    Spacer(Modifier.size(2.dp))
                    Text(
                        chat.lastMessage.orEmpty().ifBlank { "Нет сообщений" },
                        fontSize = 13.sp,
                        color = SpectrColors.TextMuted,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun ChatListScreen(navController: NavController) {
    ChatsSection(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(userId: Long) {

    val viewModel: ChatViewModel = hiltViewModel()
    var text by remember { mutableStateOf("") }

    val messages by viewModel.messages.collectAsState()

    LaunchedEffect(Unit) { viewModel.init(userId) }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(title = "Диалог")
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (messages.isEmpty()) {
                Box(Modifier.weight(1f)) {
                    EmptyState(
                        emoji = "👋",
                        title = "Напишите первым",
                        subtitle = "Начните дружеский разговор"
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        val isMine = viewModel.isMyMessage(msg)
                        MessageBubble(
                            text = msg.content,
                            isMine = isMine,
                            professionalLabel = msg.senderProfessionalLabel
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SpectrColors.Card)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Сообщение") },
                    shape = RoundedCornerShape(24.dp),
                    colors = spectrFieldColors(),
                    maxLines = 4
                )
                Spacer(Modifier.size(8.dp))
                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            viewModel.send(text)
                            text = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(SpectrColors.Primary)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(text: String, isMine: Boolean, professionalLabel: String? = null) {
    val shape = if (isMine)
        RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp)
    else
        RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp)
    val bg = if (isMine) SpectrColors.Primary else SpectrColors.Card
    val fg = if (isMine) Color.White else SpectrColors.Text

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
        ) {
            if (!isMine && !professionalLabel.isNullOrBlank()) {
                Text(
                    professionalLabel,
                    fontSize = 11.sp,
                    color = SpectrColors.Primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp, end = 4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(bg)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(text, color = fg, fontSize = 14.sp)
            }
        }
    }
}
