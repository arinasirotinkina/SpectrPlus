package com.example.spectrplus.presentation.screen.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

val GameBg = Color(0xFFF5F7FD)
val GamePrimary = Color(0xFF5B6DFF)
val GameSuccess = Color(0xFF4CAF50)
val GameWarning = Color(0xFFFFA726)
val GameError = Color(0xFFEF5350)
val GameCardBg = Color(0xFFFFFFFF)

@Composable
fun GameScaffold(
    title: String,
    navController: NavController?,
    progress: Float? = null,
    onRestart: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(GameBg)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController?.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = GamePrimary
                    )
                }
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2240),
                    modifier = Modifier.weight(1f)
                )
                if (onRestart != null) {
                    IconButton(onClick = onRestart) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Заново",
                            tint = GamePrimary
                        )
                    }
                }
            }

            if (progress != null) {
                LinearProgressIndicator(
                    progress = progress.coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(8.dp),
                    color = GamePrimary,
                    trackColor = Color(0xFFE0E4F5)
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                content(PaddingValues(16.dp))
            }
        }
    }
}

@Composable
fun FeedbackBanner(correct: Boolean?, text: String? = null) {
    if (correct == null) return
    val bg = if (correct) GameSuccess else GameError
    val msg = text ?: if (correct) "Молодец!" else "Попробуй ещё раз"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(bg, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = msg,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CompletedOverlay(
    visible: Boolean,
    message: String = "Отличная работа!",
    onAgain: () -> Unit,
    onBack: () -> Unit
) {
    if (!visible) return
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🎉", fontSize = 64.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    message,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = onAgain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GamePrimary)
                ) {
                    Text("Играть снова", fontSize = 18.sp)
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E4F5)),
                ) {
                    Text("К играм", fontSize = 18.sp, color = Color(0xFF1F2240))
                }
            }
        }
    }
}

@Composable
fun BigEmojiCard(
    emoji: String,
    label: String? = null,
    selected: Boolean = false,
    correct: Boolean? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Int = 96
) {
    val borderColor by animateColorAsState(
        targetValue = when (correct) {
            true -> GameSuccess
            false -> GameError
            else -> if (selected) GamePrimary else Color.Transparent
        },
        label = "border"
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.06f else 1f,
        label = "scale"
    )
    Card(
        modifier = modifier
            .size(size.dp)
            .scale(scale)
            .border(3.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GameCardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = (size * 0.45f).sp)
            if (label != null) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1F2240),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ColorDot(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    size: Int = 48
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .border(
                width = if (selected) 4.dp else 2.dp,
                color = if (selected) Color.Black else Color(0x33000000),
                shape = CircleShape
            )
            .background(color, CircleShape)
            .clickable { onClick() }
    )
}
