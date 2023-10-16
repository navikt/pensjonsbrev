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
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.*
import io.prometheus.client.CollectorRegistry
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds


fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    val parallelism = Runtime.getRuntime().availableProcessors()
    val activityCounter = ActiveCounter()
    val laTeXService = LaTeXService(this.log, timeout = 300.seconds)

    this.log.info("Tilgjengelige kjerner: $parallelism")
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

    val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM)
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
    }

    install(StatusPages) {
        exception<JacksonException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize json body: unknown reason")
        }
    }

    install(CallId) {
        retrieveFromHeader("Nav-Call-Id")
        generate()
        verify { it.isNotEmpty() }
    }

    routing {
        post("/compile") {
            val logger = call.application.environment.log

            val input = call.receive<PdfCompilationInput>()
            val result = activityCounter.count {
                withTimeout(300.seconds) {
                    laTeXService.producePDF(input.files)
                }
            }

            when (result) {
                is PDFCompilationResponse.Base64PDF -> call.respond(result)
                is PDFCompilationResponse.Failure.Client -> {
                    logger.info("Client error: $result")
                    call.respond(HttpStatusCode.BadRequest, result)
                }

                is PDFCompilationResponse.Failure.Server -> {
                    logger.error("Server error: $result")
                    call.respond(HttpStatusCode.InternalServerError, result)
                }

                is PDFCompilationResponse.Failure.Timeout -> {
                    logger.error("Server error: $result")
                    call.respond(HttpStatusCode.GatewayTimeout, result)
                }
            }
        }

        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            val currentActivity = activityCounter.currentCount()
            val msg = "Activity: $currentActivity, Target: $parallelism"
            call.application.log.info(msg)

            if (currentActivity > parallelism) {
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


