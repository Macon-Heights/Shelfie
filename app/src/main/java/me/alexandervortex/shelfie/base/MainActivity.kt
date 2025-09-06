package me.alexandervortex.shelfie.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import me.alexandervortex.shelfie.features.navigation.RouterScreen
import me.alexandervortex.shelfie.ui.theme.ShelfieTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShelfieTheme {
                RouterScreen()
            }
        }
    }
}