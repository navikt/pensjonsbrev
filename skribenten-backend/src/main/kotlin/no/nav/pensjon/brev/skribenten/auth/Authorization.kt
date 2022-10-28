package no.nav.pensjon.brev.skribenten.auth

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.interfaces.Payload
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.*
import com.typesafe.config.Config
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.slf4j.LoggerFactory
import java.net.URL

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.skribenten.auth.Authentication")

data class JwtConfig(val name: String, val issuer: String, val jwksUrl: String, val clientId: String, val tokenUri: String, val clientSecret: String, val preAuthorizedApps: List<PreAuthorizedApp>, val requireAzureAdClaims: Boolean) {
    data class PreAuthorizedApp(val name: String, val clientId: String)
}

private const val jwtAzureAdName = "AZURE_AD"

fun Config.requireAzureADConfig() =
    getConfig("azureAD").let {
        JwtConfig(
            name = jwtAzureAdName,
            issuer = it.getString("issuer"),
            jwksUrl = it.getString("jwksUrl"),
            clientId = it.getString("clientId"),
            tokenUri = it.getString("tokenEndpoint"),
            clientSecret = it.getString("clientSecret"),
            preAuthorizedApps = parsePreAuthorizedApps(it.getString("preAuthApps")),
            requireAzureAdClaims = true
        )
    }.also { logger.debug("AzureAD: $it") }

private fun parsePreAuthorizedApps(preAuthApps: String): List<JwtConfig.PreAuthorizedApp> =
    try {
        jacksonObjectMapper().readValue(preAuthApps)
    } catch (e: JacksonException) {
        logger.error("Failed to deserialize preAuthorizedApps, value was: $preAuthApps", e)
        emptyList()
    }


fun AuthenticationConfig.skribentenJwt(config: JwtConfig) =
    jwt(config.name) {
        realm = "skribenten-$name"
        verifier(JwkProviderBuilder(URL(config.jwksUrl)).build(), config.issuer) {
            withAnyOfAudience(config.clientId)
            withIssuer(config.issuer)

            if (config.requireAzureAdClaims) {
                withClaimPresence("sub")
                withClaimPresence("exp")
                withClaimPresence("nbf")
                withClaimPresence("iat")
            }
        }
        validate {
            val azp = it["azp"]

            if (config.preAuthorizedApps.any { app -> app.clientId == azp }) {
                UserPrincipal(userAccessToken(), it.payload)
            } else {
                logger.info("Invalid authorization - claim 'azp' is not a preAuthorizedApp: $azp")
                null
            }
        }
    }

private fun ApplicationCall.userAccessToken(): UserAccessToken =
    request.parseAuthorizationHeader().let {
        if (it is HttpAuthHeader.Single && it.authScheme == "Bearer") {
            UserAccessToken(it.blob)
        } else throw InvalidAuthorization("Requires 'Bearer' authorization scheme, was: ${it?.authScheme}")
    }

class InvalidAuthorization(msg: String, cause: Throwable? = null): Exception(msg, cause)

@JvmInline
value class UserAccessToken(val token: String)
data class UserPrincipal(val accessToken: UserAccessToken, val jwtPayload: Payload): Principal {
    private val onBehalfOfTokens = mutableMapOf<String, TokenResponse.OnBehalfOfToken>()

    fun getOnBehalfOfToken(scope: String): TokenResponse.OnBehalfOfToken? = onBehalfOfTokens[scope]
    fun setOnBehalfOfToken(scope: String, token: TokenResponse.OnBehalfOfToken) {
        onBehalfOfTokens[scope] = token
    }
}