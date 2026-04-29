package com.example.spectrplus.presentation.screen.education

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.InitialsAvatar
import com.example.spectrplus.core.ui.LoadingBlock
import com.example.spectrplus.core.ui.SectionHeader
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrLabels
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.domain.model.education.Article
import com.example.spectrplus.domain.model.education.ArticleComment
import com.example.spectrplus.presentation.viemodel.education.ArticlesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    id: Long,
    navController: NavController,
    viewModel: ArticlesViewModel = hiltViewModel()
) {
    LaunchedEffect(id) { viewModel.loadArticleDetail(id) }

    val article = viewModel.selectedArticle
    val comments = viewModel.comments
    val related = viewModel.related
    val isLoading = viewModel.isDetailLoading

    var commentText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Статья",
                onBack = { navController.popBackStack() },
                actions = {
                    if (article != null) {
                        IconButton(onClick = { viewModel.onFavoriteClick(article.id) }) {
                            Icon(
                                painterResource(if (article.isFavorite) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24),
                                contentDescription = null,
                                tint = SpectrColors.Danger
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading && article == null -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { LoadingBlock() }

            article == null -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                EmptyState(
                    emoji = "📭",
                    title = "Статья не найдена",
                    subtitle = "Попробуйте открыть ещё раз"
                )
            }

            else -> {
                val gradient = SpectrColors.categoryGradients[
                    article.category.hashCode().let { if (it < 0) -it else it } % SpectrColors.categoryGradients.size
                ]

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(gradient)
                    ) {
                        Text("📖", fontSize = 70.sp, modifier = Modifier.align(Alignment.Center))
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                SpectrLabels.article(article.category),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Column(Modifier.padding(16.dp)) {
                        Text(
                            article.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpectrColors.Text
                        )
                        Spacer(Modifier.size(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InitialsAvatar(name = article.author, size = 36)
                            Spacer(Modifier.size(10.dp))
                            Column {
                                Text(
                                    article.author,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpectrColors.Text
                                )
                                Text("Автор", fontSize = 11.sp, color = SpectrColors.TextMuted)
                            }
                        }
                        Spacer(Modifier.size(16.dp))
                        Text(
                            article.content,
                            fontSize = 15.sp,
                            color = SpectrColors.Text,
                            lineHeight = 22.sp
                        )
                        Spacer(Modifier.size(24.dp))
                    }

                    if (related.isNotEmpty()) {
                        Column(Modifier.padding(horizontal = 16.dp)) {
                            SectionHeader(title = "Похожие статьи")
                            Spacer(Modifier.size(8.dp))
                            related.forEach { relatedArticle ->
                                RelatedArticleCard(relatedArticle) {
                                    navController.navigate("article_detail/${relatedArticle.id}")
                                }
                                Spacer(Modifier.size(8.dp))
                            }
                            Spacer(Modifier.size(16.dp))
                        }
                    }

                    Column(Modifier.padding(horizontal = 16.dp)) {
                        SectionHeader(title = "Комментарии (${comments.size})")
                        Spacer(Modifier.size(8.dp))
                        if (comments.isEmpty()) {
                            Text(
                                "Пока нет комментариев. Будьте первым!",
                                fontSize = 13.sp,
                                color = SpectrColors.TextMuted,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            comments.forEach { c ->
                                CommentItem(c)
                                Spacer(Modifier.size(8.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SpectrColors.Card)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Оставьте комментарий") },
                            shape = RoundedCornerShape(24.dp),
                            colors = spectrFieldColors(),
                            maxLines = 4
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (commentText.isNotBlank()) {
                                    viewModel.addComment(article.id, commentText)
                                    commentText = ""
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SpectrColors.Primary)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RelatedArticleCard(article: Article, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                    .background(SpectrColors.PrimarySoft),
                contentAlignment = Alignment.Center
            ) {
                Text("📖", fontSize = 22.sp)
            }
            Spacer(Modifier.size(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    article.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SpectrColors.Text,
                    maxLines = 2
                )
                Text(
                    SpectrLabels.article(article.category),
                    fontSize = 11.sp,
                    color = SpectrColors.TextMuted
                )
            }
        }
    }
}

@Composable
private fun CommentItem(comment: ArticleComment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        InitialsAvatar(name = comment.authorName, size = 36)
        Spacer(Modifier.size(10.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(SpectrColors.Card)
                .padding(12.dp)
        ) {
            Text(
                comment.authorName,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = SpectrColors.Primary
            )
            Spacer(Modifier.size(4.dp))
            Text(
                comment.content,
                fontSize = 13.sp,
                color = SpectrColors.Text
            )
        }
    }
}
