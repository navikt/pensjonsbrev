package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.matchContentType
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.Logger
import io.micrometer.core.instrument.Tag
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.api.ActiveCounter
import no.nav.pensjon.brev.pdfbygger.latex.BlockingLatexService
import no.nav.pensjon.brev.pdfbygger.latex.LATEX_CONFIG_PATH
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocumentRenderer
import org.slf4j.LoggerFactory

fun main(args: Array<String>) = EngineMain.main(args)

fun ApplicationConfig.getProperty(name: String): String =
    property(name).getString()

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.pdfbygger.PdfByggerApp")

@Suppress("unused")
fun Application.module() {
    try {
        setUp()
    } catch (e: Exception) {
        logger.error(e.message, e)
        throw e
    }
}

private fun Application.setUp() {
    monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
    }

    val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    install(MicrometerMetrics) {
        registry = prometheusMeterRegistry
    }

    routing {
        get("/metrics") {
            call.respond(prometheusMeterRegistry.scrape())
        }
    }

    val blockingLatexService = BlockingLatexService(environment.config.config(LATEX_CONFIG_PATH))

    val activityCounter =
        ActiveCounter(prometheusMeterRegistry, "pensjonsbrev_pdf_compile_active", listOf(Tag.of("hpa", "value")))

    log.info("Target parallelism : ${blockingLatexService.latexParallelism}")

    install(ContentNegotiation) {
        jackson {
            pdfByggerConfig()
        }
    }

    install(Compression) {
        gzip {
            priority = 1.0
            matchContentType(
                ContentType.Application.Json
            )
        }
        deflate {
            priority = 10.0
            minimumSize(1024)
            matchContentType(
                ContentType.Application.Json
            )
        }
    }

    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        val ignorePaths = setOf("/isAlive", "/isReady", "/metrics")
        filter {
            !ignorePaths.contains(it.request.path())
        }
        mdc("x_response_code") { it.response.status()?.value?.toString() }
    }

    install(StatusPages) {
        exception<JacksonException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize json body: unknown reason")
        }
    }

    install(CallId) {
        retrieveFromHeader("X-Request-ID")
        generate()
        verify { it.isNotEmpty() }
    }

    routing {

        post("/produserBrev") {
            val result = activityCounter.count {
                call.receive<PDFRequest>()
                    .let { LatexDocumentRenderer.render(it) }
                    .let { blockingLatexService.producePDF(it.files) }
            }
            handleResult(result, call.application.environment.log)
        }

        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            val currentActivity = activityCounter.currentCount()
            if (currentActivity > blockingLatexService.latexParallelism) {
                val msg =
                    "Application not ready: pdf compilation activity of $currentActivity above target of ${blockingLatexService.latexParallelism}"
                call.application.log.info(msg)
                call.respondText(msg, ContentType.Text.Plain, HttpStatusCode.ServiceUnavailable)
            } else {
                call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
            }
        }
    }

}
private suspend fun RoutingContext.handleResult(
    result: PDFCompilationResponse,
    logger: Logger,
) {
    when (result) {
        is PDFCompilationResponse.Success -> call.respond(result.pdfCompilationOutput)
        is PDFCompilationResponse.Failure.Client -> {
            logger.warn("Client error: ${result.reason}")
            if (result.output?.isNotBlank() == true) {
                logger.warn(result.output)
            }
            if (result.error?.isNotBlank() == true) {
                logger.warn(result.error)
            }
            call.respond(HttpStatusCode.BadRequest, result)
        }

        is PDFCompilationResponse.Failure.Server -> {
            logger.error(result.reason)
            call.respond(HttpStatusCode.InternalServerError, result)
        }

        is PDFCompilationResponse.Failure.Timeout -> {
            logger.error(result.reason)
            call.respond(HttpStatusCode.InternalServerError, result)
        }

        is PDFCompilationResponse.Failure.QueueTimeout -> {
            logger.warn("Kø-timeout, løses med automatisk oppstart av flere pods: ${result.reason}")
            call.respond(HttpStatusCode.ServiceUnavailable, result)
        }
    }
}

