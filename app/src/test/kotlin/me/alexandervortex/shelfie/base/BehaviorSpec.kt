package me.alexandervortex.shelfie.base

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

fun BehaviorSpec.transformerTest(
    value: Any,
    function: () -> Any,
) {
    val className = value.javaClass.name.split(".").last()
    Given("$className transform") {
        When("do") {
            val result = function.invoke()
            Then("done") {
                result shouldBe value
            }
        }
    }
}