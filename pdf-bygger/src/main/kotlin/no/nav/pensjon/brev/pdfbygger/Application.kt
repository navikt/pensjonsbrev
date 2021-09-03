package no.nav.pensjon.brev.pdfbygger

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import io.ktor.jackson.*
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.event.Level
import java.io.Writer

val laTeXService = LaTeXService()
fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        jackson()
    }

    install(MicrometerMetrics) {
        registry = prometheusMeterRegistry
    }

    install(CallLogging) {
        level = Level.INFO
    }

    routing {
        post("/compile") {
            try {
                val pdfCompilationInput = call.receive<PdfCompilationInput>()
                call.respond(laTeXService.producePDF(pdfCompilationInput.files))
            } catch (e: PdfCompilationException) {
                call.respond(PDFCompilationOutput(e.compilationLog))
            } catch (e: Exception) {
                call.respond(PDFCompilationOutput(e.stackTraceToString()))
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

