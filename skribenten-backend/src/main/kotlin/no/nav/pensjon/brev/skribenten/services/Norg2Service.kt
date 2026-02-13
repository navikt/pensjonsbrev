package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.Cacheomraade
import no.nav.pensjon.brev.skribenten.cached
import no.nav.pensjon.brev.skribenten.context.CallIdFromContext
import org.slf4j.LoggerFactory

interface Norg2Service {
    suspend fun getEnhet(enhetId: EnhetId): NavEnhet
}

// docs: https://confluence.adeo.no/display/FEL/NORG2+-+Teknisk+beskrivelse - trykk på droppdown
class Norg2ServiceHttp(val config: Config, val cache: Cache) : Norg2Service {
    private val logger = LoggerFactory.getLogger(Norg2ServiceHttp::class.java)
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

    override suspend fun getEnhet(enhetId: EnhetId): NavEnhet =
        cache.cached(Cacheomraade.NORG, enhetId) {
            //https://confluence.adeo.no/pages/viewpage.action?pageId=174848376
            val response = client.get("api/v1/enhet/${enhetId.value}")

            if (response.status.isSuccess()) {
                response.body()
            } else {
                logger.error("Feil ved henting av enhet $enhetId. Status: ${response.status} Message: ${response.bodyAsText()}")
                throw Norg2EnhetException(enhetId)
            }
        }
}

data class NavEnhet(
    val enhetNr: EnhetId,
    val navn: String
)

@JvmInline
value class EnhetId(val value: String) {
    init {
        require(value.length == 4) { "Vi forventer at enhetsnummer er fire sifre, dette var ${value.length} langt"}
    }
}

class Norg2EnhetException(enhetId: EnhetId) : IllegalStateException("Fant ikke enhet med id $enhetId i NORG2")