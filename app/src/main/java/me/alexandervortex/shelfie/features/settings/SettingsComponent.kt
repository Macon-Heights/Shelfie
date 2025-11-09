package me.alexandervortex.shelfie.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.base.ext.getStaticSurfaceVariant
import me.alexandervortex.shelfie.ui.component.refactored.BOX_PADDING
import me.alexandervortex.shelfie.ui.component.refactored.ButtonComponent
import me.alexandervortex.shelfie.ui.component.refactored.ROOT_PADDING
import me.alexandervortex.shelfie.ui.component.refactored.TITLE_SIZE
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_M

private const val STEP_SIZE = 2

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "app font size", fontSize = TITLE_SIZE.sp
                )
                val fontSize = LocalAppSettings.fontSize.current
                ButtonComponent(
                    containerColor = getColors().tertiary,
                    contentColor = getColors().onTertiary,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    modifierAfter = Modifier.clickable {
                        viewModel?.onIntent(SettingsIntent.ChangeFont(fontSize - STEP_SIZE))
                    },
                    content = { Text(text = "-", color = it) },
                )

                Text(
                    text = fontSize.toString(),
                    fontSize = TITLE_SIZE.sp
                )
                ButtonComponent(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    modifierAfter = Modifier.clickable {
                        viewModel?.onIntent(SettingsIntent.ChangeFont(fontSize + STEP_SIZE))
                    },
                    content = { Text(text = "+", color = it) }
                )
            }
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