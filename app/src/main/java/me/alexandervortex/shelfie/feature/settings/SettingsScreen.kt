package me.alexandervortex.shelfie.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.R
import me.alexandervortex.shelfie.feature.settings.values.ThemeValue
import me.alexandervortex.shelfie.feature.settings.values.next
import me.alexandervortex.shelfie.feature.settings.values.prev
import me.alexandervortex.shelfie.ui.component.SettingsItemUI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        val fontSize = LocalAppSettings.fontSize.current
        SettingsItemUI(
            stringResource(R.string.settings_font_size),
            value = fontSize.toString(),
            onDecrease = {
                viewModel?.onIntent(SettingsIntent.ChangeFont(fontSize - 2))
            },
            onIncrease = {
                viewModel?.onIntent(SettingsIntent.ChangeFont(fontSize + 2))
            },
            onReset = {
                viewModel?.onIntent(SettingsIntent.ChangeFont())
            }
        )
        Spacer(Modifier.size(16.dp))
        val lineHeight = LocalAppSettings.lineHeight.current
        SettingsItemUI(
            stringResource(R.string.settings_line_height),
            value = lineHeight.toString(),
            onDecrease = {
                viewModel?.onIntent(SettingsIntent.ChangeLineHeight(lineHeight - 0.25f))
            },
            onIncrease = {
                viewModel?.onIntent(SettingsIntent.ChangeLineHeight(lineHeight + 0.25f))
            },
            onReset = {
                viewModel?.onIntent(SettingsIntent.ChangeLineHeight())
            }
        )
        Spacer(Modifier.size(16.dp))
        val padding = LocalAppSettings.padding.current
        SettingsItemUI(
            title = stringResource(R.string.settings_paddings),
            value = padding.toString(),
            onDecrease = {
                viewModel?.onIntent(SettingsIntent.ChangePadding(padding - 2))
            },
            onIncrease = {
                viewModel?.onIntent(SettingsIntent.ChangePadding(padding + 2))
            },
            onReset = {
                viewModel?.onIntent(SettingsIntent.ChangePadding())
            }
        )
        Spacer(Modifier.size(16.dp))
        val themeValue = LocalAppSettings.theme.current
        val theme = ThemeValue.fromValue(themeValue)
        SettingsItemUI(
            title = stringResource(R.string.settings_theme),
            value = stringResource(ThemeValue.fromValue(themeValue).textResId),
            onDecrease = {
                viewModel?.onIntent(SettingsIntent.ChangeTheme(theme.prev()))
            },
            onIncrease = {
                viewModel?.onIntent(SettingsIntent.ChangeTheme(theme.next()))
            },
            onReset = {
                viewModel?.onIntent(SettingsIntent.ChangeTheme())
            }
        )
    }
}

@CombinedPreviews
@Composable
private fun SettingsPreview() {
    CombinedPreviews {
        SettingsScreen(null)
    }
}
