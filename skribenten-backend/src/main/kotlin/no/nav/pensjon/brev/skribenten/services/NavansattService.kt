package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.Cacheomraade
import no.nav.pensjon.brev.skribenten.common.cached
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.services.HttpClientFactory.lagHttpClient
import org.slf4j.LoggerFactory

interface NavansattService {
    suspend fun harTilgangTilEnhet(ansattId: NavIdent, enhetsId: EnhetId): Boolean
    suspend fun hentNavansatt(ansattId: NavIdent): Navansatt?
    suspend fun hentNavAnsattEnhetListe(ansattId: NavIdent): List<NAVAnsattEnhet>
}

class NavansattServiceException(message: String) : ServiceException(message)

class NavansattServiceHttp(config: Config, authService: AuthService, private val cache: Cache) : NavansattService, ServiceStatus {
    private val logger = LoggerFactory.getLogger(NavansattServiceHttp::class.java)

    private val navansattUrl = config.getString("url")
    private val navansattScope = config.getString("scope")

    private val client = lagHttpClient {
        defaultRequest {
            url(navansattUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        installRetry(logger, maxRetries = 3)
        callIdAndOnBehalfOfClient(navansattScope, authService)
    }

    override suspend fun hentNavAnsattEnhetListe(ansattId: NavIdent): List<NAVAnsattEnhet> {
        return cache.cached(Cacheomraade.NAVANSATTENHET, ansattId.id) {
            val response = client.get("navansatt/${ansattId.id}/enheter")

            if (response.status.isSuccess()) {
                response.body()
            } else {
                throw NavansattServiceException("Fant ikke navansattenhet ${ansattId.id}: ${response.status} - ${response.bodyAsText()}")
            }
        }
    }

    override suspend fun harTilgangTilEnhet(ansattId: NavIdent, enhetsId: EnhetId): Boolean =
        hentNavAnsattEnhetListe(ansattId).any { enhet -> enhet.id == enhetsId }

    override suspend fun hentNavansatt(ansattId: NavIdent): Navansatt? = try {
        cache.cached(Cacheomraade.NAVANSATT, ansattId.id) {
            val response = client.get("/navansatt/${ansattId.id}")

            return@cached if (response.status.isSuccess()) {
                response.body()
            } else if (response.status == HttpStatusCode.NotFound) {
                logger.warn("Fant ikke navansatt ${ansattId.id}: ${response.status} - ${response.bodyAsText()}")
                null
            } else {
                logger.error("Fant ikke navansatt ${ansattId.id}: ${response.status} - ${response.bodyAsText()}")
                null
            }
        }
    } catch (e: Exception) {
        logger.error("Feil ved henting av navansatt ${ansattId.id}", e)
        throw e
    }

    override suspend fun ping() =
        ping("NAV Ansatt") { client.get("ping-authenticated") }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class NAVAnsattEnhet(
    val id: EnhetId,
    val navn: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Navansatt(
    val groups: List<String>,
    val navn: String,
    val fornavn: String,
    val etternavn: String,
) {
    val fulltNavn: String get() = "$fornavn $etternavn"
}


