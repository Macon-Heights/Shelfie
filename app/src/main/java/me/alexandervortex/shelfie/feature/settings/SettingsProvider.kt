package me.alexandervortex.shelfie.feature.settings

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
    val theme by repository.themeFlow.collectAsState(initial = 0)

    CompositionLocalProvider(
        LocalAppSettings.fontSize provides fontSize,
        LocalAppSettings.padding provides padding,
        LocalAppSettings.lineHeight provides lineHeight,
        LocalAppSettings.theme provides theme
    ) { content.invoke() }
}