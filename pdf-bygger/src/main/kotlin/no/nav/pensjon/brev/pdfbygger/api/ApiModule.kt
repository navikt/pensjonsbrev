package no.nav.pensjon.brev.pdfbygger.api

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.calllogging.processingTimeMillis
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.matchContentType
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.date.getTimeMillis
import io.ktor.util.logging.Logger
import io.micrometer.core.instrument.Tag
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.model.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.getProperty
import no.nav.pensjon.brev.pdfbygger.latex.BlockingLatexService
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import no.nav.pensjon.brev.pdfbygger.pdfByggerConfig
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun Application.restModule(
    latexCompileService: LatexCompileService
) {
    val parallelism =
        getProperty("pdfBygger.latex.latexParallelism")?.toInt() ?: Runtime.getRuntime().availableProcessors()
    val blockingLatexService = BlockingLatexService(
        queueWaitTimeout = getProperty("pdfBygger.latex.compileQueueWaitTimeout")?.let { Duration.Companion.parse(it) }
            ?: 4.seconds,
        latexParallelism = parallelism,
        latexCompileService = latexCompileService,
    )

    val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    install(MicrometerMetrics) {
        registry = prometheusMeterRegistry
    }

    val activityCounter = ActiveCounter(prometheusMeterRegistry, "pensjonsbrev_pdf_compile_active", listOf(Tag.of("hpa", "value")))

    log.info("Target parallelism : $parallelism")

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
        mdc("x_response_time") { it.processingTimeMillis(::getTimeMillis).toString() }
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
            if (currentActivity > parallelism) {
                val msg = "Application not ready: pdf compilation activity of $currentActivity above target of $parallelism"
                call.application.log.info(msg)
                call.respondText(msg, ContentType.Text.Plain, HttpStatusCode.ServiceUnavailable)
            } else {
                call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
            }
        }
        get("/metrics") {
            call.respond(prometheusMeterRegistry.scrape())
        }
    }
}

private suspend fun RoutingContext.handleResult(
    result: PDFCompilationResponse,
    logger: Logger,
) {
    when (result) {
        is PDFCompilationResponse.Base64PDF -> call.respond(result)
        is PDFCompilationResponse.Failure.Client -> {
            logger.info("Client error: ${result.reason}")
            if (result.output?.isNotBlank() == true) {
                logger.info(result.output)
            }
            if (result.error?.isNotBlank() == true) {
                logger.info(result.error)
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