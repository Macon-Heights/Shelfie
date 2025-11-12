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
    val stoppingTime by repository.stoppingTimeFlow.collectAsState(initial = 0L)
    val lineHeight by repository.lineHeightFlow.collectAsState(initial = 1f)

    CompositionLocalProvider(
        LocalAppSettings.fontSize provides fontSize,
        LocalAppSettings.stoppingTime provides stoppingTime,
        LocalAppSettings.lineHeight provides lineHeight
    ) { content.invoke() }
}