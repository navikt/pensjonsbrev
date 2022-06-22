package no.nav.pensjon.brev.latex

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*

data class PdfCompilationInput(val files: Map<String, String>)
data class PDFCompilationOutput(val base64PDF: String)

//TODO: Skriv tester
class LaTeXCompilerService(private val pdfByggerUrl: String) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }

        engine {
            requestTimeout = 120_000
        }
    }

    suspend fun producePDF(compilationInput: PdfCompilationInput, callId: String?): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/compile") {
            contentType(ContentType.Application.Json)
            header("Nav-Call-Id", callId)
            setBody(compilationInput)
        }.body()

    suspend fun ping() {
        httpClient.get("$pdfByggerUrl/isAlive")
    }
}
