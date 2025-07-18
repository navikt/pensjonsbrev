package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.protobuf.kotlin.toByteString
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.health.v1.HealthCheckRequest
import io.grpc.health.v1.HealthCheckResponse
import io.grpc.health.v1.HealthGrpcKt
import io.grpc.health.v1.healthCheckResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.log
import io.ktor.server.config.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.EngineMain
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.latex.BlockingLatexService
import no.nav.pensjon.brev.pdfbygger.latex.LATEX_CONFIG_PATH
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.rpc.CompilePdfResponseKt.clientError
import no.nav.pensjon.brev.pdfbygger.rpc.PdfCompile
import no.nav.pensjon.brev.pdfbygger.rpc.PdfCompileServiceGrpcKt
import no.nav.pensjon.brev.pdfbygger.rpc.compilePdfResponse
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun main(args: Array<String>) =
    if (System.getenv("PDF_BYGGER_IS_GRPC") == "true") {
        PdfByggerGrpcApp(ConfigLoader.load()).run()
    } else {
        EngineMain.main(args)
    }

class PdfByggerGrpcApp(private val config: ApplicationConfig) {
    private val logger = LoggerFactory.getLogger(PdfByggerGrpcApp::class.java)
    private val blockingLatexService = BlockingLatexService(config.config(LATEX_CONFIG_PATH))
    private val coroutineContext = EmptyCoroutineContext
    private val server: Server = ServerBuilder
        .forPort(config.property("ktor.deployment.port").getString().toInt())
        .addService(PdfCompileService(coroutineContext, blockingLatexService))
        .addService(HealthCheckService(coroutineContext, blockingLatexService))
        .build()

    private val healthProbeServer = embeddedServer(Netty, port = 8081) {
        routing {
            get("/isAlive") {
                call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
            }

            get("/isReady") {
                if (blockingLatexService.availablePermits > 0) {
                    call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
                } else {
                    val msg =
                        "Application not ready: all slots in use"
                    call.application.log.info(msg)
                    call.respondText(msg, ContentType.Text.Plain, HttpStatusCode.ServiceUnavailable)
                }
            }
        }
    }.start()

    fun run() {
        server.start()
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.info("Shutting down PDF Bygger gRPC server...")
                server.shutdown()
                healthProbeServer.stop()
                logger.info("PDF Bygger gRPC server shut down.")
            }
        )
        logger.info("Pdf Bygger gRPC server started on port ${server.port}")
        server.awaitTermination()
    }
}

class PdfCompileService(context: CoroutineContext, val latexCompileService: BlockingLatexService) :
    PdfCompileServiceGrpcKt.PdfCompileServiceCoroutineImplBase(context) {

    private val logger = LoggerFactory.getLogger(PdfCompileService::class.java)
    private val objectMapper = pdfByggerObjectMapper()

    override suspend fun compilePdf(request: PdfCompile.CompilePdfRequest) =
        withMdc("x_correlationId" to request.callId) {
            compilePdfResponse {
                val pdfRequest = request.toPDFRequest()

                val result = LatexDocumentRenderer.render(pdfRequest)
                    .let { latexCompileService.producePDF(it.files) }

                logResult(result)

                when (result) {
                    is PDFCompilationResponse.Success -> {
                        pdf = result.pdfCompilationOutput.bytes.toByteString()
                    }

                    is PDFCompilationResponse.Failure.Client -> {
                        error = clientError {
                            reason = result.reason
                            if (result.output != null) {
                                output = result.output
                            }
                            if (result.error != null) {
                                errorOutput = result.error
                            }
                        }
                    }
                    is PDFCompilationResponse.Failure.Server -> {
                        throw StatusException(Status.UNKNOWN.withDescription(result.reason))
                    }

                    is PDFCompilationResponse.Failure.QueueTimeout,
                    is PDFCompilationResponse.Failure.Timeout -> {
                        throw StatusException(Status.UNAVAILABLE.withDescription(result.reason))
                    }
                }
            }
        }

    private fun logResult(result: PDFCompilationResponse) =
        when (result) {
            is PDFCompilationResponse.Failure -> logger.error("PDF compilation failed: ${result.reason}")
            is PDFCompilationResponse.Success -> logger.info("Successfully compiled PDF")
        }

    private fun PdfCompile.CompilePdfRequest.toPDFRequest(): PDFRequest = PDFRequest(
        letterMarkup = objectMapper.readValue<LetterMarkup>(letterMarkup),
        attachments = attachmentsList.map { objectMapper.readValue<LetterMarkup.Attachment>(it) },
        language = LanguageCode.valueOf(language),
        felles = objectMapper.readValue<Felles>(felles),
        brevtype = LetterMetadata.Brevtype.valueOf(brevtype),
    )
}

class HealthCheckService(context: CoroutineContext, val latexCompileService: BlockingLatexService) :
    HealthGrpcKt.HealthCoroutineImplBase(context) {
    override suspend fun check(request: HealthCheckRequest) = healthCheckResponse {
        status = when (request.service) {
            "liveness" -> {
                HealthCheckResponse.ServingStatus.SERVING
            }

            "readiness" -> {
                if (latexCompileService.availablePermits > 0) {
                    HealthCheckResponse.ServingStatus.SERVING
                } else {
                    HealthCheckResponse.ServingStatus.NOT_SERVING
                }
            }

            else -> {
                HealthCheckResponse.ServingStatus.SERVING
            }
        }
    }
}