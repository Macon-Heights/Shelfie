package me.alexandervortex.shelfie.features.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.theme.SHAPE_S
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun ServiceStateComponent(serviceState: ServiceState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(SHAPE_S)
            .background(getColors().surface)
            .padding(8.dp)
    ) {
        Text("isPlaying = ${serviceState.isPlaying}")
        Text("isScrollable = ${serviceState.isScrollable}")
        Text("error = ${serviceState.error}")
        Text("index = ${serviceState.index}")
        Text("part = ${serviceState.part}")
    }
}

@Preview
@Composable
fun ServiceStateComponentPreview() {
    val state = ServiceState.playingState()
    ServiceStateComponent(state)
}