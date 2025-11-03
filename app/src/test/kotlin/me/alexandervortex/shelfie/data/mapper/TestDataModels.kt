package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.ElementUI
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.TextStyle

object TestDataModels {

    fun elementsEmpty(): List<ElementUI> {
        return emptyList()
    }

    fun elementsText(): List<ElementUI> {
        return listOf(
            ElementUI.TextUI(
                listOf(StyledText(TextStyle.Normal, "sashka have keked 3 times"))
            ),
        )
    }
}