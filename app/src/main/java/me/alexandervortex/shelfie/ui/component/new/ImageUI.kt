package me.alexandervortex.shelfie.ui.component.new

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.preview.getImages

@Composable
fun ImageUI(
    modifier: Modifier = Modifier,
    image: ByteArray? = null,
) {
    val bitmap = image?.let {
        remember(image) {
            BitmapFactory.decodeByteArray(
                image, 0, image.size
            )?.asImageBitmap()
        }
    }

    bitmap?.let {
        Image(
            modifier = modifier,
            bitmap = bitmap,
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )
    }
}

@Composable
@CombinedPreviews
fun ImageUIPreview() {
    CombinedPreviews {
        Row {
            val context = LocalContext.current
            val img = getImages().get(1)
            val bytes = remember(img) {
                context.resources.openRawResource(img).readBytes()
            }
            ImageUI(image = bytes)
        }
    }
}
