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

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.getPropertyOrNull(name: String): String? =
    environment.config.propertyOrNull(name)?.getString()

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

    routing {
        get("/metrics") {
            call.respond(prometheusMeterRegistry.scrape())
        }
    }

    if (getPropertyOrNull("pdfBygger.isAsyncWorker")?.toBoolean() == true) {
        kafkaModule()
    } else {
        restModule(prometheusMeterRegistry)
    }
}
