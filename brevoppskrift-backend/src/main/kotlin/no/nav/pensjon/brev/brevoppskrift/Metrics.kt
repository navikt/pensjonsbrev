package no.nav.pensjon.brev.brevoppskrift

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.path
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import java.time.Duration

private val probes = setOf("/isAlive", "/isReady", "/metrics")

fun observeCall(call: ApplicationCall): Boolean = call.request.path() !in probes

fun createMetrics(): PrometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT).apply {
    config().meterFilter(MeterFilter.ignoreTags("address"))
}

fun Application.configureMetrics() {
    val metrics: PrometheusMeterRegistry by dependencies
    install(MicrometerMetrics) {
        registry = metrics
        filter(::observeCall)
        distributionStatisticConfig = DistributionStatisticConfig.Builder()
            .percentilesHistogram(true)
            .minimumExpectedValue(Duration.ofMillis(50).toNanos().toDouble())
            .maximumExpectedValue(Duration.ofSeconds(60).toNanos().toDouble())
            .serviceLevelObjectives(*listOf(50L, 100, 250, 500, 1000, 2000, 5000, 10_000, 30_000, 60_000).map { Duration.ofMillis(it).toNanos().toDouble() }.toDoubleArray())
            .build()
    }
}
