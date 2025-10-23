package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
fun ServiceStateComponent(serviceState: MediaServiceState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(SHAPE_S)
            .background(getColors().surface)
            .padding(8.dp)
    ) {
        Column {
            Text("isPlaying = ${serviceState.isPlaying}")
            Text(" ")
            Text("index = ${serviceState.index}")
            Text("part = ${serviceState.part}")
        }
        Spacer(Modifier.weight(1f))
        Column {
            Text("title = ${serviceState.title}")
            Text("author = ${serviceState.author}")
            Text(" ")
            Text("error = ${serviceState.error}")
        }
    }
}

@Preview
@Composable
fun ServiceStateComponentPreview() {
    val state = MediaServiceState.playingState()
    ServiceStateComponent(state)
}