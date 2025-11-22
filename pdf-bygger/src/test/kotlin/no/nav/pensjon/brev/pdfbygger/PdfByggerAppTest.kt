package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.*
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class PdfByggerAppTest {
    @OptIn(InterneDataklasser::class)
    private val pdfRequest = PDFRequest(
        letterMarkup = letterMarkup {
                title { text("Tittel 1") }
        },
        attachments = listOf(),
        language = LanguageCode.BOKMAL,
        brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
    )
    private val objectMapper = jacksonObjectMapper().apply { brevbakerConfig() }

    @Test
    fun appRuns() {
        testApplication {
            appConfig()

            val response = client.get("/isAlive")
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun `app can compile LetterMarkup to pdf`() {
        testApplication {
            appConfig(latexCommand = getScriptPath("simpleCompile.sh"), parallelism = 1, compileTimeout = 1.seconds, queueTimeout = 1.seconds)

            val response = client.post("/produserBrev") {
                contentType(ContentType.Application.Json)
                setBody(objectMapper.writeValueAsString(pdfRequest))
            }
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun `compile times out when exceeding timeout`() {
        testApplication {
            appConfig(latexCommand = getScriptPath(name = "neverEndingCompile.sh"), parallelism = 1, compileTimeout = 1.seconds, queueTimeout = 1.seconds)

            val response = client.post("/produserBrev") {
                contentType(ContentType.Application.Json)
                setBody(objectMapper.writeValueAsString(pdfRequest))
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
            assertThat(response.bodyAsText()).contains("Compilation timed out")
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
                        client.post("/produserBrev") {
                            contentType(ContentType.Application.Json)
                            setBody(objectMapper.writeValueAsString(pdfRequest))
                        }
                    }
                }

                val responses = requests.awaitAll()
                val successful = responses.filter { it.status == HttpStatusCode.OK }
                val queueTimedOut = responses.filter { it.status == HttpStatusCode.ServiceUnavailable }

                assertThat(successful).hasSizeBetween(parallelism, parallelism * 2)
                assertThat(queueTimedOut).hasSize(requests.size - successful.size)
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

fun ObjectMapper.brevbakerConfig() {
    registerModule(JavaTimeModule())
    registerModule(LetterMarkupModule)
    enable(SerializationFeature.INDENT_OUTPUT)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}