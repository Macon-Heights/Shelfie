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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.clipNShadow
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.features.mediaviewer.ProgressLine
import me.alexandervortex.shelfie.ui.model.Bookable
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_CHECK
import me.alexandervortex.shelfie.ui.theme.IC_UNCHECK
import me.alexandervortex.shelfie.ui.theme.SHAPE_M

@Composable
fun BookComponent(
    isRemoveMode: Boolean,
    model: Bookable,
    modifier: Modifier = Modifier,
) {
    val color = getColors().surfaceVariant
    val onColor = getColors().onSurfaceVariant
    val onColorForTitle = getColors().onSurface
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isRemoveMode && model is Bookable.BookComponentModel) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clipNShadow(SHAPE_M)
                    .then(modifier)
                    .background(color),
                imageVector = if (model.isChecked) IC_CHECK else IC_UNCHECK,
                contentDescription = null,
                colorFilter = ColorFilter.tint(getColors().primary)
            )
            Spacer(Modifier.size(16.dp))
        }
        val isBook = model is Bookable.BookComponentModel

        Column(
            modifier = Modifier
                .clipNShadow(SHAPE_M)
                .then(modifier)
                .background(color)
                .padding(16.dp)
        ) {
            Text(
                text = (model as? Bookable.BookComponentModel)?.title.takeIf { isBook } ?: " ",
                color = onColorForTitle,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = (model as? Bookable.BookComponentModel)?.author.takeIf { isBook } ?: " ",
                color = onColor,
                fontWeight = FontWeight.Light,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            Spacer(Modifier.size(8.dp))
            ProgressLine(
                (model as? Bookable.BookComponentModel)?.scrollIndex,
                (model as? Bookable.BookComponentModel)?.elements
            )
        }
    }
}

@Composable
@CombinedPreviews
fun BookComponentPreview() {
    val model = Bookable.BookComponentModel(
        id = "thisisid",
        localPath = "",
        title = "Harry Potter and the Sorcerer's Stone",
        author = "J.K. Rowling Rowling",
        year = "1001",
        scrollIndex = 46,
        elements = 100,
        isChecked = false,
    )
    val skeleton = Bookable.BookComponentSkeleton(0)
    CombinedPreviews {
        BookComponent(true, skeleton, Modifier)
    }
}