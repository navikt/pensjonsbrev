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
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.withTimeoutOrNull
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brev.template.render.RenderedLatexLetter
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class PdfCompilationInput(val files: Map<String, String>)
data class PDFCompilationOutput(val base64PDF: String)

class LatexCompileException(msg: String, cause: Throwable? = null) : Exception(msg, cause)
class LatexTimeoutException(msg: String, cause: Throwable? = null) : Exception(msg, cause)
class LatexInvalidException(msg: String, cause: Throwable? = null) : Exception(msg, cause)

class LaTeXCompilerService(private val pdfByggerUrl: String, maxRetries: Int = 30, private val timeout: Duration = 300.seconds) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectmapper = jacksonObjectMapper()
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        HttpResponseValidator {
            validateResponse { response ->
                when (response.status) {
                    HttpStatusCode.BadRequest -> {
                        val body = response.body<String>()
                        logger.warn("Rendered latex is invalid, couldn't compile pdf: $body")
                        throw LatexInvalidException("Rendered latex is invalid, couldn't compile pdf: $body")
                    }

                    HttpStatusCode.InternalServerError -> {
                        val body = response.body<String>()
                        logger.warn("Couldn't compile latex to pdf due to server error: $body")
                        throw LatexCompileException("Couldn't compile latex to pdf due to server error: $body")
                    }

                    HttpStatusCode.ServiceUnavailable -> {
                        val body = response.body<String>()
                        logger.warn("Service unavalailable - couldn't compile latex to pdf: $body")
                        throw LatexCompileException("Service unavalailable - couldn't compile latex to pdf: $body")
                    }
                }
            }
        }
        install(ContentEncoding) {
            gzip()
        }

        engine {
            requestTimeout = 0
        }

        if (maxRetries > 0) {
            install(HttpRequestRetry) {
                this.maxRetries = maxRetries
                exponentialDelay(maxDelayMs = 10_000)
                retryOnExceptionIf { _, cause ->
                    val actualCause = cause.unwrapCancellationException()
                    val doRetry = actualCause is HttpRequestTimeoutException
                            || actualCause is ConnectTimeoutException
                            || actualCause is ServerResponseException
                            || (actualCause is ClientRequestException && actualCause.response.status == HttpStatusCode.BadRequest)
                            || actualCause is IOException

                    if (!doRetry) {
                        logger.error("Won't retry for exception: ${actualCause.message}", actualCause)
                    }
                    doRetry
                }
            }
            install(HttpSend) {
                // It is important that maxSendCount exceeds maxRetries.
                // If not the client will fail with SendCountExceeded-exception instead of the server response.
                maxSendCount = maxRetries + 20
            }
        }
    }

    suspend fun producePDF(latexLetter: RenderedLatexLetter, callId: String?): PDFCompilationOutput =
        withTimeoutOrNull(timeout) {
            httpClient.post("$pdfByggerUrl/compile") {
                contentType(ContentType.Application.Json)
                header("X-Request-ID", callId)
                //TODO unresolved bug. There is a bug where simultanious requests will lock up the requests for this http client
                // If the body is set using an object, it will use the content-negotiation strategy which also uses a jackson object-mapper
                // for some unknown reason, this results in all requests being halted for around 5 minutes.
                // To test if the bug is present, run 10 simultanious requests to brevbaker and see if it starts producing letters.
                // The solution is to seemingly do the same, but with creating a objectmapper outside of content-negotiation instead of simply using the following line:
                // setBody(PdfCompilationInput(latexLetter.base64EncodedFiles()))
                // this needs further investigation
                setBody(objectmapper.writeValueAsBytes(PdfCompilationInput(latexLetter.base64EncodedFiles())))
            }.body()
        } ?: throw LatexTimeoutException("Spent more than $timeout trying to compile latex to pdf")

    suspend fun ping(): Boolean = httpClient.get("$pdfByggerUrl/isAlive").status.isSuccess()
}
