package com.example.spectrplus.presentation.screen.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spectrplus.presentation.viewmodel.games.OddOneOutViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OddOneOutGameScreen(
    onBack: () -> Unit = {},
    viewModel: OddOneOutViewModel = hiltViewModel()
) {
    val roundIndex by viewModel.roundIndex.collectAsState()
    val wrongPick by viewModel.wrongPick.collectAsState()
    val completed by viewModel.completed.collectAsState()
    val round = viewModel.currentRound

    LaunchedEffect(wrongPick) {
        if (wrongPick != null) {
            delay(600)
            viewModel.clearWrong()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Что лишнее?", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8F5E9))
            )
        }
    ) { padding ->
        if (completed) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🎉", fontSize = 72.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    "Отлично!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Ты нашёл все лишние картинки",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF546E7A)
                )
                Spacer(Modifier.height(28.dp))
                Button(
                    onClick = { viewModel.restart() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                ) {
                    Text("Играть снова", fontSize = 17.sp)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF1F8E9))
                    .padding(16.dp)
            ) {
                Text(
                    "Раунд ${roundIndex + 1} из ${viewModel.totalRounds}",
                    fontSize = 13.sp,
                    color = Color(0xFF689F38),
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Нажми на картинку, которая не подходит к остальным",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF455A64)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    round.hint,
                    fontSize = 12.sp,
                    color = Color(0xFF78909C)
                )
                Spacer(Modifier.height(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(
                        round.options,
                        key = { idx, em -> "${roundIndex}_${idx}_$em" }
                    ) { index, emoji ->
                        val isWrong = wrongPick == index
                        val border by animateColorAsState(
                            if (isWrong) Color(0xFFE53935) else Color(0xFFE0E0E0),
                            label = "border"
                        )
                        val bg by animateColorAsState(
                            if (isWrong) Color(0xFFFFEBEE) else Color.White,
                            label = "bg"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(bg, RoundedCornerShape(20.dp))
                                .border(2.dp, border, RoundedCornerShape(20.dp))
                                .clickable { viewModel.onPick(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 52.sp)
                        }
                    }
                }
            }
        }
    }
}
