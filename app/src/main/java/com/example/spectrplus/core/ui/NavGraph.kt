package com.example.spectrplus.core.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spectrplus.presentation.screen.MainScreen
import com.example.spectrplus.presentation.screen.auth.LoginScreen
import com.example.spectrplus.presentation.screen.auth.RegisterScreen

@Composable
fun NavGraph(startDestination: String) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("register_specialist") {
            RegisterScreen(navController, asSpecialist = true)
        }

        composable("main") {
            MainScreen()
        }

    }
}