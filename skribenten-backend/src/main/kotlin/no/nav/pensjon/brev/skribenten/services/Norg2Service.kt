package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import no.nav.pensjon.brev.skribenten.NoAuthClientConfig
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.utils.io.core.Closeable
import no.nav.pensjon.brev.skribenten.SkribentenConfig
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.Cacheomraade
import no.nav.pensjon.brev.skribenten.common.cached
import no.nav.pensjon.brev.skribenten.context.CallIdFromContext
import no.nav.pensjon.brev.skribenten.services.HttpClientFactory.lagHttpClient
import org.slf4j.LoggerFactory

interface Norg2Service {
    suspend fun getEnhet(enhetId: EnhetId): NavEnhet
}

// docs: https://confluence.adeo.no/display/FEL/NORG2+-+Teknisk+beskrivelse - trykk på droppdown
class Norg2ServiceHttp(config: NoAuthClientConfig, val cache: Cache) : Norg2Service, Closeable {
    private val logger = LoggerFactory.getLogger(Norg2ServiceHttp::class.java)

    @Suppress("unused") // Brukes av ktor-di
    constructor(config: SkribentenConfig, cache: Cache): this(config.services.norg2, cache)

    private val norgUrl = config.url

    private val client = lagHttpClient {
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

    override fun close() { client.close() }
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
    override fun toString() = value
}

class Norg2EnhetException(enhetId: EnhetId) : IllegalStateException("Fant ikke enhet med id $enhetId i NORG2")