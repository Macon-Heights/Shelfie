package me.alexandervortex.shelfie.ui.component.new

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFilterNotNull
import me.alexandervortex.shelfie.ui.model.BasicImage
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.preview.getImages

private const val GAP = 16

@Composable
fun CarouselImageUI(
    modifier: Modifier = Modifier,
    images: List<BasicImage?>? = null
) {
    images?.let {
        if (images.fastFilterNotNull().isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .then(modifier),
                horizontalArrangement = Arrangement.spacedBy(GAP.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                images.forEach {
                    ImageUI(
                        modifier = Modifier.fillMaxHeight(),
                        basicImage = it
                    )
                }
            }
        }
    }
}

@Composable
@CombinedPreviews
private fun Preview() {
    CombinedPreviews {
        val context = LocalContext.current
        val images = remember {
            getImages().map {
                BasicImage(context.resources.openRawResource(it).readBytes())
            }
        }.shuffled()
        CarouselImageUI(
            modifier = Modifier.height(128.dp),
            images = images
        )
    }
}