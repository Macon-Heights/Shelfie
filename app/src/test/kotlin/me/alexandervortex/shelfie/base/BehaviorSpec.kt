package me.alexandervortex.shelfie.base

import io.kotest.assertions.withClue
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
                withClue(
                    """
                    |Expected:
                    |${expected.prettyPrint()}
                    |Actual:
                    |${actual.prettyPrint()}
                    |
                    """.trimMargin()
                ) {
                actual shouldBe expected
            }
        }
    }
}
}

private fun Any?.prettyPrint(): String {
    return when (this) {
        is List<*> -> this.joinToString(separator = ",\n") { "  $it" }.let { "[\n$it\n]" }
        else -> toString()
    }
}