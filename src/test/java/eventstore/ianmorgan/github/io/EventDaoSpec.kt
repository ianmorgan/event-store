package eventstore.ianmorgan.github.io

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.util.*

@RunWith(JUnitPlatform::class)
object EventDaoSpec : Spek({

    given("A DAO") {

        val dao = EventDao()

        it("should return an empty list for a fresh DAO ") {
            assert.that(dao.retrieve().size, equalTo(0))
        }

        it( "should load events from a given directory") {
            dao.load("src/test/resources/examples")

            assert.that(dao.retrieve().size, equalTo(4)) // number of example events
            assert.that(dao.retrieve()[0].type, equalTo("SimpleEvent"))
            assert.that(dao.retrieve()[1].type, equalTo("PayloadEvent"))
            assert.that(dao.retrieve()[2].type, equalTo("AggregateEvent"))
            assert.that(dao.retrieve()[3].type, equalTo("SessionEvent"))
        }

        it ("should filter by type"){
            val filtered = dao.retrieve(Filter (type = "SimpleEvent"))

            assert.that(filtered.size, equalTo(1))
            assert.that(filtered[0].type, equalTo("SimpleEvent"))
        }

        it ("should filter by aggregate id"){
            val filtered = dao.retrieve(Filter (aggregateId = "123"))

            assert.that(filtered.size, equalTo(1))
            assert.that(filtered[0].type, equalTo("AggregateEvent"))
        }

        it ("should filter by session id"){
            val filtered = dao.retrieve(Filter (sessionId = "session#564ghsdgd5bncfz"))

            assert.that(filtered.size, equalTo(1))
            assert.that(filtered[0].type, equalTo("SessionEvent"))
        }

        it ("should only return events after the lastId"){
            val filtered = dao.retrieve(Filter (lastId = UUID.fromString("bed6a10c-ab5a-48bc-9129-60842fe10fd9")))

            assert.that(filtered.size, equalTo(2))
            assert.that(filtered[0].type, equalTo("AggregateEvent"))
            assert.that(filtered[1].type, equalTo("SessionEvent"))
        }

        it ("should return an empty list if lastId on last event "){
            val filtered = dao.retrieve(Filter (lastId = UUID.fromString("08ec6bfa-b167-43f3-bd26-f2498fa2e291")))
            assert.that(filtered.isEmpty(), equalTo(true))
        }

        it ("should limit results by pageSize") {
            val filtered = dao.retrieve(Filter (pageSize = 2 as Integer))

            assert.that(filtered.size, equalTo(2))
            assert.that(filtered[0].type, equalTo("SimpleEvent"))
            assert.that(filtered[1].type, equalTo("PayloadEvent"))
        }

//        fun loadExamples(){
//            dao.truncate()
//            dao.load("src/test/resources/examples")
//        }

    }
})
