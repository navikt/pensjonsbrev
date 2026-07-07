package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig

object HttpClientFactory {

    fun lagHttpClient(config: HttpClientConfig<CIOEngineConfig>.() -> Unit): HttpClient =
        lagHttpClient(CIO, config)

    fun lagHttpClient(engine: HttpClientEngine, config: HttpClientConfig<*>.() -> Unit): HttpClient =
        lagHttpClient(engine, config)

    private fun <C : HttpClientEngineConfig, T : HttpClientEngineFactory<C>> lagHttpClient(engine: T, config: HttpClientConfig<C>.() -> Unit): HttpClient =
        HttpClient(engine) {
            config()
        }
}