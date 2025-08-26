package no.nav.pensjon.brev.pdfbygger.api

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import io.micrometer.core.instrument.Tag
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.latex.BlockingLatexService
import no.nav.pensjon.brev.pdfbygger.latex.LATEX_CONFIG_PATH
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocumentRenderer
import no.nav.pensjon.brev.pdfbygger.pdfByggerConfig

fun Application.restModule(
    prometheusMeterRegistry: PrometheusMeterRegistry
) {
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