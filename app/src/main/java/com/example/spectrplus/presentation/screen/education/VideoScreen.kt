package com.example.spectrplus.presentation.screen.education

import android.net.Uri
import androidx.annotation.OptIn
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.CategoryChip
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.LoadingBlock
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrLabels
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.domain.model.education.Video
import com.example.spectrplus.presentation.viemodel.education.VideoPlayerFactory
import com.example.spectrplus.presentation.viemodel.education.VideoViewModel

@Composable
fun VideosSection(
    navController: NavController,
    viewModel: VideoViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedDuration by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) { viewModel.load() }

    Column(Modifier.fillMaxSize()) {

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            item {
                CategoryChip(
                    label = "Все",
                    selected = selectedCategory == null,
                    onClick = {
                        selectedCategory = null
                        viewModel.load(null, selectedDuration)
                    }
                )
            }
            items(SpectrLabels.videoCategories.entries.toList()) { e ->
                CategoryChip(
                    label = e.value,
                    selected = selectedCategory == e.key,
                    onClick = {
                        selectedCategory = e.key
                        viewModel.load(e.key, selectedDuration)
                    }
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            item {
                CategoryChip(
                    label = "Любая длина",
                    selected = selectedDuration == null,
                    onClick = {
                        selectedDuration = null
                        viewModel.load(selectedCategory, null)
                    }
                )
            }
            item {
                CategoryChip(
                    label = "< 5 мин",
                    selected = selectedDuration == 300,
                    onClick = {
                        selectedDuration = 300
                        viewModel.load(selectedCategory, 300)
                    }
                )
            }
            item {
                CategoryChip(
                    label = "< 10 мин",
                    selected = selectedDuration == 600,
                    onClick = {
                        selectedDuration = 600
                        viewModel.load(selectedCategory, 600)
                    }
                )
            }
            item {
                CategoryChip(
                    label = "< 20 мин",
                    selected = selectedDuration == 1200,
                    onClick = {
                        selectedDuration = 1200
                        viewModel.load(selectedCategory, 1200)
                    }
                )
            }

        }

        when {
            state.isLoading -> LoadingBlock()
            state.videos.isEmpty() -> EmptyState(
                emoji = "🎬",
                title = "Видеоуроков не найдено",
                subtitle = "Попробуйте другую категорию или длину"
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.videos) { video ->
                    VideoItem(
                        video = video,
                        onClick = { navController.navigate("video_player/${video.id}") },
                        onFavorite = { viewModel.toggleFavorite(video.id) }
                    )
                }
            }
        }
    }
}


@Composable
fun VideoItem(
    video: Video,
    onClick: () -> Unit,
    onFavorite: (() -> Unit)? = null
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = video.thumbnailUrl.ifBlank {
                        "https://i.pinimg.com/736x/7d/be/d9/7dbed90655c6d7de0f4d01eb01b9cbe1.jpg"
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(SpectrColors.PrimarySoft)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f))
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = SpectrColors.Primary,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_schedule_24),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        "${video.durationSeconds / 60} мин",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        SpectrLabels.video(video.category),
                        color = SpectrColors.Primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (onFavorite != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                            .clickable { onFavorite() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource(if (video.isFavorite) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24),
                            contentDescription = null,
                            tint = SpectrColors.Danger,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Column(Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        video.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SpectrColors.Text,
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                    if (video.isWatched) {
                        Spacer(Modifier.size(6.dp))
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Просмотрено",
                            tint = SpectrColors.Success,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                if (video.description.isNotBlank()) {
                    Spacer(Modifier.size(4.dp))
                    Text(
                        video.description,
                        fontSize = 12.sp,
                        color = SpectrColors.TextMuted,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun VideoPlayerScreen(
    video: Video,
    onToggleFavorite: (() -> Unit)? = null,
    onWatched: (() -> Unit)? = null
) {
    LaunchedEffect(video.id) { onWatched?.invoke() }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Видеоурок",
                actions = {
                    if (onToggleFavorite != null) {
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                painterResource(if (video.isFavorite) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24),
                                contentDescription = null,
                                tint = SpectrColors.Danger
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {
                VideoPlayer1(url = video.videoUrl)
            }

            Column(Modifier.padding(16.dp)) {
                Text(
                    video.title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpectrColors.Text
                )
                Spacer(Modifier.size(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SpectrColors.PrimarySoft)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            SpectrLabels.video(video.category),
                            color = SpectrColors.Primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Text(
                        "${video.durationSeconds / 60} мин",
                        color = SpectrColors.TextMuted,
                        fontSize = 12.sp
                    )
                    if (video.isWatched) {
                        Spacer(Modifier.size(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SpectrColors.Success,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.size(4.dp))
                            Text(
                                "Просмотрено",
                                color = SpectrColors.Success,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(Modifier.size(14.dp))
                Text(
                    video.description,
                    fontSize = 14.sp,
                    color = SpectrColors.Text
                )
            }
        }
    }
}

@Composable
private fun VideoPlayer1(url: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { PlayerView(it).apply { player = exoPlayer } }
    )
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(url: String, subtitlesUrl: String?) {
    val context = LocalContext.current
    val player = remember {
        VideoPlayerFactory.getPlayer(context).apply {
            val mediaItemBuilder = MediaItem.Builder().setUri(url)
            if (subtitlesUrl != null) {
                val subtitle = MediaItem.SubtitleConfiguration.Builder(Uri.parse(subtitlesUrl))
                    .setMimeType(MimeTypes.TEXT_VTT)
                    .setLanguage("ru")
                    .build()
                mediaItemBuilder.setSubtitleConfigurations(listOf(subtitle))
            }
            setMediaItem(mediaItemBuilder.build())
            prepare()
            playWhenReady = true
        }
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { PlayerView(it).apply { this.player = player } }
    )
    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
}
