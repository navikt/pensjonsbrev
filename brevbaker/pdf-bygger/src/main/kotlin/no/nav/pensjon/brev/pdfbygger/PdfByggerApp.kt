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
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.acceptItems
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.typst.TypstCompileService
import no.nav.pensjon.brev.pdfbygger.typst.documentrender.TypstDocumentRenderer
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream

fun main(args: Array<String>) = EngineMain.main(args)

fun ApplicationConfig.getProperty(name: String): String =
    property(name).getString()

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.pdfbygger.PdfByggerApp")

@Suppress("unused")
fun Application.module() {
    try {
        setUp(TypstCompileService())
    } catch (e: Exception) {
        logger.error(e.message, e)
        throw e
    }
}

internal fun Application.setUp(typstCompileService: TypstCompileService) {
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


    install(ContentNegotiation) {
        jackson {
            pdfByggerConfig()
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
            val request = call.receive<PDFRequest>()
            val stream = ByteArrayOutputStream()
            val result = typstCompileService.createLetter(stream) {
                TypstDocumentRenderer.render(request, it)
            }
            val logger = call.application.environment.log
            when (result) {
                is PDFCompilationResponse.Success -> {
                    if (call.request.acceptItems().any { ContentType.Application.Pdf.match(it.value) }) {
                        call.respondOutputStream(ContentType.Application.Pdf) { stream.writeTo(this) }
                    } else {
                        call.respond(PDFCompilationOutput(stream.toByteArray()))
                    }
                }
                is PDFCompilationResponse.Failure.Client -> {
                    logger.warn("Client error: ${result.reason}")
                    if (result.output?.isNotBlank() == true) {
                        logger.warn("Output: ${result.output}")
                    }
                    if (result.error?.isNotBlank() == true) {
                        logger.warn("Error: ${result.error}")
                    }
                    call.respond(HttpStatusCode.BadRequest, result)
                }

                is PDFCompilationResponse.Failure.Server -> {
                    logger.error(result.reason)
                    call.respond(HttpStatusCode.InternalServerError, result)
                }
            }
        }

        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }
    }

}
