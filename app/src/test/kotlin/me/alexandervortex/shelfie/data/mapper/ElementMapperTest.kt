package me.alexandervortex.shelfie.data.mapper

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import me.alexandervortex.shelfie.base.transformerTest
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphComplex
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphComplexEpub
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphComplexEpubModel
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphComplexModel
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphEmpty
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphEmptyModel
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphWithText
import me.alexandervortex.shelfie.data.mapper.ParagraphData.paragraphWithTextModel

class ElementMapperTest : BehaviorSpec({

    val mapper = ElementMapper()
    val binaries = TestDataXml.binaries()

    forAll(
        table(
            headers("xml", "model", "name"),
            row(paragraphEmpty(), paragraphEmptyModel(), "paragraph empty"),
            row(paragraphWithText(), paragraphWithTextModel(), "paragraph with text"),
            row(paragraphComplex(), paragraphComplexModel(), "paragraph complex"),
            row(paragraphComplexEpub(), paragraphComplexEpubModel(), "paragraph complex"),
        )
    ) { xml, model, name ->

        transformerTest(model, name) {
            mapper.map(xml, binaries)
        }
    }
})