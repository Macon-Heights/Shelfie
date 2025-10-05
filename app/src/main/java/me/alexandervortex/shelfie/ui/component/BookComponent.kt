package me.alexandervortex.shelfie.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.features.catalogue.preview.BookPreviewFactory
import me.alexandervortex.shelfie.ui.theme.getColors

@Composable
fun BookComponent(
    model: BookEntity,
    modifier: Modifier = Modifier,
) {
    val color = getColors().surfaceVariant
    val onColor = getColors().onSurfaceVariant
    val onColorForTitle = getColors().onSurface

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
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
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            model.year?.let {
                Text(
                    color = onColor,
                    fontWeight = FontWeight.Light,
                    text = it,
                    textAlign = TextAlign.End
                )
            }
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
    }
}

@Composable
@Preview(widthDp = 180)
fun BookComponent() {
    val model = BookPreviewFactory.getBooks().random()
    BookComponent(model)
}