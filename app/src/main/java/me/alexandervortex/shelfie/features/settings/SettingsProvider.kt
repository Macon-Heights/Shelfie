package me.alexandervortex.shelfie.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun SettingsProvider(
    repository: AppSettingsRepository,
    content: @Composable () -> Unit,
) {
    val fontSize by repository.fontSizeFlow.collectAsState(initial = 16)

    CompositionLocalProvider(
        LocalAppSettings.fontSize provides fontSize
    ) { content.invoke() }
}