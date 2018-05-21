package eventstore.ianmorgan.github.io

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import ianmorgan.github.io.jsonUtils.JsonHelper
import io.javalin.Javalin
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.json.JSONArray
import org.json.JSONObject
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
object ControllerSpec : Spek({

    lateinit var app: Javalin
    val baseUrl = "http://localhost:8000/"

    describe(" controller") {
        beforeGroup {

            val parser = DefaultParser()
            val cmd = parser.parse(Options(), arrayOf<String>())
            app = JavalinApp(8000, cmd).init()
            println("app started...")
        }

        beforeEachTest {
            // todo
        }

        context("GET /events specs") {
            beforeEachTest {}

            it("should return all events if no filters") {
                val response = khttp.get(url = baseUrl + "events")
                assert.that(response.statusCode, equalTo(200))

                val expectedJson = """
                    {"payload":{"events":[
                        {"creator":"test","id":"4778a3ef-a920-4323-bc34-b87aa0bffb41","type":"SimpleEvent","timestamp":1509618174218},
                        {"creator":"test","id":"bed6a10c-ab5a-48bc-9129-60842fe10fd9","type":"PayloadEvent","timestamp":1509618174218, "payload" : {"some": "data","array": [ 1,2,3],"can be nested" : { "more" : "data"},"message": "payload should be no more less than 10K minified JSON"}},
                        {"creator":"test","aggregateId":"123","id":"db857426-4be7-4c1a-99df-10b2ed13dd02","type":"AggregateEvent","timestamp":1509618174218},
                        {"creator":"test","id":"08ec6bfa-b167-43f3-bd26-f2498fa2e291","sessionId":"session#564ghsdgd5bncfz","type":"SessionEvent","timestamp":1509618174218}
                    ]}}
"""
                val actualAsMap = JsonHelper.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonHelper.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }

            it("should filter by type") {
                val response = khttp.get(url = baseUrl + "events?type=SimpleEvent")
                assert.that(response.statusCode, equalTo(200))

                // assert JSON
                val expectedJson = """
                    {"payload":{"events":[
                        {"creator":"test","id":"4778a3ef-a920-4323-bc34-b87aa0bffb41","type":"SimpleEvent","timestamp":1509618174218},
                    ]}}
"""
                val actualAsMap = JsonHelper.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonHelper.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }

            it("should filter by aggregateId") {
                val response = khttp.get(url = baseUrl + "events?aggregateId=123")
                assert.that(response.statusCode, equalTo(200))

                // assert JSON
                val expectedJson = """
                    {"payload":{"events":[
                        {"creator":"test","aggregateId":"123","id":"db857426-4be7-4c1a-99df-10b2ed13dd02","type":"AggregateEvent","timestamp":1509618174218},
                    ]}}
"""
                val actualAsMap = JsonHelper.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonHelper.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }

            it("should limit results by pageSize") {
                val response = khttp.get(url = baseUrl + "events?pageSize=2")
                assert.that(response.statusCode, equalTo(200))

                // assert JSON
                val expectedJson = """
                    {"payload":{"events":[
                        {"creator":"test","id":"4778a3ef-a920-4323-bc34-b87aa0bffb41","type":"SimpleEvent","timestamp":1509618174218},
                        {"creator":"test","id":"bed6a10c-ab5a-48bc-9129-60842fe10fd9","type":"PayloadEvent","timestamp":1509618174218, "payload" : {"some": "data","array": [ 1,2,3],"can be nested" : { "more" : "data"},"message": "payload should be no more less than 10K minified JSON"}},
                        ],
                    "paging" : {
                        "more" : true,
                        "size" : 2,
                        "lastId" : "bed6a10c-ab5a-48bc-9129-60842fe10fd9"}
                    }}
"""
                val actualAsMap = JsonHelper.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonHelper.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }

            it("should use lastId to determine offset") {
                val response = khttp.get(url = baseUrl + "events?lastId=db857426-4be7-4c1a-99df-10b2ed13dd02")
                assert.that(response.statusCode, equalTo(200))

                // assert JSON
                val expectedJson = """
                    {"payload":{"events":[
                        {"creator":"test","id":"08ec6bfa-b167-43f3-bd26-f2498fa2e291","sessionId":"session#564ghsdgd5bncfz","type":"SessionEvent","timestamp":1509618174218}
                        ],
                    "paging" : {
                        "more" : false,
                        "size" : 1,
                        "lastId" : "08ec6bfa-b167-43f3-bd26-f2498fa2e291"}
                    }}
"""
                val actualAsMap = JsonHelper.jsonToMap(response.jsonObject)
                val expectedAsMap = JsonHelper.jsonToMap(JSONObject(expectedJson))
                assert.that(expectedAsMap, equalTo(actualAsMap))
            }
        }

        context("POST /events specs") {
            it("should save new events ") {

                val url = baseUrl + "events"
                val payload = """
                [{
                    "id" : "bd0d452c-6c0c-4954-bb70-8bb49b4818d3",
                    "type" : "NewEvent",
                    "timestamp": 1526593840000,
                    "creator": "test"
                }]
"""
                // save event
                val response = khttp.post(url, data = JSONArray(payload))
                assert.that(response.statusCode, equalTo(200))

                // check it can be read back
                val readResponse = khttp.get(url = baseUrl + "events?type=NewEvent")
                val readCount = readResponse.jsonObject.getJSONObject("payload")
                    .getJSONArray("events").length()

                assert.that(readCount, equalTo(1))
            }

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
