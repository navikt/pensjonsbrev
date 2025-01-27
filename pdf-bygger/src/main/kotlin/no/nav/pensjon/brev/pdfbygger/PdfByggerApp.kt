package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.*
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
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.date.getTimeMillis
import io.micrometer.core.instrument.Tag
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import no.nav.pensjon.brev.pdfbygger.api.ActiveCounter
import no.nav.pensjon.brev.pdfbygger.kafka.PdfRequestConsumer
import no.nav.pensjon.brev.pdfbygger.latex.BlockingLatexService
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


fun main(args: Array<String>) = EngineMain.main(args)

fun Application.getProperty(name: String): String? =
    environment.config.propertyOrNull(name)?.getString()

@Suppress("unused")
fun Application.module() {
    monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
    }

    val latexCompileService = LatexCompileService(
        compileTimeout = getProperty("pdfBygger.latex.compileTimeout")?.let { Duration.parse(it) } ?: 300.seconds,
        latexCommand = getProperty("pdfBygger.latex.latexCommand") ?: "xelatex --interaction=nonstopmode -halt-on-error",
        tmpBaseDir = Path.of(environment.config.property("pdfBygger.latex.compileTmpDir").getString()),
    )

    if (getProperty("pdfBygger.isAsyncWorker")?.toBoolean() == true) {
        val kafkaConfig = environment.config.config("pdfBygger.kafka")
        val pdfRequestConsumer = PdfRequestConsumer(kafkaConfig, latexCompileService)
        @OptIn(DelicateCoroutinesApi::class)
        pdfRequestConsumer.flow().launchIn(GlobalScope)
    } else {
        val parallelism = getProperty("pdfBygger.latex.latexParallelism")?.toInt() ?: Runtime.getRuntime().availableProcessors()
        val blockingLatexService = BlockingLatexService(
            queueWaitTimeout = getProperty("pdfBygger.latex.compileQueueWaitTimeout")?.let { Duration.parse(it) } ?: 4.seconds,
            latexParallelism = parallelism,
            latexCompileService = latexCompileService,
        )

        log.info("Target parallelism : $parallelism")

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

        val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        install(MicrometerMetrics) {
            registry = prometheusMeterRegistry
        }
        val activityCounter = ActiveCounter.Companion(
            prometheusMeterRegistry,
            "pensjonsbrev_pdf_compile_active",
            listOf(Tag.of("hpa", "value"))
        )

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
            post("/compile") {
                val logger = call.application.environment.log

                val input = call.receive<PdfCompilationInput>()
                val result = activityCounter.count {
                    blockingLatexService.producePDF(input.files)
                }

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
                        call.respond(HttpStatusCode.InternalServerError, result)
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

}

