package com.example.spectrplus.presentation.screen.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spectrplus.domain.model.games.Basket
import com.example.spectrplus.domain.model.games.SortItem
import com.example.spectrplus.presentation.viewmodel.games.DragState
import com.example.spectrplus.presentation.viewmodel.games.SortingViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingGameScreen(
    onBack: () -> Unit = {},
    viewModel: SortingViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val baskets by viewModel.baskets.collectAsState()
    val score by viewModel.score.collectAsState()
    val completed by viewModel.completed.collectAsState()
    val wrongAttempt by viewModel.wrongAttempt.collectAsState()
    val currentLevel by viewModel.currentLevel.collectAsState()

    var dragState by remember { mutableStateOf(DragState()) }
    val basketRects = remember { mutableMapOf<String, Rect>() }

    LaunchedEffect(wrongAttempt) {
        if (wrongAttempt != null) {
            delay(500)
            viewModel.clearWrongAttempt()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🧩 Сортировка", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    Text(
                        text = "⭐ $score",
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
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F7FB))
        ) {
            if (completed) {
                SortingCompletionScreen(
                    score = score,
                    level = currentLevel,
                    totalLevels = viewModel.totalLevels,
                    onNext = { viewModel.nextLevel() },
                    onRestart = { viewModel.restart() }
                )
            } else {
                Column(Modifier.fillMaxSize().padding(16.dp)) {
                    Text(
                        text = "Уровень ${currentLevel + 1} из ${viewModel.totalLevels}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Перетащи предметы в правильные корзины",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        baskets.forEach { basket ->
                            SortBasketView(basket) { basketRects[basket.id] = it }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    Text(
                        "Предметы для сортировки:",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(items) { item ->
                            val isWrong = wrongAttempt == item.id
                            if (dragState.item?.id != item.id) {
                                SortItemCard(
                                    item = item,
                                    isWrong = isWrong,
                                    onDragStart = { global, touch ->
                                        dragState = DragState(
                                            item = item,
                                            position = global - touch,
                                            touchOffset = touch
                                        )
                                    }
                                )
                            } else {
                                Box(
                                    Modifier
                                        .size(90.dp)
                                        .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                                )
                            }
                        }
                    }
                }
            }

            dragState.item?.let { item ->
                DraggingItem(
                    item = item,
                    offset = dragState.position,
                    onDrag = { delta ->
                        dragState = dragState.copy(position = dragState.position + delta)
                    },
                    onDragEnd = {
                        val hit = basketRects.entries.firstOrNull { (_, rect) ->
                            rect.contains(dragState.position)
                        }
                        if (hit != null) {
                            val basket = baskets.first { it.id == hit.key }
                            if (basket.accepts == item.category) {
                                viewModel.removeItem(item)
                            } else {
                                viewModel.onWrongDrop(item.id)
                            }
                        }
                        dragState = DragState()
                    }
                )
            }
        }
    }
}

@Composable
private fun SortBasketView(basket: Basket, onPositioned: (Rect) -> Unit) {
    val basketColors = mapOf(
        "food" to Color(0xFFFFE0B2),
        "transport" to Color(0xFFBBDEFB),
        "animals" to Color(0xFFF8BBD9),
        "plants" to Color(0xFFC8E6C9),
        "sport" to Color(0xFFB2EBF2),
        "learning" to Color(0xFFFFF9C4)
    )
    val color = basketColors[basket.id] ?: Color(0xFFE9EEFF)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.onGloballyPositioned { onPositioned(it.boundsInRoot()) }
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(color, RoundedCornerShape(20.dp))
                .border(2.dp, color.copy(alpha = 0.7f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(basket.title, fontSize = 20.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun SortItemCard(
    item: SortItem,
    isWrong: Boolean,
    onDragStart: (global: Offset, touch: Offset) -> Unit
) {
    var coords by remember { mutableStateOf<androidx.compose.ui.layout.LayoutCoordinates?>(null) }
    val shakeAnim by animateFloatAsState(if (isWrong) 10f else 0f)
    val borderColor by animateColorAsState(if (isWrong) Color.Red else Color.Transparent)

    Box(
        modifier = Modifier
            .size(90.dp)
            .offset(x = if (isWrong) shakeAnim.dp else 0.dp)
            .background(Color(item.bgColor), RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .onGloballyPositioned { coords = it }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { touchOffset ->
                        val global = coords?.localToRoot(touchOffset) ?: Offset.Zero
                        onDragStart(global, touchOffset)
                    },
                    onDrag = { _, _ -> }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(item.emoji, fontSize = 32.sp)
            Text(item.name, fontSize = 11.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun DraggingItem(
    item: SortItem,
    offset: Offset,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
            .graphicsLayer { scaleX = 1.15f; scaleY = 1.15f; shadowElevation = 24f }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount -> change.consume(); onDrag(dragAmount) },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() }
                )
            }
            .size(90.dp)
            .background(Color(item.bgColor), RoundedCornerShape(16.dp))
            .border(3.dp, Color(0xFF5C6BC0), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(item.emoji, fontSize = 32.sp)
            Text(item.name, fontSize = 11.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun SortingCompletionScreen(
    score: Int,
    level: Int,
    totalLevels: Int,
    onNext: () -> Unit,
    onRestart: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 80.sp)
        Spacer(Modifier.height(16.dp))
        Text("Отлично!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
        Spacer(Modifier.height(8.dp))
        Text("Ты правильно рассортировал всё!", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .background(Color(0xFFFFF9C4), RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text("⭐ $score очков", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8F00))
        }
        Spacer(Modifier.height(32.dp))
        if (level < totalLevels - 1) {
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0)),
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
