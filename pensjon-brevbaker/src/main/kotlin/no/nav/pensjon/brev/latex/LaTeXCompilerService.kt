package no.nav.pensjon.brev.latex

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brev.template.render.RenderedLatexLetter

data class PdfCompilationInput(val files: Map<String, String>)
data class PDFCompilationOutput(val base64PDF: String)

class LatexCompileException(msg: String, cause: Throwable? = null) : Exception(msg, cause)
class LatexTimeoutException(msg: String) : Exception(msg)

class LaTeXCompilerService(private val pdfByggerUrl: String) {
    private val objectmapper = jacksonObjectMapper()
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
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
        install(HttpRequestRetry) {
            maxRetries = 300
            exponentialDelay(maxDelayMs = 1000)
            retryOnExceptionIf { _, cause ->
                val actualCause = cause.unwrapCancellationException()
                actualCause is HttpRequestTimeoutException
                        || actualCause is ConnectTimeoutException
                        || actualCause is ServerResponseException
            }
        }
        install(HttpSend) {
            maxSendCount = 300
        }
        install(ContentEncoding) {
            gzip()
        }
        expectSuccess = true
    }

    suspend fun producePDF(latexLetter: RenderedLatexLetter, callId: String?): PDFCompilationOutput =
        httpClient.post("$pdfByggerUrl/compile") {
            contentType(ContentType.Application.Json)
            header("Nav-Call-Id", callId)
            //TODO unresolved bug. There is a bug where simultanious requests will lock up the requests for this http client
            // If the body is set using an object, it will use the content-negotiation strategy which also uses a jackson object-mapper
            // for some unknown reason, this results in all requests being halted for around 5 minutes.
            // To test if the bug is present, run 10 simultanious requests to brevbaker and see if it starts producing letters.
            // The solution is to seemingly do the same, but with creating a objectmapper outside of content-negotiation instead of simply using the following line:
            // setBody(PdfCompilationInput(latexLetter.base64EncodedFiles()))
            // this needs further investigation
            setBody(objectmapper.writeValueAsBytes(PdfCompilationInput(latexLetter.base64EncodedFiles())))
        }.body()

    suspend fun ping() {
        httpClient.get("$pdfByggerUrl/isAlive")
    }
}
