package me.alexandervortex.shelfie.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.base.ext.getStaticSurfaceVariant
import me.alexandervortex.shelfie.ui.component.refactored.BOX_PADDING
import me.alexandervortex.shelfie.ui.component.refactored.ROOT_PADDING
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_M

@Composable
fun SettingsComponent(
    viewModel: SettingsViewModel?,
    onDecline: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(getStaticSurfaceVariant().copy(alpha = 0.7f))
            .clickable { onDecline.invoke() }
            .padding(ROOT_PADDING.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(SHAPE_M)
                .background(getColors().surface)
                .padding(BOX_PADDING.dp)
        ) {
            val fontSize = LocalAppSettings.fontSize.current
            SettingsItemComponent(
                title = "App font size",
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
            SettingsItemComponent(
                title = "Line height",
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
        }
    }
}

@CombinedPreviews
@Composable
private fun SettingsPreview() {
    CombinedPreviews {
        SettingsComponent(null) {}
    }
}