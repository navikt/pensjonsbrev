package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.Cacheomraade
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.cached
import org.slf4j.LoggerFactory
import kotlin.jvm.java

interface NavansattService {
    suspend fun harTilgangTilEnhet(ansattId: String, enhetsId: String): ServiceResult<Boolean>
    suspend fun hentNavansatt(ansattId: String): Navansatt?
    suspend fun hentNavAnsattEnhetListe(ansattId: String): ServiceResult<NavAnsattEnheter>
}

class NavansattServiceHttp(config: Config, authService: AuthService, private val cache: Cache) : NavansattService, ServiceStatus {
    private val logger = LoggerFactory.getLogger(NavansattServiceHttp::class.java)

    private val navansattUrl = config.getString("url")
    private val navansattScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(navansattUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        callIdAndOnBehalfOfClient(navansattScope, authService)
    }

    override suspend fun hentNavAnsattEnhetListe(ansattId: String): ServiceResult<NavAnsattEnheter> {
        val cached: NavAnsattEnheter? = cache.cached(Cacheomraade.NAVANSATTENHET, ansattId) {
            client.get("navansatt/$ansattId/enheter").toServiceResult<List<NAVAnsattEnhet>>()
                .onError { error, statusCode -> logger.error("Fant ikke navansattenhet $ansattId: $statusCode - $error") }
                .resultOrNull()
                ?.let { NavAnsattEnheter(it) }
        }
        return cached?.let { ServiceResult.Ok(it) }
            ?: ServiceResult.Error(
            "Ingen treff",
            HttpStatusCode.ExpectationFailed
        )
    }

    override suspend fun harTilgangTilEnhet(ansattId: String, enhetsId: String): ServiceResult<Boolean> =
        hentNavAnsattEnhetListe(ansattId)
            .map { it.enheter }
            .map { it.any { enhet -> enhet.id == enhetsId } }

    override suspend fun hentNavansatt(ansattId: String): Navansatt? = try {
        cache.cached(Cacheomraade.NAVANSATT, ansattId) {
            client.get("/navansatt/$ansattId").toServiceResult<Navansatt>()
                .onError { error, statusCode -> logger.error("Fant ikke navansatt $ansattId: $statusCode - $error") }
                .resultOrNull()
        }
    } catch (e: Exception) {
        logger.error("Feil ved henting av navansatt $ansattId", e)
        throw e
    }

    override val name = "Nav Ansatt"

    override suspend fun ping(): ServiceResult<Boolean> =
        client.get("ping-authenticated").toServiceResult<String>().map { true }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class NavAnsattEnheter(
    val enheter: List<NAVAnsattEnhet>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NAVAnsattEnhet(
    val id: String,
    val navn: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Navansatt(
    val groups: List<String>,
    val navn: String,
    val fornavn: String,
    val etternavn: String,
)


