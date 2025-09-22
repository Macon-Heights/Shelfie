package me.alexandervortex.shelfie.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.data.db.entiry.BookUri
import me.alexandervortex.shelfie.features.catalogue.BookPreviewFactory

@Composable
fun BookComponent(
    model: BookUri,
    modifier: Modifier = Modifier,
) {
    val color = MaterialTheme.colorScheme.surfaceVariant
    val onColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(16.dp)
    ) {
        Text(
            model.title,
            color = onColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        model.author?.let {
            Spacer(Modifier.size(8.dp))
            Text(
                color = onColor,
                fontWeight = FontWeight.Light,
                text = (it),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
@Preview(widthDp = 180)
fun BookComponent() {
    val model = BookPreviewFactory.getBooks().random()
    BookComponent(model)
}