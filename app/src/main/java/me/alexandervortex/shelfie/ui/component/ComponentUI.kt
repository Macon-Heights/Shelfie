package me.alexandervortex.shelfie.ui.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.ui.model.ElementUI

@Composable
fun ComponentUI(element: ElementUI) {
    when (element) {
        is ElementUI.TextUI -> Text(
            textAlign = TextAlign.Justify,
            text = element.text,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.background(Color.DarkGray)
        )

        is ElementUI.ImageUI -> {
            val bitmap = BitmapFactory.decodeByteArray(
                element.image, 0, element.image.size
            )
            if (bitmap != null) {
                Image(
                    contentScale = ContentScale.FillWidth,
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }

        is ElementUI.EmptyLine -> {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color.Red)
            )
        }

        is ElementUI.ContainerElementUI -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                element.elements.forEach { child ->
                    ComponentUI(child)
                }
            }
        }
    }
}