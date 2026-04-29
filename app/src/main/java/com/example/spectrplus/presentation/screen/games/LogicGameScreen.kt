package com.example.spectrplus.presentation.screen.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.spectrplus.presentation.viewmodel.games.LogicItem
import com.example.spectrplus.presentation.viewmodel.games.LogicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogicGameScreen(
    onBack: () -> Unit = {},
    viewModel: LogicViewModel = hiltViewModel()
) {
    val situationIndex by viewModel.situationIndex.collectAsState()
    val selectedItems by viewModel.selectedItems.collectAsState()
    val checked by viewModel.checked.collectAsState()
    val score by viewModel.score.collectAsState()
    val completed by viewModel.completed.collectAsState()
    val situation = viewModel.currentSituation

    val isAllCorrect = checked && selectedItems == situation.neededIds

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🧺 Что нужно для…?", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                },
                actions = {
                    Text("⭐ $score", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp), color = Color(0xFFFF8F00))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFF8E1))
            )
        }
    ) { padding ->
        if (completed) {
            LogicCompletedScreen(score, viewModel.totalSituations, onRestart = { viewModel.restart() })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFFFFDF0))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { (situationIndex + 1).toFloat() / viewModel.totalSituations },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFFFFB300)
                )
                Spacer(Modifier.height(4.dp))
                Text("${situationIndex + 1} из ${viewModel.totalSituations}",
                    style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color(0xFFFFF9C4), RoundedCornerShape(24.dp))
                        .border(3.dp, Color(0xFFFFB300), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(situation.emoji, fontSize = 60.sp)
                }
                Spacer(Modifier.height(12.dp))

                Text(
                    situation.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    situation.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF37474F)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Нужно выбрать ${situation.neededIds.size} предмета",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(Modifier.height(16.dp))

                if (checked) {
                    val color = if (isAllCorrect) Color(0xFF4CAF50) else Color(0xFFFF8F00)
                    val text = if (isAllCorrect) "🎉 Правильно! Всё нужное выбрано!" else "💡 Посмотри: нужные предметы отмечены зелёным"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color.copy(0.12f), RoundedCornerShape(12.dp))
                            .border(2.dp, color, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text, color = color, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(situation.allItems) { item ->
                        LogicItemCard(
                            item = item,
                            isSelected = selectedItems.contains(item.id),
                            isNeeded = situation.neededIds.contains(item.id),
                            checked = checked,
                            onClick = { viewModel.toggleItem(item.id) }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (!checked) {
                    Button(
                        onClick = { viewModel.check() },
                        enabled = selectedItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("Проверить ✅", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { viewModel.next() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0)),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text(
                            if (situationIndex < viewModel.totalSituations - 1) "Следующее задание ➡️" else "Завершить 🏁",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LogicItemCard(
    item: LogicItem,
    isSelected: Boolean,
    isNeeded: Boolean,
    checked: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        when {
            checked && isNeeded -> Color(0xFFE8F5E9)
            checked && isSelected && !isNeeded -> Color(0xFFFFEBEE)
            isSelected -> Color(0xFFFFF9C4)
            else -> Color.White
        }
    )
    val borderColor by animateColorAsState(
        when {
            checked && isNeeded -> Color(0xFF4CAF50)
            checked && isSelected && !isNeeded -> Color(0xFFE53935)
            isSelected -> Color(0xFFFFB300)
            else -> Color(0xFFE0E0E0)
        }
    )
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f)

    Column(
        modifier = Modifier
            .size(95.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = !checked) { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(item.emoji, fontSize = 36.sp)
        Spacer(Modifier.height(4.dp))
        Text(item.name, fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 2,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        if (checked && isNeeded) Text("✓", fontSize = 12.sp, color = Color(0xFF4CAF50))
    }
}

@Composable
private fun LogicCompletedScreen(score: Int, total: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🧺🌟", fontSize = 72.sp)
        Spacer(Modifier.height(16.dp))
        Text("Молодец!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFB300))
        Spacer(Modifier.height(8.dp))
        Text("Ты знаешь, что нужно в разных ситуациях!", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Играть снова 🔄", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
