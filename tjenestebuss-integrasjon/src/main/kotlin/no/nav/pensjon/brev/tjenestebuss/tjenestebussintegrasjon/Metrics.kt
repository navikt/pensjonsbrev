package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.*
import io.prometheus.client.CollectorRegistry

object Metrics {
    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM)

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