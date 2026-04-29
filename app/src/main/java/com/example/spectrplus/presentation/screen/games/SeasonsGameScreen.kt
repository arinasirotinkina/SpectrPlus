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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spectrplus.domain.model.games.Basket
import com.example.spectrplus.domain.model.games.SortItem
import com.example.spectrplus.presentation.viewmodel.games.SeasonsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonsGameScreen(
    onBack: () -> Unit = {},
    viewModel: SeasonsViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val score by viewModel.score.collectAsState()
    val completed by viewModel.completed.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()
    val wrongBasket by viewModel.wrongBasket.collectAsState()

    LaunchedEffect(wrongBasket) {
        if (wrongBasket != null) {
            delay(600)
            viewModel.clearWrongBasket()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🌦️ Времена года", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                },
                actions = {
                    Text("⭐ $score", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp), color = Color(0xFFFF8F00))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF1F8E9))
            )
        }
    ) { padding ->
        if (completed) {
            SeasonsCompletedScreen(score = score, onRestart = { viewModel.restart() })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF9FFF2))
                    .padding(16.dp)
            ) {
                if (selectedItem != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(selectedItem!!.bgColor), RoundedCornerShape(12.dp))
                            .border(2.dp, Color(0xFF558B2F), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(selectedItem!!.emoji, fontSize = 32.sp)
                            Spacer(Modifier.size(8.dp))
                            Text(
                                "«${selectedItem!!.name}» — выбрано! Нажми на корзину",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                } else {
                    Text(
                        "Выбери предмет, затем нажми на нужное время года",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.baskets.forEach { basket ->
                        SeasonBasketCard(
                            basket = basket,
                            isWrong = wrongBasket == basket.id,
                            hasSelection = selectedItem != null,
                            onClick = { viewModel.placeInBasket(basket) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text("Предметы:", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                Spacer(Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(items) { item ->
                        SeasonItemCard(
                            item = item,
                            isSelected = selectedItem?.id == item.id,
                            onClick = { viewModel.selectItem(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeasonBasketCard(
    basket: Basket,
    isWrong: Boolean,
    hasSelection: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val seasonColors = mapOf(
        "spring" to Color(0xFFFCE4EC),
        "summer" to Color(0xFFFFECB3),
        "autumn" to Color(0xFFFFE0B2),
        "winter" to Color(0xFFBBDEFB)
    )
    val bgColor by animateColorAsState(
        when {
            isWrong -> Color(0xFFFFEBEE)
            else -> seasonColors[basket.id] ?: Color(0xFFEEEEEE)
        }
    )
    val borderColor by animateColorAsState(
        when {
            isWrong -> Color(0xFFE53935)
            hasSelection -> Color(0xFF558B2F)
            else -> Color.Transparent
        }
    )
    val scale by animateFloatAsState(if (hasSelection) 1.03f else 1f)

    Column(
        modifier = modifier
            .height(100.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { if (hasSelection) onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(basket.title.take(2), fontSize = 30.sp)
        Text(
            basket.title.drop(2).trim(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SeasonItemCard(
    item: SortItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        if (isSelected) Color(item.bgColor) else Color.White
    )
    val borderColor by animateColorAsState(
        if (isSelected) Color(0xFF558B2F) else Color(0xFFE0E0E0)
    )
    val scale by animateFloatAsState(if (isSelected) 1.1f else 1f)

    Column(
        modifier = Modifier
            .size(80.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(bgColor, RoundedCornerShape(14.dp))
            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(item.emoji, fontSize = 28.sp)
        Text(item.name, fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 1)
    }
}

@Composable
private fun SeasonsCompletedScreen(score: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🌸☀️🍂⛄", fontSize = 52.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text("Отлично!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF558B2F))
        Spacer(Modifier.height(8.dp))
        Text("Ты знаешь все времена года!", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .background(Color(0xFFF1F8E9), RoundedCornerShape(16.dp))
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text("⭐ $score очков", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8F00))
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF558B2F)),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Играть снова 🔄", fontSize = 18.sp)
        }
    }
}
