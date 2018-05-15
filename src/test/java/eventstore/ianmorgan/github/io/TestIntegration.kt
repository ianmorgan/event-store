package eventstore.ianmorgan.github.io

import io.javalin.Javalin
import junit.framework.TestCase
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class TestIntegration : TestCase() {

    private lateinit var app: Javalin
    private val url = "http://localhost:8000/"

    override fun setUp() {
        app = JavalinApp(8000).init()
    }

    override fun tearDown() {
        app.stop()
    }

    fun testDummy() {
        assertEquals(1, 2)
    }
}