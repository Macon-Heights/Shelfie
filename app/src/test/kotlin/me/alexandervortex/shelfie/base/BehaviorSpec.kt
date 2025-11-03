package me.alexandervortex.shelfie.base

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

fun <T> BehaviorSpec.transformerTest(
    expected: T,
    name: String,
    function: () -> T,
) {
    Given(name) {
        When("transform") {
            val actual = function.invoke()
            Then("compare result") {
                actual shouldBe expected
            }
        }
    }
}