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
import com.example.spectrplus.presentation.viewmodel.games.PartOption
import com.example.spectrplus.presentation.viewmodel.games.PartsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartsGameScreen(
    onBack: () -> Unit = {},
    viewModel: PartsViewModel = hiltViewModel()
) {
    val puzzleIndex by viewModel.puzzleIndex.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val isCorrect by viewModel.isCorrect.collectAsState()
    val score by viewModel.score.collectAsState()
    val completed by viewModel.completed.collectAsState()
    val puzzle = viewModel.currentPuzzle

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🧠 Части и целое", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                },
                actions = {
                    Text("⭐ $score", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp), color = Color(0xFFFF8F00))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8EAF6))
            )
        }
    ) { padding ->
        if (completed) {
            PartsCompletedScreen(score, viewModel.totalPuzzles, onRestart = { viewModel.restart() })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF3F4FF))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { (puzzleIndex + 1).toFloat() / viewModel.totalPuzzles },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF5C6BC0)
                )
                Spacer(Modifier.height(4.dp))
                Text("${puzzleIndex + 1} из ${viewModel.totalPuzzles}",
                    style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(3.dp, Color(0xFF5C6BC0), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    val displayEmoji = if (isCorrect == true) puzzle.completeEmoji else puzzle.incompleteEmoji
                    Text(displayEmoji, fontSize = 64.sp, textAlign = TextAlign.Center)
                }
                Spacer(Modifier.height(20.dp))

                Text(
                    puzzle.question,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    puzzle.objectName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(Modifier.height(32.dp))

                if (isCorrect != null) {
                    val color = if (isCorrect == true) Color(0xFF4CAF50) else Color(0xFFE53935)
                    val text = if (isCorrect == true) "🎉 Правильно!" else "❌ Попробуй ещё раз"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color.copy(0.15f), RoundedCornerShape(16.dp))
                            .border(2.dp, color, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
                    }
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.next() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCorrect == true) Color(0xFF4CAF50) else Color(0xFF5C6BC0)
                        ),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text(if (puzzleIndex < viewModel.totalPuzzles - 1) "Далее ➡️" else "Завершить 🏁",
                            fontSize = 18.sp)
                    }
                } else {
                    Text("Выбери нужную часть:", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        puzzle.options.forEach { option ->
                            PartOptionCard(
                                option = option,
                                selectedOption = selectedOption,
                                correctId = puzzle.correctOptionId,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.selectOption(option.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PartOptionCard(
    option: PartOption,
    selectedOption: String?,
    correctId: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val isSelected = selectedOption == option.id
    val isCorrect = option.id == correctId
    val bgColor by animateColorAsState(
        when {
            selectedOption == null -> Color.White
            isCorrect -> Color(0xFF4CAF50)
            isSelected -> Color(0xFFE53935)
            else -> Color.White
        }
    )
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f)

    Column(
        modifier = modifier
            .height(110.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(2.dp, bgColor.copy(0.5f), RoundedCornerShape(16.dp))
            .clickable(enabled = selectedOption == null) { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(option.emoji, fontSize = 40.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            option.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = if (selectedOption != null && (isCorrect || isSelected)) Color.White else Color(0xFF37474F)
        )
    }
}

@Composable
private fun PartsCompletedScreen(score: Int, total: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🧩✨", fontSize = 72.sp)
        Spacer(Modifier.height(16.dp))
        Text("Всё собрано!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5C6BC0))
        Spacer(Modifier.height(8.dp))
        Text("Ты умеешь находить недостающие части!", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .background(Color(0xFFE8EAF6), RoundedCornerShape(16.dp))
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
