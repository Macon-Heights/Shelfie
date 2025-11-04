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
                listOf(
                    StyledText(
                        setOf(),
                        "sashka have keked 3 times"
                    )
                )
            ),
        )
    }

    fun elementsFewTexts(): List<ElementUI.TextUI> {
        return listOf(
            ElementUI.TextUI(
                listOf(
                    StyledText(
                        setOf(),
                        "He apiti atu ki enei, kaua e meinga hei ritenga wehewehe te mea i whakakaupapatia na runga i nga whakahaere ture, i nga mana whanui ranei o te ao kua whakawhiwhia ki tetahi whenua ki tetahi wahanga whenua ranei no reira nei tetahi tangata, ahakoa taua wahanga whenua he whai mana motuhake, kei raro ranei i te Kaitiakitanga, he takiwa whenua ranei kahore nei ona Mana Kawanatanga Motuhake, kei raro ranei i tetahi atu ritenga whakawhaiti i tona mana motuhake."
                    ),
                    StyledText(
                        setOf(TextStyle.Bold),
                        "Rarangi 3"
                    )
                )
            )
        )
    }
}