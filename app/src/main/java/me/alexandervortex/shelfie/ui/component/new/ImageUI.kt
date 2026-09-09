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
import me.alexandervortex.shelfie.model.BitmapImageModel
import me.alexandervortex.shelfie.model.ByteImageModel
import me.alexandervortex.shelfie.model.ImageModel
import me.alexandervortex.shelfie.ui.preview.BookPreviewFactory.getImages
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews

@Composable
fun ImageUI(
    modifier: Modifier = Modifier,
    model: ImageModel? = null,
) {

    val bitmap = when (model) {
        is BitmapImageModel -> model.image
        is ByteImageModel -> {
            model.image?.let { image ->
                remember(image) {
                    BitmapFactory.decodeByteArray(
                        image, 0, image.size
                    )
                }
            }
        }

        else -> null
    }

    bitmap?.let {
        Image(
            modifier = modifier,
            bitmap = bitmap.asImageBitmap(),
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
            val img = getImages().random()
            val bytes = remember(img) {
                context.resources.openRawResource(img).readBytes()
            }
            val model = ByteImageModel(bytes)
            ImageUI(model = model)
        }
    }
}
