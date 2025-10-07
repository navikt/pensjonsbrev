package no.nav.pensjon.brev.latex

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.callid.KtorCallIdContextElement
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import kotlinx.coroutines.withTimeoutOrNull
import no.nav.brev.brevbaker.LatexTimeoutException
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import org.slf4j.LoggerFactory
import kotlin.math.pow
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class LaTeXCompilerService(
    private val pdfByggerUrl: String,
    maxRetries: Int = 30,
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

        if (maxRetries > 0) {
            install(HttpRequestRetry) {
                this.maxRetries = maxRetries
                delayMillis {
                    minOf(2.0.pow(it).toLong(), 1000L) + Random.nextLong(100)
                }
            }
            install(HttpSend) {
                // It is important that maxSendCount exceeds maxRetries.
                // If not the client will fail with SendCountExceeded-exception instead of the server response.
                maxSendCount = maxRetries + 20
            }
        }
    }

    override suspend fun producePDF(pdfRequest: PDFRequest, path: String): PDFCompilationOutput =
        withTimeoutOrNull(timeout) {
            httpClient.post("$pdfByggerUrl/$path") {
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
        } ?: throw LatexTimeoutException("Spent more than $timeout trying to compile latex to pdf")

    suspend fun ping(): Boolean = httpClient.get("$pdfByggerUrl/isAlive").status.isSuccess()
}
