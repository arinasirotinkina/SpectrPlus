package com.example.spectrplus.presentation.screen.social

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.navigation.NavController
import com.example.spectrplus.core.ui.CategoryChip
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.InitialsAvatar
import com.example.spectrplus.core.ui.LoadingBlock
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrLabels
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.domain.model.social.Post
import com.example.spectrplus.domain.model.social.Topic
import com.example.spectrplus.presentation.viemodel.social.ForumViewModel
import com.example.spectrplus.presentation.viemodel.profile.ProfileViewModel
import com.example.spectrplus.presentation.viemodel.social.TopicViewModel
import com.example.spectrplus.presentation.viemodel.social.UserProfileViewModel

@Composable
fun ForumSection(navController: NavController) {

    val viewModel: ForumViewModel = hiltViewModel()
    val state = viewModel.state
    var category by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { viewModel.load() }

    Column(Modifier.fillMaxSize()) {

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            item {
                CategoryChip(
                    label = "Все",
                    selected = category == null,
                    onClick = {
                        category = null
                        viewModel.load(null)
                    }
                )
            }
            items(SpectrLabels.forumCategories.entries.toList()) { e ->
                CategoryChip(
                    label = e.value,
                    selected = category == e.key,
                    onClick = {
                        category = e.key
                        viewModel.load(e.key)
                    }
                )
            }
        }

        when {
            state.isLoading -> LoadingBlock()
            state.topics.isEmpty() -> EmptyState(
                emoji = "💬",
                title = "Пока нет тем",
                subtitle = "Начните обсуждение — нажмите кнопку +"
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.topics) { topic ->
                    TopicItem(topic) {
                        navController.navigate("topic/${topic.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun ForumScreen(navController: NavController) {
    ForumSection(navController)
}

@Composable
fun TopicItem(topic: Topic, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InitialsAvatar(name = topic.authorName, size = 44)
            Spacer(Modifier.size(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    topic.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SpectrColors.Text,
                    maxLines = 2
                )
                Spacer(Modifier.size(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SpectrColors.PrimarySoft)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            SpectrLabels.forum(topic.category),
                            fontSize = 11.sp,
                            color = SpectrColors.Primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Text(
                        topic.authorName,
                        fontSize = 12.sp,
                        color = SpectrColors.TextMuted
                    )
                }
                Spacer(Modifier.size(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "💬 ${topic.postsCount}",
                        fontSize = 11.sp,
                        color = SpectrColors.TextMuted
                    )
                    Spacer(Modifier.size(12.dp))
                    Text(
                        "🔔 ${topic.subscribersCount}",
                        fontSize = 11.sp,
                        color = SpectrColors.TextMuted
                    )
                }
            }
            Text("›", fontSize = 22.sp, color = SpectrColors.TextMuted)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(topicId: Long, navController: NavController) {

    val viewModel: TopicViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val currentUserId = profileViewModel.state.profile?.id

    var text by remember { mutableStateOf("") }
    var editingPostId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(topicId) { viewModel.load(topicId) }

    val topic = viewModel.topic

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = topic?.title?.take(22) ?: "Обсуждение",
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { viewModel.toggleSubscribe(topicId) }) {
                        Text(
                            if (viewModel.isSubscribed) "🔔" else "🔕",
                            fontSize = 18.sp
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (topic != null) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(SpectrColors.Card)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        topic.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpectrColors.Text
                    )
                    Spacer(Modifier.size(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpectrColors.PrimarySoft)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                SpectrLabels.forum(topic.category),
                                fontSize = 11.sp,
                                color = SpectrColors.Primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(Modifier.size(10.dp))
                        Text(
                            "💬 ${topic.postsCount}",
                            fontSize = 12.sp,
                            color = SpectrColors.TextMuted
                        )
                        Spacer(Modifier.size(10.dp))
                        Text(
                            "🔔 ${topic.subscribersCount}",
                            fontSize = 12.sp,
                            color = SpectrColors.TextMuted
                        )
                    }
                }
            }

            if (viewModel.posts.isEmpty()) {
                Box(Modifier.weight(1f)) {
                    EmptyState(
                        emoji = "✍️",
                        title = "Станьте первым",
                        subtitle = "Напишите первый ответ в теме"
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.posts) { post ->
                        PostItem(
                            post = post,
                            navController = navController,
                            isOwner = currentUserId == post.authorId,
                            onEdit = { editingPostId = post.id; text = post.content },
                            onDelete = { viewModel.deletePost(post.id) }
                        )
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
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            if (editingPostId != null) "Редактирование сообщения"
                            else "Напишите сообщение"
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = spectrFieldColors(),
                    maxLines = 4
                )
                if (editingPostId != null) {
                    Spacer(Modifier.size(4.dp))
                    IconButton(
                        onClick = { editingPostId = null; text = "" },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SpectrColors.Bg)
                    ) {
                        Text("✕", fontSize = 16.sp, color = SpectrColors.TextMuted)
                    }
                }
                Spacer(Modifier.size(8.dp))
                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            val editId = editingPostId
                            if (editId != null) {
                                viewModel.editPost(editId, text)
                                editingPostId = null
                            } else {
                                viewModel.send(topicId, text)
                            }
                            text = ""
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

@Composable
fun PostItem(
    post: Post,
    navController: NavController,
    isOwner: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {
        Row(Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier.clickable {
                    navController.navigate("user_profile/${post.authorId}")
                }
            ) {
                InitialsAvatar(name = post.authorName, size = 38)
            }
            Spacer(Modifier.size(10.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            post.authorName,
                            fontSize = 13.sp,
                            color = SpectrColors.Primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                navController.navigate("user_profile/${post.authorId}")
                            }
                        )
                        if (!post.authorProfessionalLabel.isNullOrBlank()) {
                            Spacer(Modifier.size(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SpectrColors.PrimarySoft)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    post.authorProfessionalLabel,
                                    fontSize = 11.sp,
                                    color = SpectrColors.Primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    if (isOwner) {
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Text("✏️", fontSize = 14.sp)
                        }
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Text("🗑", fontSize = 14.sp)
                        }
                    }
                }
                Spacer(Modifier.size(4.dp))
                Text(
                    post.content,
                    fontSize = 14.sp,
                    color = SpectrColors.Text
                )
                if (post.createdAt.isNotBlank()) {
                    Spacer(Modifier.size(6.dp))
                    Text(
                        post.createdAt,
                        fontSize = 11.sp,
                        color = SpectrColors.TextMuted
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTopicScreen(navController: NavController) {

    val viewModel: ForumViewModel = hiltViewModel()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("QUESTIONS") }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Новая тема",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок темы") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = spectrFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = SpectrLabels.forum(category),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Раздел форума") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    SpectrLabels.forumCategories.forEach { (code, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                category = code
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Сообщение") },
                shape = RoundedCornerShape(16.dp),
                colors = spectrFieldColors(),
                minLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        viewModel.createTopic(title, category, content) {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary)
            ) {
                Text("Опубликовать", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(userId: Long, navController: NavController) {
    val viewModel: UserProfileViewModel =
        hiltViewModel()

    LaunchedEffect(userId) { viewModel.load(userId) }

    val user = viewModel.user
    val isLoading = viewModel.isLoading

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Профиль",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        when {
            isLoading && user == null -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { LoadingBlock() }

            user == null -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                EmptyState(
                    emoji = "👤",
                    title = "Профиль недоступен",
                    subtitle = "Не удалось загрузить информацию"
                )
            }

            else -> Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!user.avatarUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(SpectrColors.PrimarySoft)
                    )
                } else {
                    InitialsAvatar(
                        name = "${user.firstName} ${user.lastName}",
                        size = 96
                    )
                }
                Text(
                    "${user.firstName} ${user.lastName}".trim(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpectrColors.Text
                )
                val subtitle = when (user.accountRole) {
                    "SPECIALIST" -> "Специалист"
                    else -> "Родитель"
                }
                Text(subtitle, fontSize = 13.sp, color = SpectrColors.TextMuted)

                user.city?.takeIf { it.isNotBlank() }?.let { city ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📍 ", fontSize = 14.sp)
                        Text(city, fontSize = 14.sp, color = SpectrColors.Text)
                    }
                }

                if (user.accountRole == "SPECIALIST") {
                    user.specialistProfession?.takeIf { it.isNotBlank() }?.let {
                        Text(it, fontSize = 14.sp, color = SpectrColors.Text, fontWeight = FontWeight.SemiBold)
                    }
                    user.specialistEducation?.takeIf { it.isNotBlank() }?.let {
                        Text("Образование", fontSize = 12.sp, color = SpectrColors.TextMuted)
                        Text(it, fontSize = 14.sp, color = SpectrColors.Text)
                    }
                    user.specialistExperienceYears?.let {
                        Text("Опыт: $it лет", fontSize = 14.sp, color = SpectrColors.Text)
                    }
                }

                val child = user.publicChildProfile
                if (user.accountRole == "PARENT" && child != null) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
                        elevation = CardDefaults.elevatedCardElevation(1.dp)
                    ) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Ребёнок", fontSize = 12.sp, color = SpectrColors.TextMuted, fontWeight = FontWeight.Medium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val g = child.gender.trim().lowercase()
                                val childEmoji = when {
                                    g.startsWith("м") || g.startsWith("m") -> "👦"
                                    g.startsWith("ж") || g.startsWith("f") || g.startsWith("w") -> "👧"
                                    else -> "🧒"
                                }
                                Text(
                                    childEmoji,
                                    fontSize = 22.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Column {
                                    Text(child.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = SpectrColors.Text)
                                    Text("${child.age} лет", fontSize = 13.sp, color = SpectrColors.TextMuted)
                                }
                            }
                            Text("Диагноз: ${child.diagnosis}", fontSize = 14.sp, color = SpectrColors.Text)
                            if (child.therapies.isNotEmpty()) {
                                Text("Терапии", fontSize = 12.sp, color = SpectrColors.TextMuted)
                                child.therapies.forEach { t ->
                                    Text("• ${t.title}${if (t.description.isNotBlank()) ": ${t.description}" else ""}", fontSize = 13.sp, color = SpectrColors.Text)
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCell(user.topicsCount, "тем")
                    StatCell(user.postsCount, "сообщений")
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { navController.navigate("chat/$userId") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White)
                    Spacer(Modifier.size(8.dp))
                    Text("Написать сообщение", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun StatCell(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = SpectrColors.Primary
        )
        Text(
            label,
            fontSize = 11.sp,
            color = SpectrColors.TextMuted
        )
    }
}
