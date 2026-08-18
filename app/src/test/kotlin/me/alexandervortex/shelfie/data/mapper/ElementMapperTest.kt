package me.alexandervortex.shelfie.data.mapper

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import me.alexandervortex.shelfie.base.transformerTest
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphEmpty
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphEmptyModel
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphWithText
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphWithTextModel
import me.alexandervortex.shelfie.data.mapper.ParagraphData.problemPart
import me.alexandervortex.shelfie.data.mapper.ParagraphData.problemPartModel

class ElementMapperTest : BehaviorSpec({

    val mapper = ElementMapper()
    val binaries = TestDataXml.binaries()

    forAll(
        table(
            headers("xml", "model", "name"),
            row(paragraphEmpty(), paragraphEmptyModel(), "paragraph empty"),
            row(paragraphWithText(), paragraphWithTextModel(), "paragraph with text"),
            row(problemPart(), problemPartModel(), "paragraph with text"),
//            row(paragraphListOfText(), paragraphListOfTextModel(), "paragraph with text"),
        )
    ) { xml, model, name ->

        transformerTest(model, name) {
            mapper.map(xml, binaries)
        }
    }
})