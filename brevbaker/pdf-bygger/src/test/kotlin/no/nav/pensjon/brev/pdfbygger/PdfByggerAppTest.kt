package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.markup.LetterPDFRequest
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.letterMarkup
import no.nav.brev.brevbaker.markup.dsl.letterPDFRequest
import no.nav.brev.brevbaker.markup.dsl.paragraph
import no.nav.brev.brevbaker.markup.dsl.saksinformasjon
import no.nav.brev.brevbaker.markup.dsl.signatur
import no.nav.brev.brevbaker.markup.dsl.title1
import no.nav.brev.brevbaker.PDFRequest
import no.nav.pensjon.brev.pdfbygger.typst.TypstCompileService
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter

class PdfByggerAppTest {

    private val mapper = jacksonObjectMapper().apply { pdfByggerConfig() }

    @Test
    fun appRuns() {
        testApplication {
            environment {
                config = ApplicationConfig(null)
            }

            val response = client.get("/isAlive")
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    /**
     * Happy-path-test for `/produserBrev` som verifiserer at routing, JSON-deserialisering av [PDFRequest]
     * og kall til [no.nav.pensjon.brev.pdfbygger.typst.documentrender.TypstDocumentRenderer] er korrekt
     * koblet sammen, uten å kreve at faktisk `typst`-binær er tilgjengelig (slik integrasjonstestene
     * mot pdf-bygger-containeren krever).
     */
    @Test
    fun `produserBrev happy path returnerer PDFCompilationOutput`() {
        val expectedPdfBytes = byteArrayOf(0x25, 0x50, 0x44, 0x46) // "%PDF"
        val rendererCalled = ArrayList<Int>()

        val fakeCompileService = object : TypstCompileService() {
            override suspend fun createLetter(writeLetter: (TypstFileWriter) -> Unit): PDFCompilationResponse {
                // Driver renderer-kallbacken slik at TypstDocumentRenderer faktisk produserer Typst-innhold,
                // men hopper over det eksterne `typst`-prosesskallet.
                val captured = ByteArrayOutputStream()
                OutputStreamWriter(captured, Charsets.UTF_8).use { writer ->
                    writeLetter(TypstFileWriter(writer))
                }
                rendererCalled.add(captured.size())
                return PDFCompilationResponse.Success(PDFCompilationOutput(expectedPdfBytes))
            }
        }

        val request = PDFRequest(
            letterMarkup = letterMarkup {
                title { text("En fin tittel") }
                outline {
                    paragraph { text("Hei, dette er et brev.") }
                }
            },
            attachments = emptyList(),
            language = LanguageCode.BOKMAL,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )

        testApplication {
            environment {
                // Tom config slik at module-en fra application.conf ikke auto-lastes parallelt med vår testoppsett.
                config = MapApplicationConfig()
            }
            application {
                setUp(fakeCompileService)
            }

            val response = client.post("/produserBrev") {
                contentType(ContentType.Application.Json)
                setBody(mapper.writeValueAsBytes(request))
            }

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(1, rendererCalled.size, "TypstDocumentRenderer skal være kalt nøyaktig én gang")
            assertTrue(rendererCalled.single() > 0, "TypstDocumentRenderer skal ha skrevet Typst-innhold")

            val output = mapper.readValue(response.bodyAsText(), PDFCompilationOutput::class.java)
            assertTrue(expectedPdfBytes.contentEquals(output.bytes), "PDF-bytes skal returneres uendret til klienten")
        }
    }

    /**
     * Happy-path-test for `/v2/produserBrev` som verifiserer at routing, kotlinx-JSON-deserialisering av
     * [LetterPDFRequest] og kall til [no.nav.pensjon.brev.pdfbygger.typst.documentrender.TypstDocumentRendererV2]
     * er korrekt koblet sammen, uten å kreve at faktisk `typst`-binær er tilgjengelig.
     */
    @Test
    fun `v2 produserBrev happy path returnerer PDFCompilationOutput`() {
        val expectedPdfBytes = byteArrayOf(0x25, 0x50, 0x44, 0x46) // "%PDF"
        val rendererCalled = ArrayList<Int>()

        val fakeCompileService = object : TypstCompileService() {
            override suspend fun createLetter(writeLetter: (TypstFileWriter) -> Unit): PDFCompilationResponse {
                val captured = ByteArrayOutputStream()
                OutputStreamWriter(captured, Charsets.UTF_8).use { writer ->
                    writeLetter(TypstFileWriter(writer))
                }
                rendererCalled.add(captured.size())
                return PDFCompilationResponse.Success(PDFCompilationOutput(expectedPdfBytes))
            }
        }

        val request = letterPDFRequest(
            spraak = Markup.Spraak.BOKMAL,
            brevtype = Markup.Brevtype.VEDTAKSBREV,
            letter = letterMarkup(
                saksinformasjon = saksinformasjon(
                    gjelderNavn = "Navn Navnesen",
                    gjelderPersonidentifikator = "12345678901",
                    saksnummer = "123",
                    dokumentDato = java.time.LocalDate.of(2025, 1, 1),
                ),
                signatur = signatur(hilsenTekst = "hilsen", navAvsenderEnhet = "Nav sentralt"),
            ) {
                title1("En fin tittel")
                outline {
                    paragraph("Hei, dette er et brev.")
                }
            },
        )

        testApplication {
            environment {
                config = MapApplicationConfig()
            }
            application {
                setUp(fakeCompileService)
            }

            val response = client.post("/v2/produserBrev") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(LetterPDFRequest.serializer(), request))
            }

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(1, rendererCalled.size, "TypstDocumentRendererV2 skal være kalt nøyaktig én gang")
            assertTrue(rendererCalled.single() > 0, "TypstDocumentRendererV2 skal ha skrevet Typst-innhold")

            val output = mapper.readValue(response.bodyAsText(), PDFCompilationOutput::class.java)
            assertTrue(expectedPdfBytes.contentEquals(output.bytes), "PDF-bytes skal returneres uendret til klienten")
        }
    }
}
