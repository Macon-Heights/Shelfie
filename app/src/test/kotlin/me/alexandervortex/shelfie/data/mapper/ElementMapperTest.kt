package me.alexandervortex.shelfie.data.mapper

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import me.alexandervortex.shelfie.base.transformerTest
import me.alexandervortex.shelfie.data.mapper.TestDataModels.emptyElementList
import me.alexandervortex.shelfie.data.mapper.TestDataXml.emptyParagraph
import me.alexandervortex.shelfie.data.mapper.TestDataXml.toBody

class ElementMapperTest : BehaviorSpec({

    val mapper = ElementMapper()
    val binaries = TestDataXml.binaries()

    forAll(
        table(
            headers("xml", "model"),
            row(emptyParagraph().toBody(), emptyElementList())
        )
    ) { xml, model ->
        transformerTest(model) {
            mapper.map(xml, binaries)
        }
    }
})