package me.alexandervortex.shelfie.ui.component

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.features.catalogue.ui.preview.CataloguePreviewData
import me.alexandervortex.shelfie.features.mediaviewer.ProgressLine
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_M
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun BookComponent(
    model: BookEntity,
    modifier: Modifier = Modifier,
) {
    val color = getColors().surfaceVariant
    val onColor = getColors().onSurface
    val onColorForTitle = getColors().onSurface
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
                model.title,
                color = onColorForTitle,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Spacer(
                Modifier
                    .size(16.dp)
                    .weight(1f)
            )
            model.author?.let {
                Text(
                    color = onColor,
                    fontWeight = FontWeight.Light,
                    text = it,
                    textAlign = TextAlign.End
                )
            }

        }
        if (model.scrollIndex > 0) {
            Spacer(Modifier.size(8.dp))
            ProgressLine()
        }
    }
}

@Composable
@CombinedPreviews
fun BookComponent() {
    val model = CataloguePreviewData.getBooks().random()
    val kek = BookEntity(
        id = "thisisid",
        localPath = "",
        title = "Harry Potter and the Sorcerer's Stone",
        author = "J.K. Rowling Rowling",
        year = "1001",
        scrollIndex = 46
    )
    BookComponent(kek)
}