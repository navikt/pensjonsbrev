package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.callid.CallId
import io.ktor.http.HttpHeaders
import no.nav.pensjon.brev.skribenten.Metrics

object HttpClientFactory {

    fun lagHttpClient(engine: HttpClientEngine, config: HttpClientConfig<*>.() -> Unit): HttpClient =
        HttpClient(engine) {
            install(CallId) {
                addToHeader(HttpHeaders.XRequestId)
                addToHeader("Nav-Call-Id")
            }
            install(HttpClientMetrics) {
                registry = Metrics.registry
            }
            config()
        }

}