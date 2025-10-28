package me.alexandervortex.shelfie.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.features.catalogue.ui.preview.CataloguePreviewData
import me.alexandervortex.shelfie.features.mediaviewer.ProgressLine
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_CHECK
import me.alexandervortex.shelfie.ui.theme.IC_UNCHECK
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun BookComponent(
    isRemoveMode: Boolean,
    model: BookComponentModel,
    modifier: Modifier = Modifier,
) {
    val color = getColors().surfaceVariant
    val onColor = getColors().onSurfaceVariant
    val onColorForTitle = getColors().onSurface
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isRemoveMode) {
            Image(
                modifier = Modifier.size(32.dp),
                imageVector = if (model.isChecked) IC_CHECK else IC_UNCHECK,
                contentDescription = "",
                colorFilter = ColorFilter.tint(getColors().primary)
            )
            Spacer(Modifier.size(16.dp))
        }
        Column(
            modifier = modifier
                .shadow(
                    elevation = 6.dp,
                    shape = SHAPE_M,
                    clip = false
                )
                .clip(SHAPE_M)
                .background(color)
                .padding(16.dp)
        ) {
            model.title?.let {
                Text(
                    text = it,
                    color = onColorForTitle,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )
            }
            Spacer(Modifier.size(4.dp))
            model.author?.let {
                Text(
                    text = it,
                    color = onColor,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
            if (model.scrollIndex > 0) {
                Spacer(Modifier.size(8.dp))
                ProgressLine(model.scrollIndex, model.elements)
            }
        }
    }
}

@Composable
@CombinedPreviews
fun BookComponentPreview() {
    val model = CataloguePreviewData.getBooks().random()
    val kek = BookComponentModel(
        id = "thisisid",
        localPath = "",
        title = "Harry Potter and the Sorcerer's Stone",
        author = "J.K. Rowling Rowling",
        year = "1001",
        scrollIndex = 46,
        elements = 100,
        isChecked = false,
    )
    CombinedPreviews {
        BookComponent(false, kek, Modifier)
    }
}