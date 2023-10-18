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

class PdfByggerAppTest {
    private val compileRequest = this::class.java.classLoader.getResource("pdfbygger-request.json")!!.readText()

    @Test
    fun appRuns() {
        testApplication {
            val response = client.get("/isAlive")
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun `app can compile latex to pdf`() {
        testApplication {
            appConfig(getScriptPath("simpleCompile.sh"))

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
            appConfig(getScriptPath("neverEndingCompile.sh"), 1)

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
        val parallelism = Runtime.getRuntime().availableProcessors()
        val compileTime = 1

        runBlocking {
            testApplication {
                appConfig("${getScriptPath("compileInSeconds.sh")} $compileTime", compileTime * 2 + 1)

                val requests = (0..(parallelism * parallelismFactor)).map {
                    async(Dispatchers.Default) {
                        client.post("/compile") {
                            contentType(ContentType.Application.Json)
                            setBody(compileRequest)
                        }
                    }
                }

                val responses = requests.awaitAll()
                val successful = responses.filter { it.status == HttpStatusCode.OK }.size
                val timedOut = responses.filter { it.status == HttpStatusCode.InternalServerError }.size

                assertThat(successful, isWithin(IntRange(parallelism, parallelism + 1)))
                assertThat(timedOut, equalTo(requests.size - successful))
            }
        }
    }

    private fun ApplicationTestBuilder.appConfig(latexCommand: String, timeoutSeconds: Int = 1) =
        environment {
            config = ApplicationConfig(null).mergeWith(
                MapApplicationConfig(
                    "pdfBygger.latexCommand" to "/usr/bin/env bash $latexCommand",
                    "pdfBygger.compileTimeoutSeconds" to "$timeoutSeconds"
                )
            )
        }

}