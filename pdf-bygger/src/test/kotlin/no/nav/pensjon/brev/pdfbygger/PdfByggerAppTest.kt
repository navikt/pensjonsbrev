package no.nav.pensjon.brev.pdfbygger

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class PdfByggerAppTest {
    private val compileRequest = this::class.java.classLoader.getResource("pdfbygger-request.json")!!.readText()

    @Test
    fun appRuns() {
        testApplication {
            appConfig()

            val response = client.get("/isAlive")
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun `app can compile latex to pdf`() {
        testApplication {
            appConfig(latexCommand = getScriptPath("simpleCompile.sh"), parallelism = 1, compileTimeout = 1.seconds, queueTimeout = 1.seconds)

            val response = client.post("/compile") {
                contentType(ContentType.Application.Json)
                setBody(compileRequest)
            }
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun `compile times out when exceeding timeout`() {
        testApplication {
            appConfig(latexCommand = getScriptPath(name = "neverEndingCompile.sh"), parallelism = 1, compileTimeout = 1.seconds, queueTimeout = 1.seconds)

            val response = client.post("/compile") {
                contentType(ContentType.Application.Json)
                setBody(compileRequest)
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
            assertThat(response.bodyAsText(), containsSubstring("Compilation timed out"))
        }
    }

    @Test
    fun `compile enforces max limit of parallel latex processes`() {
        val parallelismFactor = 10
        val parallelism = 2

        runBlocking {
            testApplication {
                appConfig(
                    latexCommand = "${getScriptPath("compileInSeconds.sh")} 0.1",
                    parallelism = parallelism,
                    // 100 * 2: two runs, 500: allow for some wiggle room
                    compileTimeout = (500 + 100 * 2).milliseconds,
                    // ensure that all other compilations time out
                    queueTimeout = 10.milliseconds,
                )

                val requests = List(parallelism * parallelismFactor) {
                    async(Dispatchers.Default) {
                        client.post("/compile") {
                            contentType(ContentType.Application.Json)
                            setBody(compileRequest)
                        }
                    }
                }

                val responses = requests.awaitAll()
                val successful = responses.filter { it.status == HttpStatusCode.OK }
                val queueTimedOut = responses.filter { it.status == HttpStatusCode.ServiceUnavailable }

                assertThat(successful, hasSize(isWithin(IntRange(parallelism, parallelism * 2))))
                assertThat(queueTimedOut, hasSize(equalTo(requests.size - successful.size)))
            }
        }
    }

    private fun ApplicationTestBuilder.appConfig(latexCommand: String? = null, parallelism: Int? = null, compileTimeout: Duration? = null, queueTimeout: Duration? = null) =
        environment {
            val overrides = listOfNotNull(
                latexCommand?.let { "pdfBygger.latex.latexCommand" to "/usr/bin/env bash $it" },
                parallelism?.let { "pdfBygger.latex.latexParallelism" to "$it" },
                compileTimeout?.let { "pdfBygger.latex.compileTimeout" to "$it" },
                queueTimeout?.let { "pdfBygger.latex.compileQueueWaitTimeout" to "$it" },
                "pdfBygger.latex.compileTmpDir" to "/tmp",
            )
            config = ApplicationConfig(null).mergeWith(MapApplicationConfig(overrides))
        }

}