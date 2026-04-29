package com.example.spectrplus.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.domain.model.games.GameItem

private data class GameStyle(
    val gradient: Brush,
    val background: Color,
    val accent: Color
)

private val gameStyles = listOf(
    GameStyle(
        gradient = Brush.linearGradient(listOf(Color(0xFF7986CB), Color(0xFF5B6DFF))),
        background = Color(0xFFE8EBFF),
        accent = Color(0xFF3D4CCC)
    ),
    GameStyle(
        gradient = Brush.linearGradient(listOf(Color(0xFFFFB74D), Color(0xFFFF8A65))),
        background = Color(0xFFFFF3E0),
        accent = Color(0xFFE65100)
    ),
    GameStyle(
        gradient = Brush.linearGradient(listOf(Color(0xFF4DB6AC), Color(0xFF26A69A))),
        background = Color(0xFFE0F2F1),
        accent = Color(0xFF00796B)
    ),
    GameStyle(
        gradient = Brush.linearGradient(listOf(Color(0xFFBA68C8), Color(0xFF9575CD))),
        background = Color(0xFFF3E5F5),
        accent = Color(0xFF6A1B9A)
    ),
    GameStyle(
        gradient = Brush.linearGradient(listOf(Color(0xFFF06292), Color(0xFFE57373))),
        background = Color(0xFFFFEBEE),
        accent = Color(0xFFC62828)
    ),
    GameStyle(
        gradient = Brush.linearGradient(listOf(Color(0xFF4FC3F7), Color(0xFF29B6F6))),
        background = Color(0xFFE1F5FE),
        accent = Color(0xFF0277BD)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    onGameClick: (String) -> Unit
) {
    val games = listOf(
        GameItem("sorting", "Сортировка", "Логика и классификация", "🧩"),
        GameItem("sequence", "Последовательность", "Бытовые сценарии", "📚"),
        GameItem("emotions", "Эмоции", "Распознавание эмоций", "😊"),
        GameItem("odd_one_out", "Что лишнее?", "Найди лишний предмет", "🎯"),
        GameItem("animals", "Звуки животных", "Слуховое восприятие", "🐶"),
        //GameItem("differences", "Найди отличия", "Внимание и концентрация", "🔍"),
        GameItem("seasons", "Времена года", "Сезонные знания", "🌦️"),
        GameItem("parts", "Части и целое", "Логика конструкций", "🧠"),
        //GameItem("shadows", "Тени", "Формы и восприятие", "🌑"),
        GameItem("logic", "Что нужно для…?", "Ассоциации", "🧺")
    )

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = { SpectrTopBar(title = "Игры") }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GamesHeroCard()

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(games) { game ->
                    val style = gameStyles[
                        game.id.hashCode().let { if (it < 0) -it else it } % gameStyles.size
                    ]
                    GameTile(game = game, style = style) { onGameClick(game.id) }
                }
            }
        }
    }
}

@Composable
private fun GamesHeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(SpectrColors.HeaderGradient)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🎮", fontSize = 42.sp)
            Spacer(Modifier.size(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "Развивающие игры",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Выберите занятие для ребенка — каждое развивает свой навык",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun GameTile(
    game: GameItem,
    style: GameStyle,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.92f)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .background(style.gradient),
                contentAlignment = Alignment.Center
            ) {
                Text(game.icon, fontSize = 46.sp)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = game.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SpectrColors.Text,
                    maxLines = 1
                )
                Text(
                    text = game.description,
                    fontSize = 11.sp,
                    color = SpectrColors.TextMuted,
                    maxLines = 2
                )
            }
        }
    }
}
