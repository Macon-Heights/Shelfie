package me.alexandervortex.shelfie.ui.component.new

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val GAP = 16

@Composable
fun CarouselImageUI(
    modifier: Modifier = Modifier, images: List<ByteArray?>? = null
) {
    images?.let {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(GAP.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            images.forEach {
                ImageUI(
                    modifier = Modifier.fillMaxHeight(), image = it
                )
            }
        }
    }
}