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
    val padding by repository.paddingFlow.collectAsState(initial = 24)
    val lineHeight by repository.lineHeightFlow.collectAsState(initial = 1f)

    CompositionLocalProvider(
        LocalAppSettings.fontSize provides fontSize,
        LocalAppSettings.padding provides padding,
        LocalAppSettings.lineHeight provides lineHeight
    ) { content.invoke() }
}