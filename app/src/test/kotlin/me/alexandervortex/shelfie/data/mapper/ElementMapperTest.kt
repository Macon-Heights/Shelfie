package me.alexandervortex.shelfie.data.mapper

import io.kotest.core.spec.style.BehaviorSpec
import me.alexandervortex.shelfie.base.transformerTest
import me.alexandervortex.shelfie.data.mapper.TestDataXml.toBody

class ElementMapperTest : BehaviorSpec({

    val mapper = ElementMapper()
    val binaries = TestDataXml.binaries()


    transformerTest(TestDataModels.emptyElementList()) {
        val body = TestDataXml.emptyParagraph().toBody()
        mapper.map(body, binaries)
    }
})