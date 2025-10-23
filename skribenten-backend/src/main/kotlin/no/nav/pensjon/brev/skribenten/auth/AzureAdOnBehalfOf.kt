package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.bearerAuth

class AzureAdOnBehalfOfAuthorizationException(error: TokenResponse.ErrorResponse): Exception(
    "Feil ved token-utveksling correlation_id: ${error.correlation_id} Description:${error.error_description}"
)

class AzureAdOnBehalfOfConfig {
    lateinit var scope: String
    lateinit var authService: AuthService
}

val AzureAdOnBehalfOf = createClientPlugin("PrincipalFromContext_AzureAdOnBehalfOf", ::AzureAdOnBehalfOfConfig) {
    val scope = pluginConfig.scope
    val authService = pluginConfig.authService

    onRequest { request, _ ->
        val principal = PrincipalInContext.require()
        request.apply { bearerAuth(authService.getOnBehalfOfToken(principal, scope).accessToken)}
    }
}