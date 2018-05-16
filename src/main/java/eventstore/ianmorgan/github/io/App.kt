package eventstore.ianmorgan.github.io

import io.javalin.Javalin
import io.javalin.ApiBuilder.*


fun main(args: Array<String>) {
    JavalinApp(7000).init()
}

class JavalinApp(private val port: Int) {

    fun init(): Javalin {


        val userDao = UserDao()

        val app = Javalin.create().apply {
            port(port)
            //exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("not found") }
        }.start()

        app.routes {

            get("/users") { ctx ->
                ctx.json(userDao.users)
            }

            get("/users/:id") { ctx ->
                ctx.json(userDao.findById(ctx.param("id")!!.toInt())!!)
            }

            get("/users/email/:email") { ctx ->
                ctx.json(userDao.findByEmail(ctx.param("email")!!)!!)
            }

            post("/users/create") { ctx ->
                val user = ctx.bodyAsClass(User::class.java)
                userDao.save(name = user.name, email = user.email)
                ctx.status(201)
            }

            patch("/users/update/:id") { ctx ->
                val user = ctx.bodyAsClass(User::class.java)
                userDao.update(
                    id = ctx.param("id")!!.toInt(),
                    user = user
                )
                ctx.status(204)
            }

            delete("/users/delete/:id") { ctx ->
                userDao.delete(ctx.param("id")!!.toInt())
                ctx.status(204)
            }

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
