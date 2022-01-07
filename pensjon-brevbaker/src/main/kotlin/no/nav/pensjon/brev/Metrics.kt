package no.nav.pensjon.brev

import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Writer

object Metrics {
    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM)

    suspend fun writeMetrics004(writer: Writer, registry: PrometheusMeterRegistry) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                TextFormat.write004(writer, registry.prometheusRegistry.metricFamilySamples())
            }
        }
    }
}