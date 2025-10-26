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
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.features.catalogue.ui.preview.CataloguePreviewData
import me.alexandervortex.shelfie.ui.component.BookComponent
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_S
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun ProgressLine(scrollIndex: Int, elements: Int) {
    if (scrollIndex > 0 && elements > 0 && scrollIndex <= elements) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(SHAPE_S)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(scrollIndex.toFloat())
                    .clip(SHAPE_S)
                    .background(getColors().primary)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight((elements - scrollIndex).toFloat())
                    .background(getColors().primaryContainer)
            )
        }
    }
}

@Composable
@CombinedPreviews
fun BookComponent2() {
    val model = CataloguePreviewData.getBooks().random()
    val kek = BookEntity(
        id = "thisisid",
        localPath = "",
        title = "Harry Potter and the Sorcerer's Stone",
        author = "J.K. Rowling Rowling",
        year = "1001",
        scrollIndex = 50,
        elements = 100
    )
    BookComponent(kek)
}