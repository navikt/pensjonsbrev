package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.date.*
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


fun main(args: Array<String>) = EngineMain.main(args)

private val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM)

private fun Application.getProperty(name: String): String? =
    environment.config.propertyOrNull(name)?.getString()

@Suppress("unused")
fun Application.module() {
    val parallelism = getProperty("pdfBygger.latexParallelism")?.toInt() ?: Runtime.getRuntime().availableProcessors()
    val activityCounter = ActiveCounter()
    val laTeXService = LaTeXService(
        compileTimeout = getProperty("pdfBygger.compileTimeout")?.let { Duration.parse(it) } ?: 300.seconds,
        queueWaitTimeout = getProperty("pdfBygger.compileQueueWaitTimeout")?.let { Duration.parse(it) } ?: 4.seconds,
        latexParallelism = parallelism,
        latexCommand = getProperty("pdfBygger.latexCommand") ?: "xelatex --interaction=nonstopmode -halt-on-error"
    )

    log.info("Target parallelism : $parallelism")
    environment.monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
    }

    install(ContentNegotiation) {
        jackson()
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

    install(MicrometerMetrics) {
        registry = prometheusMeterRegistry
    }

    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        val ignorePaths = setOf("/isAlive", "/isReady", "/metrics")
        filter {
            !ignorePaths.contains(it.request.path())
        }
        // TODO: Legg til når jeg har funnet ut av hvorfor loggene forsvant fra kibana
//        mdc("status_code") { it.response.status()?.value?.toString() ?: "-" }
//        mdc("response_time") { "${it.processingTimeMillis(::getTimeMillis)}ms" }
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
        post("/compile") {
            val logger = call.application.environment.log

            val input = call.receive<PdfCompilationInput>()
            val result = activityCounter.count {
                laTeXService.producePDF(input.files)
            }

            when (result) {
                is PDFCompilationResponse.Base64PDF -> call.respond(result)
                is PDFCompilationResponse.Failure.Client -> {
                    logger.info("Client error: ${result.reason}")
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
                    logger.error(result.reason)
                    call.respond(HttpStatusCode.ServiceUnavailable, result)
                }
            }
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

