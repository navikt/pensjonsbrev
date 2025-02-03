package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.date.*
import io.ktor.util.logging.Logger
import io.micrometer.core.instrument.Tag
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.PDFRequest
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


fun main(args: Array<String>) = EngineMain.main(args)

private fun Application.getProperty(name: String): String? =
    environment.config.propertyOrNull(name)?.getString()

@Suppress("unused")
fun Application.module() {
    val parallelism = getProperty("pdfBygger.latexParallelism")?.toInt() ?: Runtime.getRuntime().availableProcessors()
    val laTeXService = LaTeXService(
        compileTimeout = getProperty("pdfBygger.compileTimeout")?.let { Duration.parse(it) } ?: 300.seconds,
        queueWaitTimeout = getProperty("pdfBygger.compileQueueWaitTimeout")?.let { Duration.parse(it) } ?: 4.seconds,
        latexParallelism = parallelism,
        latexCommand = getProperty("pdfBygger.latexCommand") ?: "xelatex --interaction=nonstopmode -halt-on-error",
        tmpBaseDir = Path.of(environment.config.property("pdfBygger.compileTmpDir").getString()),
    )

    log.info("Target parallelism : $parallelism")
    monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
    }


    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            registerModule(LetterMarkupModule)
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
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

    val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    install(MicrometerMetrics) {
        registry = prometheusMeterRegistry
    }
    val activityCounter = ActiveCounter(prometheusMeterRegistry, "pensjonsbrev_pdf_compile_active", listOf(Tag.of("hpa", "value")))

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
                    // TODO: Dropp base64-enkodinga (og dekodinga inni)
                    .let { laTeXService.producePDF(it.base64EncodedFiles()) }
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

