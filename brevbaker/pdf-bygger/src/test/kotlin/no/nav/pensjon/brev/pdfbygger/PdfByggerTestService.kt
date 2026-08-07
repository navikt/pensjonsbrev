package no.nav.pensjon.brev.pdfbygger

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import no.nav.brev.brevbaker.PDFRequest
import no.nav.brev.brevbaker.serialization.internalObjectMapper
import no.nav.brev.brevbaker.pdfbygger.api.LetterPDFRequest
import no.nav.brev.brevbaker.pdfbygger.api.PDFCompilationOutput

class PdfByggerTestService(
    private val pdfByggerUrl: String = PDFByggerTestContainer.mappedUrl(),
    private val logWarning: (String) -> Unit = ::println,
) {
    private val objectMapper = internalObjectMapper()

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        HttpResponseValidator {
            validateResponse {
                if (it.status.value >= 400) {
                    val body = it.body<String>()
                    logWarning("Pdf-bygger returned ${it.status.value}: $body")
                    throw IllegalStateException("Pdf-bygger returned ${it.status.value}: $body")
                }
            }
        }
        engine {
            requestTimeout = 0
        }
    }

    suspend fun producePDF(pdfRequest: PDFRequest): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/produserBrev") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsBytes(pdfRequest))
        }.body()

    suspend fun producePDFV2(pdfRequest: LetterPDFRequest): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/v2/produserBrev") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsBytes(pdfRequest))
        }.body()

    suspend fun ping(): Boolean = httpClient.get("$pdfByggerUrl/isAlive").status.isSuccess()
}
