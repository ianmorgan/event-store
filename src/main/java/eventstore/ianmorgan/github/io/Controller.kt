package eventstore.ianmorgan.github.io

import io.javalin.ApiBuilder
import io.javalin.Javalin

class Controller constructor(dao: EventDao) {
    private val theDao = dao

    fun register(app: Javalin) {
        app.routes {
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

            ApiBuilder.post("/events") { ctx ->

                println(ctx.body())
            }
        }
    }

    private fun buildPaging(events: List<Event>, filter: Filter): Map<String, Any> {
        val lastId = events.get(events.size - 1).id.toString()
        val more = if (filter.pageSize != null) (events.size == filter.pageSize) else false
        return mapOf("size" to events.size, "more" to more, "lastId" to lastId)
    }
}
