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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spectrplus.presentation.viemodel.games.ColorRegion
import com.example.spectrplus.presentation.viemodel.games.ColoringViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColoringGameScreen(
    onBack: () -> Unit = {},
    viewModel: ColoringViewModel = hiltViewModel()
) {
    val regionColors by viewModel.colors.collectAsState()
    val selectedColor by viewModel.selectedColor.collectAsState()
    val pictureIndex by viewModel.pictureIndex.collectAsState()
    val picture = viewModel.currentPicture

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎨 Раскраска", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFCE4EC))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFFF8F9))
                .padding(16.dp)
        ) {
            Text(
                text = picture.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Выбери цвет и раскрась картинку",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(picture.regions) { region ->
                    ColoringRegionCell(
                        region = region,
                        fillColor = regionColors[region.id],
                        onClick = { viewModel.paintRegion(region.id) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            ColorPaletteRow(
                palette = viewModel.palette,
                selectedColor = selectedColor,
                onColorSelected = { viewModel.selectColor(it) }
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = { viewModel.resetAll() },
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("Очистить 🗑️")
                }
                Button(
                    onClick = { viewModel.nextPicture() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0)),
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("Другая картинка 🖼️")
                }
            }
        }
    }
}

@Composable
private fun ColoringRegionCell(
    region: ColorRegion,
    fillColor: Color?,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(fillColor ?: Color.White)
    val scale by animateFloatAsState(if (fillColor != null) 0.97f else 1f)
    val textColor = if ((fillColor?.luminance() ?: 1f) > 0.5f) Color(0xFF37474F) else Color.White

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .background(bgColor, RoundedCornerShape(12.dp))
                .border(2.dp, Color(0xFFDDDDDD), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(region.emoji, fontSize = 28.sp)
        }
        Text(
            text = region.label,
            fontSize = 9.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            maxLines = 1
        )
    }
}

@Composable
private fun ColorPaletteRow(
    palette: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    Column {
        Text("Выбери цвет:", fontWeight = FontWeight.Medium, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            palette.forEach { color ->
                val isSelected = color == selectedColor
                val scale by animateFloatAsState(if (isSelected) 1.25f else 1f)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                        .background(color, CircleShape)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) Color(0xFF37474F) else Color(0xFFBBBBBB),
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(color) }
                )
            }
        }
    }
}
