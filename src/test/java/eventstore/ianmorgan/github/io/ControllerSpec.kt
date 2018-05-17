package eventstore.ianmorgan.github.io

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import io.javalin.Javalin
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.json.JSONObject
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

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

            it("should return all events if no filters") {
                val response = khttp.get(url = baseUrl + "events")
                assert.that(response.statusCode, equalTo(200))

                val expectedJson = """
                    {"payload":[
                        {"creator":"test","id":"4778a3ef-a920-4323-bc34-b87aa0bffb41","type":"SimpleEvent","timestamp":1509618174218},
                        {"creator":"test","id":"bed6a10c-ab5a-48bc-9129-60842fe10fd9","type":"PayloadEvent","timestamp":1509618174218},
                        {"creator":"test","aggregateId":"123","id":"db857426-4be7-4c1a-99df-10b2ed13dd02","type":"AggregateEvent","timestamp":1509618174218},
                        {"creator":"test","id":"08ec6bfa-b167-43f3-bd26-f2498fa2e291","sessionId":"session#564ghsdgd5bncfz","type":"SessionEvent","timestamp":1509618174218}
                    ]}
"""
                val actualAsMap = JsonToMap.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonToMap.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }

            it("should filter by type") {
                val response = khttp.get(url = baseUrl + "events?type=SimpleEvent")
                assert.that(response.statusCode, equalTo(200))

                // assert JSON
                val expectedJson = """
                    {"payload":[
                        {"creator":"test","id":"4778a3ef-a920-4323-bc34-b87aa0bffb41","type":"SimpleEvent","timestamp":1509618174218},
                    ]}
"""
                val actualAsMap = JsonToMap.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonToMap.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }

            it("should filter by aggregateId") {
                val response = khttp.get(url = baseUrl + "events?aggregateId=123")
                assert.that(response.statusCode, equalTo(200))

                // assert JSON
                val expectedJson = """
                    {"payload":[
                        {"creator":"test","aggregateId":"123","id":"db857426-4be7-4c1a-99df-10b2ed13dd02","type":"AggregateEvent","timestamp":1509618174218},
                    ]}
"""
                val actualAsMap = JsonToMap.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonToMap.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }

            it("should limit results by pageSize") {
                val response = khttp.get(url = baseUrl + "events?pageSize=2")
                assert.that(response.statusCode, equalTo(200))

                println (response.text)

                // assert JSON
                val expectedJson = """
                    {"payload":[
                        {"creator":"test","id":"4778a3ef-a920-4323-bc34-b87aa0bffb41","type":"SimpleEvent","timestamp":1509618174218},
                        {"creator":"test","id":"bed6a10c-ab5a-48bc-9129-60842fe10fd9","type":"PayloadEvent","timestamp":1509618174218},
                        ],
                    "pageing" : {
                        "more" : true,
                        "size" : 2,
                        "lastId" : "bed6a10c-ab5a-48bc-9129-60842fe10fd9"}
                    }
"""
                val actualAsMap = JsonToMap.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonToMap.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }
        }

        it("POST /events ") {

            val url = baseUrl + "events"
            val payload = mapOf("some" to "data")

            val response = khttp.post(url, data = JSONObject(payload))
            //val response = khttp.post(url = baseUrl + "missing/url")
            assert.that(response.statusCode, equalTo(200))
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
