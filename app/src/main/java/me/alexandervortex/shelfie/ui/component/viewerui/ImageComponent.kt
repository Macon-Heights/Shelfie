package me.alexandervortex.shelfie.ui.component.viewerui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import me.alexandervortex.shelfie.ui.model.BlockUi

@Composable
fun ImageComponent(block: BlockUi.Image) {
    val bitmap = remember(block.data) {
        BitmapFactory.decodeByteArray(block.data, 0, block.data.size)
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}