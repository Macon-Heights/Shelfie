package me.alexandervortex.shelfie.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import me.alexandervortex.shelfie.data.db.entiry.BookEntity
import me.alexandervortex.shelfie.features.catalogue.BookPreviewFactory

@Composable
fun BookComponent(
    model: BookEntity,
    modifier: Modifier = Modifier,
    navHostController: NavHostController? = null,
) {
    Column(
        modifier = modifier
            .sizeIn(minHeight = 180.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, Color(0xFF_777777), RoundedCornerShape(16.dp))
            .background(Color(0xFF_EAEAEA))
            .padding(8.dp)
    ) {
        Spacer(Modifier.weight(1f))
        Text(
            model.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        model.author?.let {
            Spacer(Modifier.size(16.dp))
            Text(
                text = (it),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
@Preview(showBackground = true, widthDp = 120, heightDp = 180)
fun BookComponent() {
    val model = BookPreviewFactory.getBooks().random()
    BookComponent(model)
}