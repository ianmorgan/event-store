package eventstore.ianmorgan.github.io

import io.javalin.Javalin
import junit.framework.TestCase
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class TestIntegration : TestCase() {

    private lateinit var app: Javalin
    private val url = "http://localhost:8000/"

    override fun setUp() {
        val parser = DefaultParser()
        val cmd = parser.parse(Options(), arrayOf<String>())
        app = JavalinApp(8000,cmd).init()
    }

    override fun tearDown() {
        app.stop()
    }

    fun testDummy() {
        assertEquals(1, 2)
    }
}