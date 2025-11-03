package me.alexandervortex.shelfie.base

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

fun BehaviorSpec.transformerTest(
    value: Any,
    name: String,
    function: () -> Any,
) {
    Given(name) {
        When("do") {
            val result = function.invoke()
            Then("done") {
                result shouldBe value
            }
        }
    }
}