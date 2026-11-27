package no.nav.pensjon.brev.brevoppskrift.services

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.micrometer.core.instrument.MeterRegistry
import kotlin.time.Duration.Companion.seconds

enum class ExternalService(val timeout: kotlin.time.Duration) { VERTEX(20.seconds), SLACK(3.seconds), GITHUB(10.seconds) }

object HttpClientFactory {
    fun lagHttpClient(engine: HttpClientEngine, metrics: MeterRegistry, service: ExternalService): HttpClient =
        HttpClient(engine) {
            followRedirects = false
            install(HttpTimeout) {
                requestTimeoutMillis = service.timeout.inWholeMilliseconds
                connectTimeoutMillis = 5.seconds.inWholeMilliseconds
                socketTimeoutMillis = service.timeout.inWholeMilliseconds
            }
            install(HttpClientMetrics) {
                registry = metrics
                this.service = service
            }
        }
}
