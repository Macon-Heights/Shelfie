package me.alexandervortex.shelfie.ui.component.viewerui

import androidx.compose.runtime.Composable
import me.alexandervortex.shelfie.ui.model.BlockUi

@Composable
fun BlockComponent(
    model: BlockUi,
) {
    when (model) {
        is BlockUi.Subtitle -> SubtitleComponent(model)
        is BlockUi.Cite -> CiteComponent(model)
        BlockUi.EmptyLine -> EmptyLineComponent(model as? BlockUi.EmptyLine)
        is BlockUi.Image -> ImageComponent(model)
        is BlockUi.Paragraph -> ParagraphComponent(model)
        is BlockUi.Poem -> PoemComponent(model)
        is BlockUi.Table -> TableComponent(model)
    }
}