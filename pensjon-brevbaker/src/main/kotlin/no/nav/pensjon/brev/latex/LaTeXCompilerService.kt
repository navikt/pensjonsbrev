package no.nav.pensjon.brev.latex

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.lang.IllegalStateException

data class PdfCompilationInput(val files: Map<String, String>)
data class PDFCompilationOutput(val buildLog: String? = null, val pdf: /*base64*/String? = null)

//TODO: Skriv tester
class LaTeXCompilerService(private val pdfByggerUrl: String = "http://127.0.0.1:8081") {
    private val httpClient = HttpClient(CIO){
        install(JsonFeature){
            serializer = JacksonSerializer()
        }
        engine {
            requestTimeout = 20_000
        }
    }

    fun producePDF(compilationInput: PdfCompilationInput): String {
        var response: PDFCompilationOutput
        runBlocking {
            response = httpClient.post("$pdfByggerUrl/compile") { //TODO get url from config
                contentType(ContentType.Application.Json)
                body = compilationInput
            }
        }
        if(response.buildLog != null) {
            throw IllegalStateException(response.buildLog)
        } else {
            return response.pdf
                ?:throw IllegalStateException("Pdf compiler service responded without errors or pdf result")
        }
    }
}