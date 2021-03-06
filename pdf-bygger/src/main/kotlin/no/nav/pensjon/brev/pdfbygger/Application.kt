package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Writer

val laTeXService = LaTeXService()
fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(ContentNegotiation) {
        jackson()
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
            val result = call.receive<PdfCompilationInput>()
                .let { laTeXService.producePDF(it.files) }

            when(result) {
                is PDFCompilationResponse.Base64PDF -> call.respond(result)
                is PDFCompilationResponse.Failure.Client -> call.respond(HttpStatusCode.BadRequest, result)
                is PDFCompilationResponse.Failure.Server -> call.respond(HttpStatusCode.InternalServerError, result)
            }
        }

        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/metrics") {
            call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
                writeMetrics004(this, prometheusMeterRegistry)
            }
        }
    }
}

suspend fun writeMetrics004(writer: Writer, registry: PrometheusMeterRegistry) {
    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            TextFormat.write004(writer, registry.prometheusRegistry.metricFamilySamples())
        }
    }
}

val prometheusMeterRegistry =
    PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM)

