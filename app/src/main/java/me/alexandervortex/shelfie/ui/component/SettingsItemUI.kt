package me.alexandervortex.shelfie.ui.component

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
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_NEXT
import me.alexandervortex.shelfie.ui.theme.IC_PLAYER_PREV
import me.alexandervortex.shelfie.ui.theme.IC_RESET

@Composable
fun SettingsItemUI(
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
        ButtonUI(
            containerColor = getColors().tertiary,
            contentColor = getColors().onTertiary,
            modifier = Modifier.padding(horizontal = 4.dp),
            modifierAfter = Modifier.clickable {
                onDecrease.invoke()
            },
            content = {
                Icon(
                    imageVector = IC_PLAYER_PREV,
                    contentDescription = null,
                    tint = it
                )
            },
        )
        Text(text = value)
        ButtonUI(
            modifier = Modifier.padding(start = 4.dp),
            modifierAfter = Modifier.clickable {
                onIncrease.invoke()
            },
            content = {
                Icon(
                    imageVector = IC_PLAYER_NEXT,
                    contentDescription = null,
                    tint = it
                )
            }
        )
        ButtonUI(
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