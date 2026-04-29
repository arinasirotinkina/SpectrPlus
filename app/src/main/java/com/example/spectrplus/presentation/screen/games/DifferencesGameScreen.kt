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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.spectrplus.presentation.viemodel.games.DifferencesViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifferencesGameScreen(
    onBack: () -> Unit = {},
    viewModel: DifferencesViewModel = hiltViewModel()
) {
    val levelIndex by viewModel.levelIndex.collectAsState()
    val foundDifferences by viewModel.foundDifferences.collectAsState()
    val wrongTaps by viewModel.wrongTaps.collectAsState()
    val completed by viewModel.completed.collectAsState()
    val level = viewModel.currentLevel

    LaunchedEffect(wrongTaps) {
        if (wrongTaps.isNotEmpty()) {
            delay(600)
            wrongTaps.forEach { viewModel.clearWrongTap(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🔍 Найди отличия", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFEDE7F6))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F5FF))
                .padding(16.dp)
        ) {
            if (completed) {
                DifferencesCompleted(
                    levelIndex = levelIndex,
                    totalLevels = viewModel.totalLevels,
                    onNext = { viewModel.nextLevel() },
                    onRestart = { viewModel.restart() }
                )
            } else {
                Text(
                    "Уровень ${levelIndex + 1} — ${level.title}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))

                val total = level.differenceIndices.size
                LinearProgressIndicator(
                    progress = { foundDifferences.size.toFloat() / total },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF7E57C2)
                )
                Text(
                    "Найдено: ${foundDifferences.size} из $total отличий",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Найди все отличия между картинками!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PicturePanel(
                        title = "Картинка 1",
                        items = level.leftItems,
                        foundDifferences = foundDifferences,
                        wrongTaps = wrongTaps,
                        differenceIndices = level.differenceIndices,
                        accentColor = Color(0xFF5C6BC0),
                        onTap = { viewModel.tapDifference(it) },
                        modifier = Modifier.weight(1f)
                    )
                    PicturePanel(
                        title = "Картинка 2",
                        items = level.rightItems,
                        foundDifferences = foundDifferences,
                        wrongTaps = wrongTaps,
                        differenceIndices = level.differenceIndices,
                        accentColor = Color(0xFF7E57C2),
                        onTap = { viewModel.tapDifference(it) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PicturePanel(
    title: String,
    items: List<String>,
    foundDifferences: Set<Int>,
    wrongTaps: Set<Int>,
    differenceIndices: Set<Int>,
    accentColor: Color,
    onTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(2.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Text(
            title,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(4.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(items) { index, emoji ->
                val isFound = foundDifferences.contains(index) && differenceIndices.contains(index)
                val isWrong = wrongTaps.contains(index)
                DifferenceCellItem(
                    emoji = emoji,
                    isFound = isFound,
                    isWrong = isWrong,
                    onClick = { onTap(index) }
                )
            }
        }
    }
}

@Composable
private fun DifferenceCellItem(
    emoji: String,
    isFound: Boolean,
    isWrong: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        when {
            isFound -> Color(0xFFE8F5E9)
            isWrong -> Color(0xFFFFEBEE)
            else -> Color(0xFFF5F5F5)
        }
    )
    val borderColor by animateColorAsState(
        when {
            isFound -> Color(0xFF4CAF50)
            isWrong -> Color(0xFFE53935)
            else -> Color.Transparent
        }
    )
    val scale by animateFloatAsState(if (isFound) 1.05f else 1f)

    Box(
        modifier = Modifier
            .size(44.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 20.sp)
            if (isFound) Text("✓", fontSize = 8.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DifferencesCompleted(
    levelIndex: Int,
    totalLevels: Int,
    onNext: () -> Unit,
    onRestart: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🔍✨", fontSize = 72.sp)
        Spacer(Modifier.height(16.dp))
        Text("Все отличия найдены!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7E57C2))
        Spacer(Modifier.height(8.dp))
        Text("Ты очень внимательный!", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        if (levelIndex < totalLevels - 1) {
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Следующий уровень ➡️", fontSize = 18.sp)
            }
            Spacer(Modifier.height(12.dp))
        }
        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78909C)),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Начать сначала 🔄", fontSize = 18.sp)
        }
    }
}
