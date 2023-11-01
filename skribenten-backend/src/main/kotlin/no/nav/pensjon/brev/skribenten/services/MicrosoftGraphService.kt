package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService

class MicrosoftGraphService(config: Config, authService: AzureADService) {
    private val graphServiceUrl = config.getString("url")
    private val graphServiceScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(graphServiceScope, authService) {
        defaultRequest {
            url(graphServiceUrl)
        }
        install(ContentNegotiation) {
            jackson {
                // We are only interested in onPremisesSamAccountName
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    data class SelectUsernameResponse(val onPremisesSamAccountName: String)

    suspend fun getOnPremisesSamAccountName(call: ApplicationCall): ServiceResult<String, String> =
        client.get(call, "/v1.0/me/?\$select=onPremisesSamAccountName") {
            contentType(ContentType.Application.Json)
        }.toServiceResult<SelectUsernameResponse, String>()
            .map { it.onPremisesSamAccountName }
}