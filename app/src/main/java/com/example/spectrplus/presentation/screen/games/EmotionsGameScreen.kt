package com.example.spectrplus.presentation.screen.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spectrplus.presentation.viemodel.games.EmotionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionsGameScreen(
    onBack: () -> Unit = {},
    viewModel: EmotionsViewModel = hiltViewModel()
) {
    val currentIndex by viewModel.currentIndex.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val isCorrect by viewModel.isCorrect.collectAsState()
    val score by viewModel.score.collectAsState()
    val completed by viewModel.completed.collectAsState()
    val options by viewModel.currentOptions.collectAsState()
    val question = viewModel.currentQuestion

    val levelLabel = when (question.level) {
        1 -> "Уровень 1 — Основные"
        2 -> "Уровень 2 — Средние"
        else -> "Уровень 3 — Сложные"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("😊 Эмоции", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                },
                actions = {
                    Text("⭐ $score", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp), color = Color(0xFFFF8F00))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFF9C4))
            )
        }
    ) { padding ->
        if (completed) {
            EmotionsCompletedScreen(score, viewModel.totalQuestions, onRestart = { viewModel.restart() })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFFFFBF0))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(levelLabel, style = MaterialTheme.typography.labelLarge, color = Color(0xFFFF8F00))
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / viewModel.totalQuestions },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFFFFB300)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${currentIndex + 1} / ${viewModel.totalQuestions}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(Color.White, RoundedCornerShape(80.dp))
                        .border(4.dp, Color(0xFFFFB300), RoundedCornerShape(80.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(question.emoji, fontSize = 88.sp)
                }
                Spacer(Modifier.height(20.dp))

                Text(
                    text = question.situation,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                )
                Spacer(Modifier.height(24.dp))

                Text(
                    "Какая это эмоция?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    options.chunked(2).forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            row.forEach { option ->
                                EmotionOptionButton(
                                    text = option,
                                    selectedAnswer = selectedAnswer,
                                    correctAnswer = question.correctAnswer,
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.selectAnswer(option) }
                                )
                            }
                        }
                    }
                }

                if (selectedAnswer != null) {
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.nextQuestion() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0)),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text(
                            if (currentIndex < viewModel.totalQuestions - 1) "Следующий вопрос ➡️" else "Завершить 🏁",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmotionOptionButton(
    text: String,
    selectedAnswer: String?,
    correctAnswer: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val isSelected = selectedAnswer == text
    val isCorrect = text == correctAnswer
    val bgColor by animateColorAsState(
        when {
            selectedAnswer == null -> Color.White
            isCorrect -> Color(0xFF4CAF50)
            isSelected -> Color(0xFFE53935)
            else -> Color.White
        }
    )
    val textColor by animateColorAsState(
        when {
            selectedAnswer == null -> Color(0xFF37474F)
            isCorrect || isSelected -> Color.White
            else -> Color(0xFF37474F)
        }
    )
    val scale by animateFloatAsState(if (isSelected) 0.96f else 1f)

    Box(
        modifier = modifier
            .height(60.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(2.dp, bgColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clickable(enabled = selectedAnswer == null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun EmotionsCompletedScreen(score: Int, total: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🌟", fontSize = 80.sp)
        Spacer(Modifier.height(16.dp))
        Text("Игра завершена!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Ты отлично знаешь эмоции!", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .background(Color(0xFFFFF9C4), RoundedCornerShape(16.dp))
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text("⭐ $score из $total", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8F00))
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0)),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Играть снова 🔄", fontSize = 18.sp)
        }
    }
}
