package me.alexandervortex.shelfie.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastAny
import me.alexandervortex.shelfie.base.ext.getColors
import me.alexandervortex.shelfie.ui.model.UI
import me.alexandervortex.shelfie.ui.theme.SHAPE_M

@Composable
fun SectionsUI(
    items: List<UI>,
) {
    val titles = items.filterIsInstance<UI.Heading>()
        .filter { it.content.parts.fastAll { it.text.isNotBlank() } }
        .filter { it.content.parts.fastAny { it.text.any { char -> char.isLetter() } } }

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
