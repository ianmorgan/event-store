package eventstore.ianmorgan.github.io

import io.javalin.ApiBuilder
import io.javalin.Javalin
import org.json.JSONArray

class Controller constructor(dao: EventDao) {
    private val theDao = dao

    fun register(app: Javalin) {
        app.routes {

//            ApiBuilder.get ("/status") {
//                println ("in status")
//                it.result("Running!")
//            }

            ApiBuilder.get("/events") { ctx ->

                // run the query
                val filter = Filter.ModelMapper.fromQueryMap(ctx.queryParamMap())
                val events = theDao.retrieve(filter)

                // build the result
                val result = HashMap<String, Any>()
                result["events"] = events.map { it -> Event.ModelMapper.asMap(it) }
                if (filter.pageSize != null || filter.lastId != null) {
                    result["paging"] = buildPaging(events, filter)
                }
                ctx.json(mapOf("payload" to result))
            }


            // TODO - this is a tempporary work aorund
            //        to support the find functionality in the doc-store
            //        and needs to be replaced by a better indexer
            ApiBuilder.get("/aggregates") { ctx ->

                // run the query
                val filter = Filter()
                val events = theDao.retrieve(filter)

                // build the result
                val aggregates = HashSet<String>()
                for (ev in events){
                    if (ev.aggregateId != null){
                        aggregates.add(ev.aggregateId)
                    }
                }

                val result = HashMap<String, Any>()
                result["aggregates"] = aggregates
                ctx.json(mapOf("payload" to result))
            }

            ApiBuilder.post("/events") { ctx ->
                val json = JSONArray(ctx.body())

                // convert to Event objects
                val events = ArrayList<Event>(json.length())
                for (i in json.toList().indices ){
                    events.add(Event.ModelMapper.fromJSON(json.getJSONObject(i)))
                }

                // and store
                theDao.storeEvents(events)
            }
        }
    }

    private fun buildPaging(events: List<Event>, filter: Filter): Map<String, Any> {
        val lastId = events.get(events.size - 1).id.toString()
        val more = if (filter.pageSize != null) (events.size == filter.pageSize) else false
        return mapOf("size" to events.size, "more" to more, "lastId" to lastId)
    }
}
