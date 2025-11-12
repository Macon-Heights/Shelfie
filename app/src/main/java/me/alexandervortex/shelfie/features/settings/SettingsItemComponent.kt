package me.alexandervortex.shelfie.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.component.refactored.ButtonComponent
import me.alexandervortex.shelfie.ui.theme.IC_RESET

@Composable
fun SettingsItemComponent(
    title: String,
    value: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onReset: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
        )
        ButtonComponent(
            containerColor = getColors().tertiary,
            contentColor = getColors().onTertiary,
            modifier = Modifier.padding(horizontal = 4.dp),
            modifierAfter = Modifier.clickable {
                onDecrease.invoke()
            },
            content = { Text(text = "-", color = it) },
        )
        Text(text = value)
        ButtonComponent(
            modifier = Modifier.padding(start = 4.dp),
            modifierAfter = Modifier.clickable {
                onIncrease.invoke()
            },
            content = { Text(text = "+", color = it) }
        )
        ButtonComponent(
            modifier = Modifier.padding(start = 4.dp),
            modifierAfter = Modifier.clickable {
                onReset.invoke()
            },
            content = {
                Icon(
                    imageVector = IC_RESET,
                    contentDescription = null,
                    tint = it
                )
            }
        )
    }
}