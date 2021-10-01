package no.nav.pensjon.brev.latex

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

data class PdfCompilationInput(val files: Map<String, String>)
data class PDFCompilationOutput(val base64PDF: String)

//TODO: Skriv tester
class LaTeXCompilerService(private val pdfByggerUrl: String = "http://127.0.0.1:8081") {
    private val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
        engine {
            requestTimeout = 20_000
        }
    }

    fun producePDF(compilationInput: PdfCompilationInput): PDFCompilationOutput =
        runBlocking {
            httpClient.post("$pdfByggerUrl/compile") {
                contentType(ContentType.Application.Json)
                body = compilationInput
            }
        }
}
