package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.utils.io.core.Closeable

object HttpClientFactory : Closeable {
    private val clients = mutableListOf<HttpClient>()

    @Synchronized
    fun lagHttpClient(config: HttpClientConfig<CIOEngineConfig>.() -> Unit): HttpClient =
        lagHttpClient(CIO, config)

    @Synchronized
    fun lagHttpClient(engine: HttpClientEngine, config: HttpClientConfig<*>.() -> Unit): HttpClient =
        HttpClient(engine) {
            config()
        }.also { clients.add(it) }

    @Synchronized
    private fun <C : HttpClientEngineConfig, T : HttpClientEngineFactory<C>> lagHttpClient(engine: T, config: HttpClientConfig<C>.() -> Unit): HttpClient =
        HttpClient(engine) {
            config()
        }.also { clients.add(it) }

    @Synchronized
    override fun close() {
        try {
            clients.forEach { it.close() }
        } finally {
            clients.clear()
        }
    }
}