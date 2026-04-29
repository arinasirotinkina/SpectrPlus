package com.example.spectrplus.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrLabels
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.presentation.screen.education.VideoItem
import com.example.spectrplus.presentation.viewmodel.education.ArticlesViewModel
import com.example.spectrplus.presentation.viewmodel.education.VideoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    articlesViewModel: ArticlesViewModel = hiltViewModel(),
    videoViewModel: VideoViewModel = hiltViewModel()
) {
    var tab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        articlesViewModel.loadFavorites()
        videoViewModel.loadFavorites()
    }

    val articles = articlesViewModel.favorites
    val videos = videoViewModel.favorites

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Избранное",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("Статьи", "Видео").forEachIndexed { index, title ->
                    val selected = tab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) SpectrColors.Primary else Color.Transparent)
                            .clickable { tab = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            title,
                            fontSize = 14.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (selected) Color.White else SpectrColors.TextMuted
                        )
                    }
                }
            }

            when (tab) {
                0 -> {
                    if (articles.isEmpty()) {
                        EmptyState(
                            emoji = "🔖",
                            title = "В избранном пока пусто",
                            subtitle = "Отмечайте статьи, чтобы быстро возвращаться"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(articles, key = { it.id }) { article ->
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate("article_detail/${article.id}")
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
                                    elevation = CardDefaults.elevatedCardElevation(1.dp)
                                ) {
                                    Row(
                                        Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(SpectrColors.DangerSoft),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                painterResource(R.drawable.baseline_bookmark_24),
                                                null,
                                                tint = SpectrColors.Danger
                                            )
                                        }
                                        Spacer(Modifier.size(12.dp))
                                        Column(Modifier.weight(1f)) {
                                            Text(
                                                article.title,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = SpectrColors.Text,
                                                maxLines = 2
                                            )
                                            Text(
                                                SpectrLabels.article(article.category),
                                                fontSize = 12.sp,
                                                color = SpectrColors.TextMuted
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    if (videos.isEmpty()) {
                        EmptyState(
                            emoji = "🎬",
                            title = "Видео в избранном нет",
                            subtitle = "Пометьте видеоуроки, чтобы вернуться позже"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(videos, key = { it.id }) { video ->
                                VideoItem(
                                    video = video,
                                    onClick = { navController.navigate("video_player/${video.id}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
