package eventstore.ianmorgan.github.io

import io.javalin.ApiBuilder
import io.javalin.Javalin
import java.util.*

class Controller {

    fun register(app : Javalin){
        app.routes {
            ApiBuilder.get("/events") { ctx ->
                ctx.json(Arrays.asList(
                    Event.ModelMapper.asMap(Event(type = "SimpleEvent")),
                    Event.ModelMapper.asMap(Event(type = "AggregateEvent", aggregateId = "123"))
                ))
            }
        }

    }
}
