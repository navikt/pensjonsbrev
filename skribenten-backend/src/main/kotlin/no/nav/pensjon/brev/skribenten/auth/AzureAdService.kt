package no.nav.pensjon.brev.skribenten.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

class UnauthorizedException(msg: String) : Exception(msg)

data class OnBehalfOfToken(@JsonProperty("access_token") val accessToken: String, @JsonProperty("refresh_token") val refreshToken: String) {
    fun isValid(): Boolean = true
}

class AzureAdService(private val jwtConfig: JwtConfig) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    private suspend fun exchangeToken(accessToken: UserAccessToken, scope: String): OnBehalfOfToken =
        client.submitForm(
            url = jwtConfig.tokenUri,
            formParameters = Parameters.build {
                append("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                append("client_id", jwtConfig.clientId)
                append("client_secret", jwtConfig.clientSecret)
                append("assertion", accessToken.token)
                append("scope", scope)
                append("requested_token_use", "on_behalf_of")
            }
        ) {
            headers { append(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded) }
        }.body()

    suspend fun getOnBehalfOfToken(call: ApplicationCall, scope: String): OnBehalfOfToken {
        val principal: UserPrincipal = call.authentication.principal() ?: throw UnauthorizedException("ApplicationCall doesn't have a UserPrincipal")

        return principal.getOnBehalfOfToken(scope)?.takeIf { it.isValid() }
            ?: exchangeToken(principal.accessToken, scope).also { principal.setOnBehalfOfToken(scope, it) }
    }
}