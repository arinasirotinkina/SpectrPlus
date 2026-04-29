package com.example.spectrplus.presentation.screen.games

import android.speech.tts.TextToSpeech
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spectrplus.presentation.viewmodel.games.AnimalData
import com.example.spectrplus.presentation.viewmodel.games.AnimalsViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalsGameScreen(
    onBack: () -> Unit = {},
    viewModel: AnimalsViewModel = hiltViewModel()
) {
    val activeAnimalId by viewModel.activeAnimalId.collectAsState()
    val context = LocalContext.current

    val tts = remember {
        TextToSpeech(context) { }
    }

    DisposableEffect(Unit) {
        tts.language = Locale("ru", "RU")
        onDispose { tts.shutdown() }
    }

    val activeAnimal = activeAnimalId?.let { viewModel.getAnimal(it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🐶 Звуки животных", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8F5E9))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF1FFF5))
                .padding(16.dp)
        ) {
            Text(
                "Нажми на животное, чтобы услышать его звук!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            if (activeAnimal != null) {
                ActiveAnimalBanner(activeAnimal)
                Spacer(Modifier.height(12.dp))
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.animals) { animal ->
                    AnimalCard(
                        animal = animal,
                        isActive = animal.id == activeAnimalId,
                        onClick = {
                            viewModel.onAnimalTapped(animal.id)
                            tts.speak(animal.speechText, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ActiveAnimalBanner(animal: AnimalData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(animal.bgColor), RoundedCornerShape(20.dp))
            .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(20.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(animal.emoji, fontSize = 48.sp)
            Spacer(Modifier.height(4.dp))
            Text(animal.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(
                animal.sound,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2E7D32)
            )
        }
    }
}

@Composable
private fun AnimalCard(
    animal: AnimalData,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        if (isActive) Color(animal.bgColor) else Color.White
    )
    val scale by animateFloatAsState(if (isActive) 1.05f else 1f)
    val borderColor by animateColorAsState(
        if (isActive) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
    )

    Card(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
        elevation = CardDefaults.cardElevation(if (isActive) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(animal.emoji, fontSize = 38.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                animal.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            if (isActive) {
                Text(
                    animal.sound,
                    fontSize = 10.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
