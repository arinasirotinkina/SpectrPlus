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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spectrplus.presentation.viemodel.games.ShadowItem
import com.example.spectrplus.presentation.viemodel.games.ShadowsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShadowsGameScreen(
    onBack: () -> Unit = {},
    viewModel: ShadowsViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val shadows by viewModel.shadows.collectAsState()
    val selectedItemId by viewModel.selectedItemId.collectAsState()
    val matched by viewModel.matched.collectAsState()
    val wrongPair by viewModel.wrongPair.collectAsState()
    val completed by viewModel.completed.collectAsState()

    LaunchedEffect(wrongPair) {
        if (wrongPair != null) {
            delay(700)
            viewModel.clearWrongPair()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🌑 Тени", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFECEFF1))
            )
        }
    ) { padding ->
        if (completed) {
            ShadowsCompletedScreen(onRestart = { viewModel.restart() })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
                    .padding(16.dp)
            ) {
                LinearProgressIndicator(
                    progress = { matched.size.toFloat() / items.size },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF546E7A)
                )
                Spacer(Modifier.height(4.dp))
                Text("Найдено пар: ${matched.size} из ${items.size}",
                    style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(12.dp))

                if (selectedItemId != null) {
                    val sel = items.find { it.id == selectedItemId }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${sel?.emoji ?: ""} Выбрано: «${sel?.name ?: ""}» — нажми на тень!",
                            fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.height(8.dp))
                } else {
                    Text("Нажми на животное, потом найди его тень ниже",
                        style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(Modifier.height(8.dp))
                }

                Text("Животные:", fontWeight = FontWeight.Bold, color = Color(0xFF455A64))
                Spacer(Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(items) { item ->
                        val isMatched = matched.contains(item.id)
                        val isSelected = selectedItemId == item.id
                        val isWrong = wrongPair?.first == item.id
                        AnimalItemCell(
                            item = item,
                            isMatched = isMatched,
                            isSelected = isSelected,
                            isWrong = isWrong,
                            isShadow = false,
                            onClick = { if (!isMatched) viewModel.selectItem(item.id) }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text("Тени:", fontWeight = FontWeight.Bold, color = Color(0xFF455A64))
                Spacer(Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(shadows) { shadow ->
                        val isMatched = matched.contains(shadow.id)
                        val isWrong = wrongPair?.second == shadow.id
                        AnimalItemCell(
                            item = shadow,
                            isMatched = isMatched,
                            isSelected = false,
                            isWrong = isWrong,
                            isShadow = true,
                            onClick = { if (!isMatched && selectedItemId != null) viewModel.matchShadow(shadow.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimalItemCell(
    item: ShadowItem,
    isMatched: Boolean,
    isSelected: Boolean,
    isWrong: Boolean,
    isShadow: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        when {
            isMatched -> Color(0xFFE8F5E9)
            isWrong -> Color(0xFFFFEBEE)
            isSelected -> Color(0xFFE3F2FD)
            isShadow && !isMatched -> Color(0xFFECEFF1)
            isShadow -> Color(0xFFE8F5E9)
            else -> Color.White
        }
    )
    val borderColor by animateColorAsState(
        when {
            isMatched -> Color(0xFF4CAF50)
            isWrong -> Color(0xFFE53935)
            isSelected -> Color(0xFF2196F3)
            else -> Color(0xFFE0E0E0)
        }
    )
    val scale by animateFloatAsState(if (isSelected) 1.1f else 1f)

    Column(
        modifier = Modifier
            .size(72.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(14.dp))
            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isShadow && !isMatched) {
            Text(
                item.emoji,
                fontSize = 28.sp,
                modifier = Modifier.drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color(0xFF78909C),
                        blendMode = BlendMode.Saturation
                    )
                }
            )
        } else {
            Text(item.emoji, fontSize = 28.sp)
            if (!isShadow || isMatched) {
                Text(
                    item.name,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    color = if (isShadow && isMatched) Color(0xFF2E7D32) else Color.Gray
                )
            }
        }
        if (isMatched) Text("✓", fontSize = 10.sp, color = Color(0xFF4CAF50))
    }
}

@Composable
private fun ShadowsCompletedScreen(onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🌑✨", fontSize = 72.sp)
        Spacer(Modifier.height(16.dp))
        Text("Все тени найдены!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF546E7A))
        Spacer(Modifier.height(8.dp))
        Text("Ты отлично различаешь формы!", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF546E7A)),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Играть снова 🔄", fontSize = 18.sp)
        }
    }
}
