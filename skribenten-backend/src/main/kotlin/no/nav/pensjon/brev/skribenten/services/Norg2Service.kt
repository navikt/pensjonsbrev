package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.context.CallIdFromContext
import org.slf4j.LoggerFactory

// docs: https://confluence.adeo.no/display/FEL/NORG2+-+Teknisk+beskrivelse - trykk på droppdown
class Norg2Service(val config: Config) {
    private val logger = LoggerFactory.getLogger(Norg2Service::class.java)
    private val norgUrl = config.getString("url")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(norgUrl)
        }
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        install(CallIdFromContext)
    }

    private val enhetCache = Cache<String, NavEnhet>()
    suspend fun getEnhet(enhetId: String) =
        enhetCache.cached(enhetId) {
            //https://confluence.adeo.no/pages/viewpage.action?pageId=174848376
            client.get("api/v1/enhet/$enhetId")
                .toServiceResult<NavEnhet>()
                .onError { error, statusCode -> logger.error("Fant ikke Nav-enhet $enhetId: $statusCode - $error") }
                .resultOrNull()
        }
}

data class NavEnhet(
    val enhetNr:String,
    val navn: String
)