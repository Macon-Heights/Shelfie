package me.alexandervortex.shelfie.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.component.refactored.ButtonComponent
import me.alexandervortex.shelfie.ui.component.refactored.TITLE_SIZE

@Composable
fun SettingsItemComponent(
    title: String,
    value: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontSize = TITLE_SIZE.sp
        )

        ButtonComponent(
            containerColor = getColors().tertiary,
            contentColor = getColors().onTertiary,
            modifier = Modifier.padding(horizontal = 8.dp),
            modifierAfter = Modifier.clickable {
                onDecrease.invoke()
            },
            content = { Text(text = "-", color = it) },
        )

        Text(
            text = value,
            fontSize = TITLE_SIZE.sp
        )
        ButtonComponent(
            modifier = Modifier.padding(horizontal = 8.dp),
            modifierAfter = Modifier.clickable {
                onIncrease.invoke()
            },
            content = { Text(text = "+", color = it) }
        )
    }
}