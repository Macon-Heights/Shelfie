package me.alexandervortex.shelfie.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.features.navigate.RouterScreen
import me.alexandervortex.shelfie.features.settings.AppSettingsRepository
import me.alexandervortex.shelfie.features.settings.ProvideAppSettings
import me.alexandervortex.shelfie.ui.theme.ShelfieTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSettingsRepository: AppSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProvideAppSettings(appSettingsRepository) {
                ShelfieTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = getColors().background
                    ) { RouterScreen() }
                }
            }
        }
    }
}