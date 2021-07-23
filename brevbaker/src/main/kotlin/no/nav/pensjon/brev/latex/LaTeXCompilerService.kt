package no.nav.pensjon.brev.latex

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.dto.PDFCompilationOutput
import no.nav.pensjon.brev.dto.PdfCompilationInput
import java.lang.IllegalStateException

class LaTeXCompilerService {
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
            response = httpClient.post("http://127.0.0.1:8080/compile") { //TODO get url from config
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