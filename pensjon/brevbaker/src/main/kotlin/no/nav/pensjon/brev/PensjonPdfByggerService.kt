package no.nav.pensjon.brev

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.callid.*
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.IOException
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PDFTimeoutException
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private const val MAX_RETRIES = 5
private val RETRY_BASE_DELAY = 200.milliseconds
private val RETRY_MAX_DELAY = 2.seconds

class PensjonPdfByggerService(
    private val pdfByggerUrl: String,
    private val timeout: Duration = 300.seconds,
) : PDFByggerService {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectmapper = brevbakerJacksonObjectMapper()
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson {
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
        }
        HttpResponseValidator {
            validateResponse { validateResponse(it.status.value, { msg -> logger.warn(msg) }) { it.body<String>() } }
        }
        install(ContentEncoding) {
            gzip()
        }

        engine {
            requestTimeout = 0
        }

        install(HttpRequestRetry) {
            maxRetries = MAX_RETRIES

            exponentialDelay(
                base = 2.0,
                baseDelayMs = RETRY_BASE_DELAY.inWholeMilliseconds,
                maxDelayMs = RETRY_MAX_DELAY.inWholeMilliseconds,
                randomizationMs = RETRY_BASE_DELAY.inWholeMilliseconds,
            )
            retryOnExceptionIf { _, cause ->
                val actualCause = cause.unwrapCancellationException()
                val doRetry = actualCause is HttpRequestTimeoutException
                        || actualCause is ConnectTimeoutException
                        || actualCause is ServerResponseException
                        || actualCause is IOException
                if (!doRetry) {
                    logger.error("Won't retry for exception: ${actualCause.message}", actualCause)
                }
                doRetry
            }
        }
        install(HttpSend) {
            // It is important that maxSendCount exceeds maxRetries.
            // If not, the client will fail with SendCountExceeded-exception instead of the server response.
            maxSendCount = MAX_RETRIES + 20
        }
    }

    override suspend fun producePDF(pdfRequest: PDFRequest): PDFCompilationOutput = try {
        withTimeoutOrNull(timeout) {
            httpClient.post("$pdfByggerUrl/produserBrev") {
                // Bakoverkompatibilitet: pdf-bygger <= main ruter til LaTeX uten dette flagget.
                // Ny pdf-bygger ignorerer parameteret og bruker alltid typst.
                // Fjern dette etter at ny pdf-bygger er rullet ut til alle miljø.
                url { parameters.append("typst", "true") }
                contentType(ContentType.Application.Json)
                header("X-Request-ID", coroutineContext[KtorCallIdContextElement]?.callId)
                //TODO unresolved bug. There is a bug where simultanious requests will lock up the requests for this http client
                // If the body is set using an object, it will use the content-negotiation strategy which also uses a jackson object-mapper
                // for some unknown reason, this results in all requests being halted for around 5 minutes.
                // To test if the bug is present, run 10 simultanious requests to brevbaker and see if it starts producing letters.
                // The solution is to seemingly do the same, but with creating a objectmapper outside of content-negotiation instead of simply using the following line:
                // setBody(pdfRequest)
                // this needs further investigation
                setBody(objectmapper.writeValueAsBytes(pdfRequest))
            }.body()
        }
    } catch (e: CancellationException) {
        throw PDFTimeoutException("Spent more than $timeout trying to compile pdf", e)
    } ?: throw PDFTimeoutException("Spent more than $timeout trying to compile pdf")

    suspend fun ping(): Boolean = httpClient.get("$pdfByggerUrl/isAlive").status.isSuccess()
}
