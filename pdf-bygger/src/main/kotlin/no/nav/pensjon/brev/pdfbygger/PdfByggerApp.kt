package no.nav.pensjon.brev.pdfbygger

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.pdfbygger.api.restModule
import no.nav.pensjon.brev.pdfbygger.kafka.kafkaModule
import no.nav.pensjon.brev.pdfbygger.latex.LatexCompileService
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


fun main(args: Array<String>) = EngineMain.main(args)

fun Application.getPropertyOrNull(name: String): String? =
    environment.config.propertyOrNull(name)?.getString()

fun ApplicationConfig.getPropertyOrNull(name: String): String? =
    propertyOrNull(name)?.getString()

fun ApplicationConfig.getProperty(name: String): String =
    property(name).getString()

@Suppress("unused")
fun Application.module() {
    monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
    }

    val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    install(MicrometerMetrics) {
        registry = prometheusMeterRegistry
    }

    val latexCompileService = LatexCompileService(
        compileTimeout = getPropertyOrNull("pdfBygger.latex.compileTimeout")?.let { Duration.parse(it) } ?: 300.seconds,
        latexCommand = getPropertyOrNull("pdfBygger.latex.latexCommand")
            ?: "xelatex --interaction=nonstopmode -halt-on-error",
        tmpBaseDir = Path.of(environment.config.property("pdfBygger.latex.compileTmpDir").getString()),
    )

    routing {
        get("/metrics") {
            call.respond(prometheusMeterRegistry.scrape())
        }
    }

    if (getPropertyOrNull("pdfBygger.isAsyncWorker")?.toBoolean() == true) {
        kafkaModule(latexCompileService)
    } else {
        restModule(latexCompileService, prometheusMeterRegistry)
    }

}

