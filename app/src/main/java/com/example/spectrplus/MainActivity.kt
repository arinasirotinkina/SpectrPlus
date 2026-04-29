package com.example.spectrplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.spectrplus.core.ui.NavGraph
import com.example.spectrplus.presentation.screen.SplashScreen
import com.example.spectrplus.presentation.theme.SpectrPlusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpectrPlusTheme {
                val status by viewModel.authStatus.collectAsState()

                AnimatedContent(
                    targetState = status,
                    transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                    label = "auth_status"
                ) { current ->
                    when (current) {
                        AuthStatus.Loading -> SplashScreen()
                        AuthStatus.Unauthenticated -> NavGraph(startDestination = "login")
                        is AuthStatus.Authenticated -> NavGraph(startDestination = "main")
                    }
                }
            }
        }
    }
}
