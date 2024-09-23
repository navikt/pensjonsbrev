package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.callId
import org.slf4j.LoggerFactory

class Norg2Service(config: Config) {
    private val logger = LoggerFactory.getLogger(Norg2Service::class.java)

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(config.getString("url"))
        }
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    private val enhetCache = Cache<String, NAVEnhet>()
    suspend fun getEnhet(call: ApplicationCall, enhetId: String) =
        enhetCache.cached(enhetId) {
            client.get("/api/v1/enhet/$enhetId") {
                headers { callId(call) }
            }.toServiceResult<NAVEnhet>()
                .onError { error, statusCode -> logger.error("Fant ikke NAV Enhet $enhetId: $statusCode - $error") }
                .resultOrNull()
        }
}