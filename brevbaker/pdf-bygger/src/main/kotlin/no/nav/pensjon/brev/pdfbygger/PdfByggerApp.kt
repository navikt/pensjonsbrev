package no.nav.pensjon.brev.pdfbygger

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.Logger
import no.nav.brev.brevbaker.PDFRequest
import no.nav.brev.brevbaker.jackson.internalObjectMapper
import no.nav.brev.brevbaker.pdfbygger.api.LetterPDFRequest
import no.nav.pensjon.brev.pdfbygger.Metrics.configureMetrics
import no.nav.pensjon.brev.pdfbygger.typst.TypstCompileService
import no.nav.pensjon.brev.pdfbygger.typst.documentrender.TypstDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.typst.documentrender.TypstDocumentRendererV2
import org.slf4j.LoggerFactory

private val objectMapper = internalObjectMapper()

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

    configureMetrics()

    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(objectMapper))
    }

    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        filter(Metrics::skalObserveres)
        mdc("x_response_code") { it.response.status()?.value?.toString() }
    }

    install(StatusPages)

    install(CallId) {
        retrieveFromHeader("X-Request-ID")
        generate()
        verify { it.isNotEmpty() }
    }

    routing {

        post("/produserBrev") {
            val request = call.receive<PDFRequest>()
            val result = typstCompileService.createLetter {
                TypstDocumentRenderer.render(request, it)
            }
            handleResult(result, call.application.environment.log)
        }

        post("/v2/produserBrev") {
            val request = call.receive<LetterPDFRequest>()
            val result = typstCompileService.createLetter {
                TypstDocumentRendererV2.render(request, it)
            }
            handleResult(result, call.application.environment.log)
        }

        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
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

