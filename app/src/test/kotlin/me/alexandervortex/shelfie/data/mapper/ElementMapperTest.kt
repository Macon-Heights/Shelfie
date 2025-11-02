package me.alexandervortex.shelfie.data.mapper

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import me.alexandervortex.shelfie.data.mapper.ElementMapperXmlTestData.toBody

class ElementMapperTest : BehaviorSpec({

    val mapper = ElementMapper()
    val binaries = ElementMapperXmlTestData.binaries()

    Given("a paragraph with text and image") {
        val body = ElementMapperXmlTestData.paragraphWithTextAndImage().toBody()
        val model = ElementMapperTestData.paragraphWithTextAndImage()

        When("test") {
            val result = mapper.map(body, binaries)

            Then("test") {
                result shouldBe model
            }
        }
    }
})