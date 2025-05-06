package no.nav.brev.brevbaker

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.PDFRequest

class LaTeXCompilerService(private val pdfByggerUrl: String) : PDFByggerService {
    private val objectmapper = jacksonObjectMapper()
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        HttpResponseValidator {
            validateResponse {
                validateResponse(it.status.value,
                    { msg -> println(msg) }) { it.body<String>() }
            }
        }
        install(ContentEncoding) {
            gzip()
        }

        engine {
            requestTimeout = 0
        }
    }

    override suspend fun producePDF(pdfRequest: PDFRequest, path: String): PDFCompilationOutput =
            httpClient.post("$pdfByggerUrl/$path") {
                contentType(ContentType.Application.Json)
                setBody(objectmapper.writeValueAsBytes(pdfRequest))
            }.body()

}
