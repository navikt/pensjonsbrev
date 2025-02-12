package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

object Metrics {
    private val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    fun Application.configureMetrics() {
        install(MicrometerMetrics) {
            registry = prometheusRegistry
        }
        routing {
            get("/metrics") {
                call.respond(prometheusRegistry.scrape())
            }
        }
    }
}
