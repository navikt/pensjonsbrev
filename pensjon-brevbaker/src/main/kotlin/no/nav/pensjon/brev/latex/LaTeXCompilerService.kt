package no.nav.pensjon.brev.latex

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.utils.io.*
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brev.template.render.RenderedLatexLetter

data class PdfCompilationInput(val files: Map<String, String>)
data class PDFCompilationOutput(val base64PDF: String)

class LatexCompileException(msg: String, cause: Throwable? = null) : Exception(msg, cause)
class LatexTimeoutException(msg: String) : Exception(msg)

class LaTeXCompilerService(private val pdfByggerUrl: String) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        install(ContentEncoding) {
            gzip()
        }
        expectSuccess = true

        engine {
            requestTimeout = 60_000
        }
    }

    // TODO improve error handling.
    suspend fun producePDF(latexLetter: RenderedLatexLetter, callId: String?): PDFCompilationOutput =
        httpClient.runCatching {
            httpClient.post("$pdfByggerUrl/compile") {
                contentType(ContentType.Application.Json)
                header("Nav-Call-Id", callId)
                //TODO use multipart form/file data. This works but it's inefficient and ugly.
                // it's a fix for the application locking up on just a few simultaneous users

                setBody(jacksonObjectMapper().writeValueAsBytes(PdfCompilationInput(latexLetter.base64EncodedFiles())))
            }
        }.getOrElse { exception ->
            when (exception) {
                is ClientRequestException -> {
                    val response = exception.response
                    when (response.status) {
                        HttpStatusCode.BadRequest -> {
                            throw LatexCompileException("Rendered latex is invalid, couldn't compile pdf: ${response.bodyAsText()}")
                        }
                        HttpStatusCode.InternalServerError -> {
                            throw LatexCompileException("Couldn't compile latex to pdf due to server error: ${response.bodyAsText()}")
                        }

                        HttpStatusCode.GatewayTimeout,
                        HttpStatusCode.TooManyRequests,
                        HttpStatusCode.RequestTimeout
                        -> {
                            throw LatexTimeoutException(
                                """Latex compilation timed out with status with:
                                    code: ${response.status.description}
                                    message:${response.bodyAsText()}""".trimMargin())
                        }
                        else -> {throw exception}
                    }
                }
                is HttpRequestTimeoutException,
                is CancellationException,
                is ConnectTimeoutException ->
                    throw LatexTimeoutException("Latex compilation timed out with exception: " + exception.message)
                else -> {throw exception}
            }
        }.body()

    suspend fun ping() {
        httpClient.get("$pdfByggerUrl/isAlive")
    }
}
