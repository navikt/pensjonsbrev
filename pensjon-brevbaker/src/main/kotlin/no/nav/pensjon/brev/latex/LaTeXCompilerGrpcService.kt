package no.nav.pensjon.brev.latex

import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.health.v1.HealthGrpc
import io.ktor.callid.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.rpc.PdfCompileServiceGrpc
import no.nav.pensjon.brev.pdfbygger.rpc.PdfCompileServiceGrpcKt
import no.nav.pensjon.brev.pdfbygger.rpc.compilePdfRequest
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import kotlin.time.Duration.Companion.seconds

private val retryable = setOf(Status.Code.UNAVAILABLE, Status.Code.UNKNOWN)

class LaTeXCompilerGrpcService(host: String, port: Int) : PDFByggerService {
    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)
    private val objectMapper = brevbakerJacksonObjectMapper()
    private val client = PdfCompileServiceGrpcKt.PdfCompileServiceCoroutineStub(
        ManagedChannelBuilder
            .forAddress(host, port)
            .defaultServiceConfig(serviceConfig)
            .enableRetry()
            .usePlaintext()
            .build(),
    )

    override suspend fun producePDF(pdfRequest: PDFRequest, path: String): PDFCompilationOutput =
        withTimeout(300.seconds) {
            flow {
                emit(requestPdfCompilation(pdfRequest))
            }.retry(100) { cause ->
                if (cause is StatusException && cause.status.code in retryable) {
                    logger.info("Retrying PDF compilation due to: ${cause.status.description}", cause)
                    true
                } else {
                    logger.error("Non-retryable error during PDF compilation: ${cause.message}", cause)
                    false
                }
            }.single()
        }

    private suspend fun requestPdfCompilation(pdfRequest: PDFRequest): PDFCompilationOutput =
        withContext(Dispatchers.IO) {
            client.compilePdf(
                compilePdfRequest {
                    letterMarkup = objectMapper.writeValueAsString(pdfRequest.letterMarkup)
                    attachments.addAll(pdfRequest.attachments.map<LetterMarkup.Attachment, String> {
                        objectMapper.writeValueAsString(
                            it
                        )
                    })
                    language = pdfRequest.language.name
                    felles = objectMapper.writeValueAsString(pdfRequest.felles)
                    brevtype = pdfRequest.brevtype.name
                    callId = coroutineContext[KtorCallIdContextElement]?.callId ?: "unknown-call-id"
                }
            )
        }.let {
            if (it.hasPdf()) {
                PDFCompilationOutput(it.pdf.toByteArray())
            } else {
                throw Exception("PDF compilation failed: ${it.error.reason} ")
            }
        }
}

private val serviceConfig = mapOf(
    "loadBalancingConfig" to listOf(mapOf("round_robin" to emptyMap<String, String>())),
    "healthCheckConfig" to mapOf(
        "serviceName" to HealthGrpc.SERVICE_NAME,
    ),
    "retryThrottling" to mapOf(
        "maxTokens" to "10",
        "tokenRatio" to 0.1,
    ),
    "methodConfig" to listOf(
        mapOf(
            "name" to listOf(
                mapOf(
                    "service" to PdfCompileServiceGrpc.SERVICE_NAME,
                    "method" to PdfCompileServiceGrpc.getCompilePdfMethod().bareMethodName!!
                )
            ),
            "waitForReady" to true,
            "timeout" to "300s",
            "retryPolicy" to mapOf(
                "maxAttempts" to "5",
                "initialBackoff" to "0.2s",
                "maxBackoff" to "10s",
                "backoffMultiplier" to 5.0,
                "retryableStatusCodes" to listOf(Status.Code.UNAVAILABLE, Status.Code.UNKNOWN).map { it.name },
            )
        )
    )
)