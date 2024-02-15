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
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent

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

    suspend fun harAnsattTilgangTilEnhet(call: ApplicationCall, ansattId: String, enhetsId: String): ServiceResult<Boolean> {
        return when (val enheter = hentNavAnsattEnhetListe(call, ansattId)) {
            is ServiceResult.Error -> ServiceResult.Error(error = enheter.error, statusCode = enheter.statusCode)
            is ServiceResult.Ok -> ServiceResult.Ok(result = enheter.result.any { it.id == enhetsId })
        }
    }

    suspend fun harAnsattTilgangTilEnhet(call: ApplicationCall, enhetsId: String): Boolean {
        return harAnsattTilgangTilEnhet(
            call = call,
            ansattId = fetchLoggedInNavIdent(call = call),
            enhetsId = enhetsId
        ).let { when (it) {
            is ServiceResult.Error -> false
            is ServiceResult.Ok -> it.result
        }}
    }

    private fun fetchLoggedInNavIdent(call: ApplicationCall): String {
        return call.getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
    }
}


@JsonIgnoreProperties(ignoreUnknown = true)
data class NAVEnhet(
    val id: String,
    val navn: String,
)
