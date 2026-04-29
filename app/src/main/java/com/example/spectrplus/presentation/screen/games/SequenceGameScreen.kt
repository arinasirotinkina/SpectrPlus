package com.example.spectrplus.presentation.screen.games

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.spectrplus.presentation.viemodel.games.SequenceStep
import com.example.spectrplus.presentation.viemodel.games.SequenceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequenceGameScreen(
    onBack: () -> Unit = {},
    viewModel: SequenceViewModel = hiltViewModel()
) {
    val shuffledSteps by viewModel.shuffledSteps.collectAsState()
    val selectedOrder by viewModel.selectedOrder.collectAsState()
    val result by viewModel.result.collectAsState()
    val score by viewModel.score.collectAsState()
    val levelIndex by viewModel.levelIndex.collectAsState()
    val level = viewModel.currentLevel

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📚 ${level.title}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    Text(
                        "⭐ $score",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp),
                        color = Color(0xFFFF8F00)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF0F4FF))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F7FB))
                .padding(16.dp)
        ) {
            Text(
                text = "Уровень ${levelIndex + 1} из ${viewModel.totalLevels}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = level.description,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(20.dp))

            if (result != null) {
                ResultBanner(isCorrect = result == true)
                Spacer(Modifier.height(16.dp))

                if (result == true) {
                    CorrectSequenceView(steps = level.steps.sortedBy { it.correctPosition })
                } else {
                    CorrectSequenceView(steps = level.steps.sortedBy { it.correctPosition })
                }
                Spacer(Modifier.height(24.dp))

                if (result == true && levelIndex < viewModel.totalLevels - 1) {
                    Button(
                        onClick = { viewModel.nextLevel() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("Следующий уровень ➡️", fontSize = 18.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                }
                OutlinedButton(
                    onClick = { viewModel.retryLevel() },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Попробовать снова 🔄", fontSize = 18.sp)
                }
            } else {
                Text(
                    text = "Нажимай на карточки в правильном порядке:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(Modifier.height(12.dp))

                SelectedOrderRow(
                    steps = shuffledSteps,
                    selectedOrder = selectedOrder
                )
                Spacer(Modifier.height(20.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(shuffledSteps) { step ->
                        val positionInOrder = selectedOrder.indexOf(step.id)
                        val isSelected = positionInOrder >= 0
                        SequenceStepCard(
                            step = step,
                            positionInOrder = if (isSelected) positionInOrder + 1 else null,
                            isSelected = isSelected,
                            onClick = { viewModel.toggleStep(step.id) }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.checkAnswer() },
                    enabled = selectedOrder.size == shuffledSteps.size,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0)),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Проверить ✅", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun SequenceStepCard(
    step: SequenceStep,
    positionInOrder: Int?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        if (isSelected) Color(0xFFE8EAF6) else Color.White
    )
    val borderColor by animateColorAsState(
        if (isSelected) Color(0xFF5C6BC0) else Color(0xFFE0E0E0)
    )
    val scale by animateFloatAsState(if (isSelected) 0.97f else 1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    if (positionInOrder != null) Color(0xFF5C6BC0) else Color(0xFFEEEEEE),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (positionInOrder != null) "$positionInOrder" else "?",
                color = if (positionInOrder != null) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(step.emoji, fontSize = 36.sp)
        Spacer(Modifier.width(12.dp))
        Text(step.label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SelectedOrderRow(steps: List<SequenceStep>, selectedOrder: List<Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(steps.size) { idx ->
            val stepId = selectedOrder.getOrNull(idx)
            val step = steps.find { it.id == stepId }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (step != null) Color(0xFFE8EAF6) else Color(0xFFF5F5F5),
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        if (step != null) Color(0xFF5C6BC0) else Color(0xFFE0E0E0),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (step != null) {
                    Text(step.emoji, fontSize = 28.sp)
                } else {
                    Text("${idx + 1}", color = Color.LightGray, fontSize = 20.sp)
                }
            }
            if (idx < steps.size - 1) {
                Text("→", color = Color.LightGray, fontSize = 18.sp)
            }
        }
    }
}

@Composable
private fun CorrectSequenceView(steps: List<SequenceStep>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text("Правильный порядок:", fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    Modifier
                        .size(32.dp)
                        .background(Color(0xFF4CAF50), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${index + 1}", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Text(step.emoji, fontSize = 24.sp)
                Spacer(Modifier.width(8.dp))
                Text(step.label, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun ResultBanner(isCorrect: Boolean) {
    val color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935)
    val text = if (isCorrect) "🎉 Правильно! Молодец!" else "❌ Не совсем верно"

    AnimatedVisibility(visible = true, enter = fadeIn() + scaleIn()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                .border(2.dp, color, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color, textAlign = TextAlign.Center)
        }
    }
}
