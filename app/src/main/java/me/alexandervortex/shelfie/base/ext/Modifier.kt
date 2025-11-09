package me.alexandervortex.shelfie.base.ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

private const val DEFAULT_ELEVATION = 8

fun Modifier.clipNShadow(shape: Shape): Modifier {
    return this.shadow(
        elevation = DEFAULT_ELEVATION.dp,
        shape = shape,
        clip = true
    )
}