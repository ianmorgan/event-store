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
import org.jetbrains.spek.api.dsl.given
import org.json.JSONObject

@RunWith(JUnitPlatform::class)
object EventDaoSpec : Spek({

    given("A DAO") {

        val dao = EventDao()

        it("should return an empty list for a fresh DAO ") {
            assert.that(dao.events.size, equalTo(0))
        }

        it( "should load events from a given directory") {
            dao.load("src/test/resources/examples")
            assert.that(dao.events.size, equalTo(4)) // number of example events
            assert.that(dao.events[0].type, equalTo("SimpleEvent"))
            assert.that(dao.events[1].type, equalTo("PayloadEvent"))
            assert.that(dao.events[2].type, equalTo("AggregateEvent"))
            assert.that(dao.events[3].type, equalTo("SessionEvent"))
        }



    }
})
