package eventstore.ianmorgan.github.io

import com.natpryce.hamkrest.equalTo
import io.javalin.Javalin
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import  com.natpryce.hamkrest.assertion.assert

@RunWith(JUnitPlatform::class)
object ContollerSpec : Spek({

    lateinit var app: Javalin
    val url = "http://localhost:8000/"

    describe("the controller") {
        beforeGroup {
            app = JavalinApp(8000).init()
            println("app started...")
        }

        beforeEachTest {
            // todo
        }

        context("a nested group") {

            beforeEachTest {
                //todo
            }

            beforeEachTest {
                //todo
            }

            it("should work") { 1 == 1 }
        }

        it("should return 404 for unknown urls") {
            val response = khttp.get(url = url + "/missing/url")
            assert.that(response.statusCode, equalTo(404))
        }

        afterEachTest {
            //todo
        }

        afterGroup {
            app.stop()
            println("app stopped")
        }
    }
})
