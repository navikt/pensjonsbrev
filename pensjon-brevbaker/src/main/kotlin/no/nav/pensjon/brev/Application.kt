package no.nav.pensjon.brev

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.metrics.micrometer.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.dto.description
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.brevbakerConfig
import java.io.Writer
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val latexCompilerService = LaTeXCompilerService(System.getenv("PDF_BYGGER_URL") ?: "http://127.0.0.1:8081")
private val base64Decoder = Base64.getDecoder()

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    install(ContentNegotiation) {
        jackson {
            brevbakerConfig()
        }
    }

    install(MicrometerMetrics) {
        registry = prometheusMeterRegistry
    }

    routing {

        get("/templates") {
            call.respond(TemplateResource.getTemplates())
        }

        get("/templates/{name}") {
            val template = TemplateResource.getTemplate(call.parameters["name"]!!)?.description()
            if (template == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(template)
            }
        }

        post("/letter") {
            val letterRequest = call.receive<LetterRequest>()

            val pdfBase64 = LetterResource.create(letterRequest).render()
                .let { PdfCompilationInput(it.base64EncodedFiles()) }
                .let { latexCompilerService.producePDF(it) }
                .let { base64Decoder.decode(it.base64PDF) }

            call.respondBytes(pdfBase64, ContentType.Application.Pdf)
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

