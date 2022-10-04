package no.nav.pensjon.brev.latex

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brev.template.render.RenderedLatexLetter

data class PdfCompilationInput(val files: Map<String, String>)
data class PDFCompilationOutput(val base64PDF: String)

class LatexCompileException(msg: String, cause: Throwable? = null) : Exception(msg, cause)

class LaTeXCompilerService(private val pdfByggerUrl: String) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        install(ContentEncoding){
            gzip()
        }

        HttpResponseValidator {
            validateResponse { response ->
                when (response.status) {
                    HttpStatusCode.BadRequest -> {
                        throw LatexCompileException("Rendered latex is invalid, couldn't compile pdf: ${response.body<String>()}")
                    }

                    HttpStatusCode.InternalServerError -> {
                        throw LatexCompileException("Couldn't compile latex to pdf due to server error: ${response.body<String>()}")
                    }
                }
            }
        }

        engine {
            requestTimeout = 60_000
        }
    }

    suspend fun producePDF(latexLetter: RenderedLatexLetter, callId: String?): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/compile") {
            contentType(ContentType.Application.Json)
            header("Nav-Call-Id", callId)
            setBody(jacksonObjectMapper().writeValueAsBytes(PdfCompilationInput(latexLetter.base64EncodedFiles())))
        }.body()

    suspend fun ping() {
        httpClient.get("$pdfByggerUrl/isAlive")
    }
}
