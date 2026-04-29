package com.example.spectrplus.presentation.screen.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.InitialsAvatar
import com.example.spectrplus.core.ui.LoadingBlock
import com.example.spectrplus.core.ui.SectionHeader
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.presentation.viemodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state

    if (state.isLoading) {
        Box(
            Modifier
                .fillMaxSize()
                .background(SpectrColors.Bg),
            contentAlignment = Alignment.Center
        ) {
            LoadingBlock()
        }
        return
    }

    val profile = state.profile ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SpectrColors.Bg),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            ProfileHero(
                name = "${profile.firstName} ${profile.lastName}",
                email = profile.email,
                avatarUrl = profile.avatarUrl,
                onEdit = { navController.navigate("edit_profile") }
            )
        }

        item {
            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SectionHeader(title = "Контактная информация")
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
                    elevation = CardDefaults.elevatedCardElevation(2.dp)
                ) {
                    Column(Modifier.padding(vertical = 4.dp)) {
                        ContactRow(Icons.Default.Email, profile.email)
                        Divider()
                        ContactRow(Icons.Default.Phone, profile.phone)
                        Divider()
                        ContactRow(
                            Icons.Default.LocationOn,
                            profile.city.takeUnless { it.isNullOrBlank() } ?: "Город не указан"
                        )
                    }
                }
            }
        }

        if (profile.accountRole == "SPECIALIST") {
            item {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        SectionHeader(title = "Профиль специалиста")
                    }
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
                        elevation = CardDefaults.elevatedCardElevation(1.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            profile.specialistProfession?.takeIf { it.isNotBlank() }?.let {
                                Text(it, fontSize = 15.sp, color = SpectrColors.Text, fontWeight = FontWeight.SemiBold)
                            }
                            profile.specialistEducation?.takeIf { it.isNotBlank() }?.let {
                                Text("Образование", fontSize = 12.sp, color = SpectrColors.TextMuted)
                                Text(it, fontSize = 14.sp, color = SpectrColors.Text)
                            }
                            profile.specialistExperienceYears?.let { y ->
                                Text("Опыт: $y лет", fontSize = 14.sp, color = SpectrColors.Text)
                            }
                        }
                    }
                }
            }
        } else {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    QuickAction(
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Add,
                        title = "Добавить ребенка",
                        color = Color(0xFFE65100),
                        background = SpectrColors.AccentSoft
                    ) { navController.navigate("add_child") }
                }
            }

            item {
                Column(
                    Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionHeader(
                        title = "Дети",
                        subtitle = if (profile.children.isEmpty()) "Добавьте ребенка, чтобы видеть его здесь" else null
                    )
                }
            }
            if (profile.children.isEmpty()) {
                item {
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        ElevatedCard(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
                            elevation = CardDefaults.elevatedCardElevation(1.dp)
                        ) {
                            EmptyState(
                                emoji = "🧒",
                                title = "Пока нет карточек ребенка",
                                subtitle = "Нажмите «Добавить ребенка», чтобы начать"
                            )
                        }
                    }
                }
            } else {
                items(profile.children) { child ->
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        ChildCard(
                            child = child,
                            onEdit = { navController.navigate("edit_child/${child.id}") }
                        )
                    }
                }
            }
        }

        item {
            Box(Modifier.padding(horizontal = 16.dp)) {
                NavCard(
                    icon = ImageVector.vectorResource(R.drawable.baseline_calendar_month_24),
                    title = "Планы",
                    subtitle = "Календарь занятий и задач",
                    background = Color(0xFFE3F2FD),
                    color = Color(0xFF1976D2),
                    onClick = { navController.navigate("plans") }
                )
            }
        }

        item {
            Box(Modifier.padding(horizontal = 16.dp)) {
                NavCard(
                    icon = Icons.Default.Favorite,
                    title = "Избранное",
                    subtitle = "Сохраненные статьи и видео",
                    background = SpectrColors.DangerSoft,
                    color = SpectrColors.Danger,
                    onClick = { navController.navigate("favorites") }
                )
            }
        }

        item {
            Box(Modifier.padding(horizontal = 16.dp)) {
                NavCard(
                    icon = ImageVector.vectorResource(R.drawable.baseline_help_outline_24),
                    title = "Поддержка",
                    subtitle = "support@spectrplus.app",
                    background = SpectrColors.SuccessSoft,
                    color = SpectrColors.Success,
                    onClick = { navController.navigate("support") }
                )
            }
        }

        item {
            Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.logout() },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = SpectrColors.Danger
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Выйти из аккаунта", color = SpectrColors.Danger, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun ProfileHero(
    name: String,
    email: String,
    avatarUrl: String?,
    onEdit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpectrColors.HeaderGradient)
            .padding(horizontal = 20.dp)
            .padding(top = 48.dp, bottom = 28.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                )
            } else {
                InitialsAvatar(
                    name = name,
                    size = 72,
                    background = androidx.compose.ui.graphics.Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.35f), Color.White.copy(alpha = 0.15f))
                    )
                )
            }

            Spacer(Modifier.size(16.dp))

            Column(Modifier.weight(1f)) {

                Text(
                    name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    email,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onEdit() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Редактировать", tint = Color.White)
            }
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SpectrColors.PrimarySoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = SpectrColors.Primary, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.size(12.dp))
        Text(text, fontSize = 15.sp, color = SpectrColors.Text)
    }
}

@Composable
private fun Divider() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(SpectrColors.Divider)
    )
}

@Composable
private fun QuickAction(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    color: Color,
    background: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .height(96.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(background),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = SpectrColors.Text)
        }
    }
}

@Composable
private fun NavCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    background: Color,
    color: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(background),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color)
            }
            Spacer(Modifier.size(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = SpectrColors.Text)
                Text(subtitle, fontSize = 12.sp, color = SpectrColors.TextMuted)
            }
            Text("›", fontSize = 22.sp, color = SpectrColors.TextMuted)
        }
    }
}
