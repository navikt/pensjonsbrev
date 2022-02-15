package no.nav.pensjon.brev.latex

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*

data class PdfCompilationInput(val files: Map<String, String>)
data class PDFCompilationOutput(val base64PDF: String)

//TODO: Skriv tester
class LaTeXCompilerService(private val pdfByggerUrl: String) {
    private val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }

        engine {
            requestTimeout = 20_000
        }
    }

    suspend fun producePDF(compilationInput: PdfCompilationInput, callId: String?): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/compile") {
            contentType(ContentType.Application.Json)
            header("Nav-Call-Id", callId)
            body = compilationInput
        }

    suspend fun ping() {
        httpClient.get<String>("$pdfByggerUrl/isAlive")
    }
}
