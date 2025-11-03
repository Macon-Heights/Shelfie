package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.ui.model.ElementUI

object TestDataModels {

    fun elementsEmpty(): List<ElementUI> {
        return emptyList()
    }

    fun elementsText(): List<ElementUI> {
        return listOf(
            ElementUI.TextUI(listOf("sashka have keked 3 times")),
        )
    }
}