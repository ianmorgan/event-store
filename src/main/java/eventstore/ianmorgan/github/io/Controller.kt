package eventstore.ianmorgan.github.io

import io.javalin.ApiBuilder
import io.javalin.Javalin
import java.util.*

class Controller constructor( dao : EventDao ){
    val theDao = dao

    fun register(app : Javalin){
        app.routes {
            ApiBuilder.get("/events") { ctx ->
                ctx.json(theDao.events.map { it -> Event.ModelMapper.asMap(it) })
            }

            ApiBuilder.post("/events") { ctx ->
               println(ctx.body())
            }
        }

    }
}
