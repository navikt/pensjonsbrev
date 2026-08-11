package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine

object HttpClientFactory {

    fun lagHttpClient(engine: HttpClientEngine, config: HttpClientConfig<*>.() -> Unit): HttpClient =
        HttpClient(engine) {
            config()
        }

}