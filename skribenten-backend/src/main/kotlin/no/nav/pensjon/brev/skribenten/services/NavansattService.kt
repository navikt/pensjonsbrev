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

    suspend fun hentNavAnsattEnhetListe(call: ApplicationCall, ansattId: String): ServiceResult<HentEnheterDto.Success, HentEnheterDto.Failure> =
        client.get(call, "navansatt/$ansattId/enheter")
            .toServiceResult<HentEnheterDto.Success, HentEnheterDto.Failure>()
            .map { success ->
                HentEnheterDto.Success(success.enheter)
            }.catch { error ->
                HentEnheterDto.Failure(error.message, error.type)
            }
}

sealed class HentEnheterDto {
    data class Success(val enheter: List<NAVEnhet>) : HentEnheterDto()
    data class Failure(val message: String?, val type: FailureType?) : HentEnheterDto()

    enum class FailureType {
        IKKE_FUNNET,
        IKKE_TILGANG,
        GENERELL
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class NAVEnhet(
        val enhetNr: String,
        val navn: String,
    )
}
