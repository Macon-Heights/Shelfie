package me.alexandervortex.shelfie.features.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.features.mvi.catalogue.preview.CataloguePreviewData
import me.alexandervortex.shelfie.ui.component.BookComponent
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_S

@Composable
fun ProgressLine(scrollIndex: Int, elements: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(SHAPE_S)
    ) {
        if (scrollIndex > 0 && elements > 0 && scrollIndex <= elements) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(scrollIndex.toFloat())
                    .clip(SHAPE_S)
                    .background(getColors().onSurfaceVariant)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight((elements - scrollIndex).toFloat())
                    .background(getColors().onSurfaceVariant.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
@CombinedPreviews
private fun BookComponentWithLine() {
    val model = CataloguePreviewData.getBooks().random()
    val kek = model.copy(
        scrollIndex = 20,
        elements = 100
    )
    CombinedPreviews {
        BookComponent(false, kek)
    }
}