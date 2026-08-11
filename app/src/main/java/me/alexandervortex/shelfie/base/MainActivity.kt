package me.alexandervortex.shelfie.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.navigation.RouterScreen
import me.alexandervortex.shelfie.data.repository.AppSettingsRepository
import me.alexandervortex.shelfie.feature.settings.SettingsProvider
import me.alexandervortex.shelfie.ui.theme.ShelfieTheme
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSettingsRepository: AppSettingsRepository
    private var dataUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dataUri = handleIntent(intent)
        setContent {
            SettingsProvider(appSettingsRepository) {
                ShelfieTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = getColors().background
                    ) { RouterScreen(dataUri) }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        dataUri = handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?): Uri? {
        return when (intent?.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(Intent.EXTRA_STREAM)
                }
            }
            else -> null
        }
    }
}