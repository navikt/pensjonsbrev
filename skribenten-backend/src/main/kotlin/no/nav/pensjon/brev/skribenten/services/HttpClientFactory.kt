package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig

object HttpClientFactory {
    fun lagHttpClient(config: HttpClientConfig<CIOEngineConfig>.() -> Unit): HttpClient =
        HttpClient(CIO) {
            config()
        }
}