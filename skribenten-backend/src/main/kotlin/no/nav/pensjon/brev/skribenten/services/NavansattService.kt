package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import org.slf4j.LoggerFactory

class NavansattService(config: Config, authService: AzureADService) : ServiceStatus {
    private val logger = LoggerFactory.getLogger(NavansattService::class.java)

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

    suspend fun hentNavAnsattEnhetListe(ansattId: String): ServiceResult<List<NAVAnsattEnhet>> {
        return client.get("navansatt/$ansattId/enheter").toServiceResult<List<NAVAnsattEnhet>>()
    }

    suspend fun harTilgangTilEnhet(ansattId: String, enhetsId: String): ServiceResult<Boolean> =
        hentNavAnsattEnhetListe(ansattId)
            .map { it.any { enhet -> enhet.id == enhetsId } }

    private val navansattCache = Cache<String, Navansatt>()
    suspend fun hentNavansatt(ansattId: String): Navansatt? =
        navansattCache.cached(ansattId) {
            client.get("/navansatt/$ansattId").toServiceResult<Navansatt>()
                .onError { error, statusCode -> logger.error("Fant ikke navansatt $ansattId: $statusCode - $error") }
                .resultOrNull()
        }

    override val name = "Nav Ansatt"

    override suspend fun ping(): ServiceResult<Boolean> =
        client.get("ping-authenticated").toServiceResult<String>().map { true }
}


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


