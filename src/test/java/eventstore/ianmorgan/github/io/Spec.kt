package eventstore.ianmorgan.github.io

import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import SampleCalculator
import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
object CalculatorSpec: Spek({
    given("a calculator") {
        val calculator = SampleCalculator()
        on("addition") {
            val sum = calculator.sum(2, 4)
            it("should return the result of adding the first number to the second number") {
                assert.that(sum, equalTo(6))
            }
        }
        on("subtraction") {
            val subtract = calculator.subtract(4, 2)
            it("should return the result of subtracting the second number asMap the first number") {
                assertEquals(2, subtract)
            }
        }
    }
})


fun assertMatch(result: MatchResult) {
    kotlin.test.assertEquals(MatchResult.Match, result)
}