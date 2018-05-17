package eventstore.ianmorgan.github.io

import io.javalin.Javalin
import io.javalin.ApiBuilder.*


fun main(args: Array<String>) {
    JavalinApp(7000).init()
}

class JavalinApp(private val port: Int) {

    fun init(): Javalin {


        val app = Javalin.create().apply {
            port(port)
            //exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("not found") }
        }.start()

        app.routes {



        }

        // setup the  main controller
        val eventDao = EventDao()
        eventDao.load("src/test/resources/examples")

        val controller = Controller(eventDao)
        controller.register(app)

        //JavalinJacksonPlugin.configure()

        return app

    }
}
