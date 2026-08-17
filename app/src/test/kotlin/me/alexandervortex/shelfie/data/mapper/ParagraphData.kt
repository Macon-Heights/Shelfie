package me.alexandervortex.shelfie.data.mapper

import me.alexandervortex.shelfie.data.mapper.TestDataXml.toElement
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.ui.model.StyledText
import me.alexandervortex.shelfie.ui.model.TextStyleUIModel

object ParagraphData {

    fun paragraphEmpty() = """
        <body>
            <p>   </p>
        </body>
    """.trimIndent().toElement()

    fun paragraphEmptyModel() = emptyList<ElementUIModel>()

    fun paragraphWithText() = """
        <body>
            <p>sashka have keked 3 times</p>
        </body>
    """.trimIndent().toElement()

    fun paragraphWithTextModel(): List<ElementUIModel> {
        return listOf(
            ElementUIModel.TextUIModel(
                listOf(
                    StyledText(
                        setOf(),
                        "sashka have keked 3 times"
                    )
                )
            ),
        )
    }

    fun paragraphListOfText() = """
        <body>
            <p>
                He apiti atu ki enei <strong>Rarangi 3</strong> atu ki.
            </p>
        </body>
    """.toElement()

    fun paragraphListOfTextModel(): List<ElementUIModel.TextUIModel> {
        return listOf(
            ElementUIModel.TextUIModel(
                parts = listOf(
                    StyledText(
                        styles = emptySet(),
                        text = "He apiti atu ki enei "
                    ),
                    StyledText(styles = setOf(TextStyleUIModel.Bold), text = "Rarangi 3"),
                    StyledText(
                        styles = emptySet(),
                        text = " atu ki."
                    )
                )
            )
        )
    }
}