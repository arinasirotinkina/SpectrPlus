package com.example.spectrplus.presentation.screen.education

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.spectrplus.core.ui.CategoryChip
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.LoadingBlock
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrLabels
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.domain.model.education.Article
import com.example.spectrplus.presentation.viemodel.education.ArticlesViewModel

@Composable
fun ArticlesSection(
    navController: NavController,
    viewModel: ArticlesViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Column(Modifier.fillMaxSize()) {

        OutlinedTextField(
            value = state.search,
            onValueChange = viewModel::onSearchChange,
            placeholder = { Text("Поиск статей") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = SpectrColors.TextMuted) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = spectrFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Spacer(Modifier.height(4.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                CategoryChip(
                    label = "Все",
                    selected = state.category == null,
                    onClick = { viewModel.onCategoryChange(null) }
                )
            }
            items(SpectrLabels.articleCategories.entries.toList()) { entry ->
                CategoryChip(
                    label = entry.value,
                    selected = state.category == entry.key,
                    onClick = { viewModel.onCategoryChange(entry.key) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        when {
            state.isLoading -> LoadingBlock()
            state.articles.isEmpty() -> EmptyState(
                emoji = "📖",
                title = "Статьи не найдены",
                subtitle = "Попробуйте изменить поиск или категорию"
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.articles) { article ->
                    ArticleCard(
                        article = article,
                        onClick = { navController.navigate("article_detail/${article.id}") },
                        onFavorite = { viewModel.onFavoriteClick(article.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit,
    onFavorite: () -> Unit
) {
    val gradient = SpectrColors.categoryGradients[
        article.category.hashCode().let { if (it < 0) -it else it } % SpectrColors.categoryGradients.size
    ]

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(gradient)
            ) {
                Text(
                    "📖",
                    fontSize = 50.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = onFavorite,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                    ) {
                        Icon(
                            painterResource(if (article.isFavorite) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SpectrColors.Text,
                    maxLines = 2
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    article.content.take(140).let { if (article.content.length > 140) "$it…" else it },
                    fontSize = 13.sp,
                    color = SpectrColors.TextMuted
                )
                Spacer(Modifier.size(10.dp))
                Text(
                    "Автор: ${article.author}",
                    fontSize = 12.sp,
                    color = SpectrColors.TextMuted
                )
            }
        }
    }
}
