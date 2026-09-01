package me.alexandervortex.shelfie.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastAny
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.base.ext.getStaticSurfaceVariant
import me.alexandervortex.shelfie.ui.preview.ViewerPreviewData.getBookDocument
import me.alexandervortex.shelfie.feature.viewer.ViewerUIFactory
import me.alexandervortex.shelfie.ui.preview.getTitleInfo
import me.alexandervortex.shelfie.model.ParsedBookModel
import me.alexandervortex.shelfie.model.ProgressBookModel
import me.alexandervortex.shelfie.model.ProgressModel
import me.alexandervortex.shelfie.ui.model.UI
import me.alexandervortex.shelfie.ui.preview.CombinedPreviews
import me.alexandervortex.shelfie.ui.theme.SHAPE_M

@Composable
fun SectionsUI(
    items: List<UI>,
    onDecline: () -> Unit,
) {
    val titles = items.filterIsInstance<UI.Heading>()
        .filter { it.content.parts.fastAll { it.text.isNotBlank() } }
        .filter { it.content.parts.fastAny { it.text.any { char -> char.isLetter() } } }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(getStaticSurfaceVariant().copy(alpha = 0.7f))
            .clickable { onDecline.invoke() }
            .padding(ROOT_PADDING.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .clip(SHAPE_M)
                .background(getColors().surface)
                .padding(BOX_PADDING.dp)
        ) {
            items(titles) { item ->
                Text(getStyledText(item.content, false, 0))
            }
        }
    }
}

@CombinedPreviews
@Composable
private fun SettingsPreview() {
    val factory = ViewerUIFactory()

    val parsedBookModel = ParsedBookModel(
        titleInfo = getTitleInfo(), document = getBookDocument()
    )
    val progressBookModel = ProgressBookModel(
        id = "", localPath = "", progress = ProgressModel(), book = parsedBookModel
    )
    val bookUI = factory.getBookUIModel(progressBookModel)

    CombinedPreviews {
        SectionsUI(bookUI?.elements.orEmpty()) {}
    }
}