package no.nav.brev.brevbaker

import no.nav.brev.brevbaker.pdfbygger.api.PDFCompilationOutput
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
import no.nav.brev.brevbaker.serialization.internalObjectMapper
import no.nav.brev.brevbaker.pdfbygger.api.LetterPDFRequest

/**
 * Delt testklient mot pdf-bygger. Brukes av både brevbaker (core) og pdf-bygger sine egne
 * integrasjonstester, slik at begge treffer pdf-bygger med nøyaktig samme serialisering som
 * produksjonskoden ([internalObjectMapper]).
 */
class PdfByggerTestService(
    private val pdfByggerUrl: String = PDFByggerTestContainer.mappedUrl(),
    private val logWarning: (String) -> Unit = ::println,
) : PDFByggerService {
    private val objectMapper = internalObjectMapper()

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        HttpResponseValidator {
            validateResponse {
                validateResponse(it.status.value, logWarning) { it.body<String>() }
            }
        }

        engine {
            requestTimeout = 0
        }
    }

    override suspend fun producePDF(pdfRequest: PDFRequest): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/produserBrev") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsBytes(pdfRequest))
        }.body()

    override suspend fun producePDFV2(pdfRequest: LetterPDFRequest): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/v2/produserBrev") {
            contentType(ContentType.Application.Json)
            setBody(objectMapper.writeValueAsBytes(pdfRequest))
        }.body()

    suspend fun ping(): Boolean = httpClient.get("$pdfByggerUrl/isAlive").status.isSuccess()
}
