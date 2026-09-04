package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpStatusCode

class AzureAdOnBehalfOfAuthorizationException(val error: TokenResponse.ErrorResponse) : Exception(
    "Feil ved token-utveksling correlation_id: ${error.correlation_id} Description:${error.error_description}"
) {
    /**
     * "invalid_grant" er OAuth2-standardkoden Azure AD bruker når selve assertion/token som sendes inn
     * i OBO-kallet er utløpt, ugyldig eller tilbakekalt – dvs. brukerens innloggingssesjon er ikke
     * lenger gyldig og de må logge inn på nytt. Alle andre feilkoder (invalid_client,
     * unauthorized_client, invalid_scope, osv.) skyldes feilkonfigurasjon av vår egen app.
     */
    val status: HttpStatusCode =
        if (error.error == "invalid_grant") HttpStatusCode.Unauthorized else HttpStatusCode.InternalServerError
}

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