package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService

class NavansattService(config: Config, authService: AzureADService) {

    private val navansattUrl = config.getString("url")
    private val navansattScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(navansattScope, authService) {
        defaultRequest {
            url(navansattUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    suspend fun hentNavAnsattEnhetListe(call: ApplicationCall, ansattId: String): ServiceResult<List<NAVEnhet>> {
        return client.get(call, "navansatt/$ansattId/enheter").toServiceResult<List<NAVEnhet>>()
    }

    suspend fun hentGruppetilgang(call: ApplicationCall, ansattId: String): ServiceResult<Gruppetilgang> {
        return client.get(call, "/navansatt/$ansattId").toServiceResult<Gruppetilgang>()
    }
}


@JsonIgnoreProperties(ignoreUnknown = true)
data class NAVEnhet(
    val id: String,
    val navn: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Gruppetilgang(
    val groups: List<String>
)
