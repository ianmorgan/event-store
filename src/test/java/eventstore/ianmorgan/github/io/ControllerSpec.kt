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
object ControllerSpec : Spek({

    lateinit var app: Javalin
    val baseUrl = "http://localhost:8000/"

    describe(" controller") {
        beforeGroup {
            app = JavalinApp(8000).init()
            println("app started...")
        }

        beforeEachTest {
            // todo
        }

        context("GET /events group") {
            beforeEachTest {}
            //beforeEachTest {}

            it("should return all events") {
                val response = khttp.get(url = baseUrl +  "events")
                assert.that(response.statusCode, equalTo(999))

                println (response.text)

            }
        }

        it("GET /events ") {
            val response = khttp.get(url = baseUrl + "missing/url")
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
