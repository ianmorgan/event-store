package eventstore.ianmorgan.github.io

import io.javalin.ApiBuilder
import io.javalin.Javalin
import java.util.*

class Controller constructor(dao: EventDao) {
    private val theDao = dao

    fun register(app: Javalin) {
        app.routes {
            ApiBuilder.get("/events") { ctx ->

                val map = ctx.queryParamMap()
                if (ctx.queryParamMap().isEmpty()) {
                    val eventsMapped = theDao.retrieve().map { it -> Event.ModelMapper.asMap(it) }
                    ctx.json(mapOf("payload" to eventsMapped))
                }
                else {
                    val filter = Filter.ModelMapper.fromQueryMap(ctx.queryParamMap())
                    val eventsMapped = theDao.retrieve(filter).map { it -> Event.ModelMapper.asMap(it) }
                    if (filter.pageSize != null || filter.lastId != null) {
                        ctx.json(mapOf("payload" to eventsMapped, "pageing" to buildPaging(eventsMapped)))
                    }
                    else {
                        ctx.json(mapOf("payload" to eventsMapped))
                    }

                }


            }

            ApiBuilder.post("/events") { ctx ->
                println(ctx.body())
            }
        }
    }

    private fun buildPaging(events: List<Map<String, Any>>): Map<String, Any> {
        val size = events.size
        val lastId = events.get(events.size-1).get("id") as UUID
        return mapOf ( "size" to size, "more" to true, "lastId" to lastId.toString())
    }
}
