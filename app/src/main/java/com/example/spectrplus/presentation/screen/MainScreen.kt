package com.example.spectrplus.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.spectrplus.core.ui.BottomNavItem
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.presentation.screen.education.ArticleDetailScreen
import com.example.spectrplus.presentation.screen.education.LearningScreen
import com.example.spectrplus.presentation.screen.education.VideoPlayerScreen
import com.example.spectrplus.presentation.screen.games.AnimalsGameScreen
import com.example.spectrplus.presentation.screen.games.OddOneOutGameScreen
import com.example.spectrplus.presentation.screen.games.DifferencesGameScreen
import com.example.spectrplus.presentation.screen.games.EmotionsGameScreen
import com.example.spectrplus.presentation.screen.games.LogicGameScreen
import com.example.spectrplus.presentation.screen.games.PartsGameScreen
import com.example.spectrplus.presentation.screen.games.SeasonsGameScreen
import com.example.spectrplus.presentation.screen.games.SequenceGameScreen
import com.example.spectrplus.presentation.screen.games.ShadowsGameScreen
import com.example.spectrplus.presentation.screen.games.SortingGameScreen
import com.example.spectrplus.presentation.screen.profile.AddChildScreen
import com.example.spectrplus.presentation.screen.profile.EditChildScreen
import com.example.spectrplus.presentation.screen.profile.EditProfileScreen
import com.example.spectrplus.presentation.screen.profile.FavoritesScreen
import com.example.spectrplus.presentation.screen.profile.PlansScreen
import com.example.spectrplus.presentation.screen.profile.ProfileScreen
import com.example.spectrplus.presentation.screen.profile.SupportScreen
import com.example.spectrplus.presentation.screen.social.ChatScreen
import com.example.spectrplus.presentation.screen.social.CommunityScreen
import com.example.spectrplus.presentation.screen.social.CreateTopicScreen
import com.example.spectrplus.presentation.screen.social.SpecialistPublishScreen
import com.example.spectrplus.presentation.screen.social.TopicScreen
import com.example.spectrplus.presentation.screen.social.UserProfileScreen
import com.example.spectrplus.presentation.viemodel.auth.AccountRoleViewModel
import com.example.spectrplus.presentation.viemodel.education.VideoViewModel

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val roleVm: AccountRoleViewModel = hiltViewModel()
    val accountRole by roleVm.accountRole.collectAsState()

    val items =
        if (accountRole == "SPECIALIST") {
            listOf(
                BottomNavItem.Profile,
                BottomNavItem.Learning,
                BottomNavItem.SpecialistContent,
                BottomNavItem.Chat
            )
        } else {
            listOf(
                BottomNavItem.Profile,
                BottomNavItem.Learning,
                BottomNavItem.Games,
                BottomNavItem.Chat
            )
        }

    Scaffold(
        containerColor = SpectrColors.Bg,
        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route

            NavigationBar(
                containerColor = Color.White,
                contentColor = SpectrColors.Primary,
                tonalElevation = 0.dp
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo("profile")
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(painter = painterResource(item.iconId), contentDescription = item.title) },
                        label = {
                            Text(
                                item.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = SpectrColors.Primary,
                            unselectedIconColor = SpectrColors.TextMuted,
                            unselectedTextColor = SpectrColors.TextMuted,
                            indicatorColor = SpectrColors.Primary
                        )
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Profile.route,
            modifier = Modifier.padding(padding)
        ) {

            composable("profile") { ProfileScreen(navController) }

            composable("learning") { LearningScreen(navController) }

            composable("games") {
                GamesScreen { gameId -> navController.navigate(gameId) }
            }

            composable("specialist_publish") { SpecialistPublishScreen() }

            composable("community") { CommunityScreen(navController) }

            composable("sorting") { SortingGameScreen(onBack = { navController.popBackStack() }) }
            composable("sequence") { SequenceGameScreen(onBack = { navController.popBackStack() }) }
            composable("emotions") { EmotionsGameScreen(onBack = { navController.popBackStack() }) }
            composable("odd_one_out") { OddOneOutGameScreen(onBack = { navController.popBackStack() }) }
            composable("animals") { AnimalsGameScreen(onBack = { navController.popBackStack() }) }
            composable("differences") { DifferencesGameScreen(onBack = { navController.popBackStack() }) }
            composable("seasons") { SeasonsGameScreen(onBack = { navController.popBackStack() }) }
            composable("parts") { PartsGameScreen(onBack = { navController.popBackStack() }) }
            composable("shadows") { ShadowsGameScreen(onBack = { navController.popBackStack() }) }
            composable("logic") { LogicGameScreen(onBack = { navController.popBackStack() }) }

            composable("edit_profile") { EditProfileScreen(navController) }
            composable("add_child") { AddChildScreen(navController) }
            composable("edit_child/{id}") { backStack ->
                val id = backStack.arguments?.getString("id")!!.toLong()
                EditChildScreen(childId = id, navController = navController)
            }
            composable("favorites") { FavoritesScreen(navController) }
            composable("plans") { PlansScreen(navController) }
            composable("support") { SupportScreen(navController) }

            composable("videos") { LearningScreen(navController) }
            composable("forum") { CommunityScreen(navController) }
            composable("chat") { CommunityScreen(navController) }

            composable("topic/{id}") { backStack ->
                val id = backStack.arguments?.getString("id")!!.toLong()
                TopicScreen(id, navController)
            }
            composable("create_topic") { CreateTopicScreen(navController) }

            composable("user_profile/{userId}") { backStack ->
                val userId = backStack.arguments?.getString("userId")!!.toLong()
                UserProfileScreen(userId, navController)
            }

            composable("chat/{userId}") { backStack ->
                val userId = backStack.arguments?.getString("userId")!!.toLong()
                ChatScreen(userId)
            }

            composable("article_detail/{id}") { backStack ->
                val id = backStack.arguments?.getString("id")!!.toLong()
                ArticleDetailScreen(id, navController)
            }

            composable("video_player/{id}",) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                    ?: return@composable
                val viewModel: VideoViewModel = hiltViewModel()
                LaunchedEffect(id) { viewModel.loadById(id) }
                val video = viewModel.selectedVideo
                if (video != null) {
                    VideoPlayerScreen(
                        video = video,
                        onToggleFavorite = { viewModel.toggleFavorite(video.id) },
                        onWatched = { viewModel.markWatched(video.id) }
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Загрузка...", color = SpectrColors.TextMuted)
                    }
                }
            }
        }
    }
}
