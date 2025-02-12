package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.plugins.api.*
import io.ktor.client.request.*

class AzureAdOnBehalfOfAuthorizationException(error: TokenResponse.ErrorResponse) : Exception(
    "Feil ved token-utveksling correlation_id: ${error.correlation_id} Description:${error.error_description}",
)

class AzureAdOnBehalfOfConfig {
    lateinit var scope: String
    lateinit var authService: AzureADService
}

val AzureAdOnBehalfOf =
    createClientPlugin("PrincipalFromContext_AzureAdOnBehalfOf", ::AzureAdOnBehalfOfConfig) {
        val scope = pluginConfig.scope
        val authService = pluginConfig.authService

        onRequest { request, _ ->
            val principal = PrincipalInContext.require()

            when (val tokenResponse = authService.getOnBehalfOfToken(principal, scope)) {
                is TokenResponse.ErrorResponse -> throw AzureAdOnBehalfOfAuthorizationException(tokenResponse)
                is TokenResponse.OnBehalfOfToken ->
                    request.apply {
                        bearerAuth(tokenResponse.accessToken)
                    }
            }
        }
    }
