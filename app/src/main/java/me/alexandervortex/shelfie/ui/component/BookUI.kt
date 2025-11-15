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
import me.alexandervortex.shelfie.ui.model.CatalogueItemUI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_CHECK
import me.alexandervortex.shelfie.ui.theme.IC_UNCHECK
import me.alexandervortex.shelfie.ui.theme.SHAPE_M

private const val ROOT_PADDINGS = 16
private const val SPACE_SYMBOL = " "

@Composable
fun BookUI(
    isRemoveMode: Boolean,
    model: CatalogueItemUI,
    modifier: Modifier = Modifier,
) {
    val isBook = model is CatalogueItemUI.Model

    val color = if (isBook) {
        getColors().surfaceVariant
    } else {
        getColors().surfaceContainer
    }
    val onColor = getColors().onSurfaceVariant
    val onColorForTitle = getColors().onSurface

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isRemoveMode && model is CatalogueItemUI.Model) {
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

        Column(
            modifier = Modifier
                .clipNShadow(SHAPE_M)
                .then(modifier)
                .background(color)
                .padding(ROOT_PADDINGS.dp)
        ) {
            Text(
                text = (model as? CatalogueItemUI.Model)
                    ?.title.takeIf { isBook }
                    ?: SPACE_SYMBOL,
                color = onColorForTitle,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = (model as? CatalogueItemUI.Model)
                    ?.author.takeIf { isBook }
                    ?: SPACE_SYMBOL,
                color = onColor,
                fontWeight = FontWeight.Light,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            Spacer(Modifier.size(8.dp))
            ProgressUI(
                (model as? CatalogueItemUI.Model)?.scrollIndex,
                (model as? CatalogueItemUI.Model)?.elements
            )
        }
    }
}

@Composable
@CombinedPreviews
fun BookComponentPreview() {
    val model = CatalogueItemUI.Model(
        id = "thisisid",
        localPath = "",
        title = "Harry Potter and the Sorcerer's Stone",
        author = "J.K. Rowling Rowling",
        year = "1001",
        scrollIndex = 46,
        elements = 100,
        isChecked = false,
    )
    val skeleton = CatalogueItemUI.Skeleton(0)
    CombinedPreviews {
        BookUI(true, skeleton, Modifier)
    }
}