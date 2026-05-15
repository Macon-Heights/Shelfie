package me.alexandervortex.shelfie.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.alexandervortex.shelfie.base.ext.clipNShadow
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.model.CatalogueItemUIModel
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.IC_CHECK
import me.alexandervortex.shelfie.ui.theme.IC_UNCHECK
import me.alexandervortex.shelfie.ui.theme.SHAPE_M

private const val ROOT_PADDINGS = 16
private const val SPACE_SYMBOL = " "

@Composable
fun CatalogueItemUI(
    isRemoveMode: Boolean,
    model: CatalogueItemUIModel,
    modifier: Modifier = Modifier,
) {
    val isBook = model is CatalogueItemUIModel.Model

    val animatedColor by rememberInfiniteTransition().animateColor(
        initialValue = getColors().surfaceVariant,
        targetValue = getColors().surfaceContainer,
        animationSpec = infiniteRepeatable(
            animation = tween(DefaultDurationMillis * 3, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color = if (isBook) {
        getColors().surfaceVariant
    } else {
        animatedColor
    }

    val onColor = getColors().onSurfaceVariant
    val onColorForTitle = getColors().onSurface

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isRemoveMode && model is CatalogueItemUIModel.Model) {
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
            modifier = modifier
                .clipNShadow(SHAPE_M)
                .then(modifier)
                .background(color)
                .padding(ROOT_PADDINGS.dp)
        ) {
            Text(
                text = (model as? CatalogueItemUIModel.Model)
                    ?.title.takeIf { isBook }
                    ?: SPACE_SYMBOL,
                color = onColorForTitle,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Spacer(Modifier.size(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val scrollIndex = (model as? CatalogueItemUIModel.Model)?.scrollIndex // 25
                val elements = (model as? CatalogueItemUIModel.Model)?.elements // 50

                if (scrollIndex != null && elements != null) {
                    val textValue = 100 / (elements / scrollIndex)
                    Text(
                        text = "$textValue%",
                        color = onColor,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Start
                    )
                }
                Text(
                    text = (model as? CatalogueItemUIModel.Model)
                        ?.author.takeIf { isBook }
                        ?: SPACE_SYMBOL,
                    color = onColor,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            Spacer(Modifier.size(8.dp))
            ProgressUI(
                (model as? CatalogueItemUIModel.Model)?.scrollIndex,
                (model as? CatalogueItemUIModel.Model)?.elements
            )
        }
    }
}

@Composable
@CombinedPreviews
fun BookComponentPreview() {
    val model = CatalogueItemUIModel.Model(
        id = "thisisid",
        localPath = "",
        title = "Harry Potter and the Sorcerer's Stone",
        author = "J.K. Rowling Rowling",
        year = "1001",
        scrollIndex = 50,
        elements = 365,
        isChecked = false,
    )
    val skeleton = CatalogueItemUIModel.Skeleton
    CombinedPreviews {
        CatalogueItemUI(true, model, Modifier)
    }
}