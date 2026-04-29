package com.example.spectrplus.core.ui

import androidx.annotation.DrawableRes
import com.example.spectrplus.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val iconId: Int
) {
    object Profile : BottomNavItem("profile", "Профиль", R.drawable.baseline_person_24)
    object Learning : BottomNavItem("learning", "Обучение", R.drawable.baseline_menu_book_24)
    object Games : BottomNavItem("games", "Игры", R.drawable.baseline_extension_24)
    object SpecialistContent : BottomNavItem("specialist_publish", "Контент", R.drawable.baseline_extension_24)
    object Chat : BottomNavItem("community", "Общение", R.drawable.baseline_chat_24)
}
