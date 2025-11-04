package me.alexandervortex.shelfie.data.mapper

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import me.alexandervortex.shelfie.base.transformerTest
import me.alexandervortex.shelfie.data.mapper.TestDataModels.elementsEmpty
import me.alexandervortex.shelfie.data.mapper.TestDataModels.elementsText
import me.alexandervortex.shelfie.data.mapper.TestDataXml.paragraphEmpty
import me.alexandervortex.shelfie.data.mapper.TestDataXml.paragraphWithText

class ElementMapperTest : BehaviorSpec({

    val mapper = ElementMapper()
    val binaries = TestDataXml.binaries()

    forAll(
        table(
            headers("xml", "model", "name"),
            row(paragraphEmpty(), elementsEmpty(), "elements empty"),
            row(paragraphWithText(), elementsText(), "elements text"),
//            row(paragraphWithFewTexts(), elementsFewTexts(), "elements text"),
        )
    ) { xml, model, name ->

        transformerTest(model, name) {
            mapper.map(xml, binaries)
        }
    }
})