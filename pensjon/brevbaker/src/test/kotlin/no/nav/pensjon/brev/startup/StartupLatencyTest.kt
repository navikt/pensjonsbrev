package no.nav.pensjon.brev.startup

import com.sun.net.httpserver.HttpServer
import io.ktor.server.application.serverConfig
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevbakerTestModule
import no.nav.pensjon.brev.routing.OppvarmingKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.parallel.Isolated
import java.net.InetSocketAddress
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.io.path.readText
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource
import kotlin.time.measureTime

@Isolated
class StartupLatencyTest {
    @Test
    @Timeout(75)
    fun `cold startup and first requests stay within latency budgets`(@TempDir directory: Path) {
        val log = directory.resolve("startup.log")
        val process = ProcessBuilder(
            Path.of(System.getProperty("java.home"), "bin", "java").toString(),
            "-XX:ActiveProcessorCount=2",
            "-Xmx512m",
            "-cp",
            System.getProperty("java.class.path"),
            javaClass.name,
        ).redirectErrorStream(true).redirectOutput(log.toFile()).start()

        try {
            assertTrue(process.waitFor(60, TimeUnit.SECONDS)) { "Cold startup test timed out:\n${log.readText()}" }
            val output = log.readText()
            println(output)
            assertEquals(0, process.exitValue()) { "Cold startup test failed:\n$output" }
        } finally {
            if (process.isAlive) {
                process.destroyForcibly()
                process.waitFor(5, TimeUnit.SECONDS)
            }
        }
    }

    private fun verifyColdStartup() {
        val pdfRequests = AtomicInteger()
        val pdfBygger = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0).apply {
            createContext("/produserBrev") { exchange ->
                exchange.requestBody.use { it.readAllBytes() }
                pdfRequests.incrementAndGet()
                val response = """{"bytes":"JVBERi0xLjQKJSVFT0YK"}""".toByteArray()
                exchange.responseHeaders.add("Content-Type", "application/json")
                exchange.sendResponseHeaders(200, response.size.toLong())
                exchange.responseBody.use { it.write(response) }
            }
            start()
        }

        try {
            HttpClient.newBuilder().proxy(HttpClient.Builder.NO_PROXY).build().use { client ->
                val start = TimeSource.Monotonic.markNow()
                val server = embeddedServer(
                    Netty,
                    rootConfig = serverConfig(applicationEnvironment {
                        config = MapApplicationConfig(
                            "brevbaker.pdfByggerUrl" to "http://127.0.0.1:${pdfBygger.address.port}",
                            "brevbaker.pdfByggerScope" to "startup-test",
                            "brevbaker.unleash.useFakeUnleash" to "true",
                        )
                    }) {
                        developmentMode = true
                        module { brevbakerTestModule() }
                    },
                    configure = { connector { host = "127.0.0.1"; port = 0 } },
                )

                try {
                    server.start(wait = false)
                    val port = runBlocking { server.engine.resolvedConnectors().single().port }
                    val baseUrl = "http://127.0.0.1:$port"
                    val readyRequest = HttpRequest.newBuilder(URI("$baseUrl/isReady"))
                        .timeout(Duration.ofSeconds(5)).GET().build()
                    do {
                        val status = client.send(readyRequest, HttpResponse.BodyHandlers.discarding()).statusCode()
                        assertTrue(status == 200 || status == 503) { "Unexpected readiness status: $status" }
                        assertTrue(start.elapsedNow() < STARTUP_BUDGET) { "Cold startup exceeded $STARTUP_BUDGET" }
                        if (status == 200) break
                        Thread.sleep(25)
                    } while (true)
                    println("Cold startup: ${start.elapsedNow()} (budget: $STARTUP_BUDGET)")
                    val oppvarming = checkNotNull(server.application.attributes.getOrNull(OppvarmingKey)) {
                        "Startup warmup was not registered"
                    }
                    assertTrue(oppvarming.ferdig)
                    assertEquals(emptySet<String>(), oppvarming.feiledeSteg)

                    val body = javaClass.getResource("/startup-request.json")!!.readText()
                    listOf(
                        "/letter/autobrev/json",
                        "/v2/letter/autobrev/json",
                        "/letter/autobrev/pdf",
                    ).forEach { path ->
                        val request = HttpRequest.newBuilder(URI("$baseUrl$path"))
                            .timeout(Duration.ofSeconds(5))
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build()
                        val elapsed = measureTime {
                            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
                            assertEquals(200, response.statusCode()) { "$path: ${response.body()}" }
                            assertTrue(response.body().isNotBlank()) { "$path returned an empty response" }
                        }
                        println("First $path: $elapsed (budget: $FIRST_REQUEST_BUDGET)")
                        assertTrue(elapsed < FIRST_REQUEST_BUDGET) {
                            "First $path took $elapsed, exceeding $FIRST_REQUEST_BUDGET; check ObjectMapper warmup"
                        }
                    }
                    assertEquals(1, pdfRequests.get())
                } finally {
                    server.stop(0, 0)
                }
            }
        } finally {
            pdfBygger.stop(0)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            StartupLatencyTest().verifyColdStartup()
        }

        private val STARTUP_BUDGET = 15.seconds
        private val FIRST_REQUEST_BUDGET = 1500.milliseconds
    }
}
