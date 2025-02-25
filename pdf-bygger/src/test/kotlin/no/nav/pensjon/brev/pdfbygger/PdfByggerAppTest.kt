package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.*
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class PdfByggerAppTest {
    @OptIn(InterneDataklasser::class)
    private val pdfRequest = PDFRequest(
        letterMarkup = LetterMarkup(
            title = "Tittel 1",
            sakspart = LetterMarkup.SakspartImpl(
                gjelderNavn = "Navn Navnesen",
                gjelderFoedselsnummer = "12345678901",
                saksnummer = "123",
                dokumentDato = LocalDate.of(2025, 1, 1).format(DateTimeFormatter.ISO_LOCAL_DATE)
            ),
            blocks = listOf(),
            signatur = LetterMarkup.SignaturImpl(
                hilsenTekst = "hilsen",
                saksbehandlerRolleTekst = "saksbehandler",
                saksbehandlerNavn = "Saksbehandler Saksbehandlersen",
                attesterendeSaksbehandlerNavn = null,
                navAvsenderEnhet = "Nav sentralt",
            )
        ),
        attachments = listOf(),
        language = LanguageCode.BOKMAL,
        felles = Fixtures.felles,
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
                        client.post("/produserBrev") {
                            contentType(ContentType.Application.Json)
                            setBody(objectMapper.writeValueAsString(pdfRequest))
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
                latexCommand?.let { "pdfBygger.latexCommand" to "/usr/bin/env bash $it" },
                parallelism?.let { "pdfBygger.latexParallelism" to "$it" },
                compileTimeout?.let { "pdfBygger.compileTimeout" to "$it" },
                queueTimeout?.let { "pdfBygger.compileQueueWaitTimeout" to "$it" },
                "pdfBygger.compileTmpDir" to "/tmp",
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
}