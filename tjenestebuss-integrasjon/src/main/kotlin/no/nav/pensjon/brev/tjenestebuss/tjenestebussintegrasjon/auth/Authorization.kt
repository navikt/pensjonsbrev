package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth

import com.auth0.jwk.JwkProviderBuilder
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.typesafe.config.Config
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.slf4j.LoggerFactory
import java.net.URL

private const val jwtAzureAdName = "AZURE_AD"
private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.Authentication")

data class JwtConfig(
    val name: String,
    val issuer: String,
    val jwksUrl: String,
    val clientId: String,
    val tokenUri: String,
    val clientSecret: String,
    val preAuthorizedApps: List<PreAuthorizedApp>,
    val requireAzureAdClaims: Boolean
) {
    data class PreAuthorizedApp(val name: String, val clientId: String)
}

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

fun AuthenticationConfig.tjenestebusJwt(config: JwtConfig) =
    jwt(config.name) {
        realm = "tjenestebuss-integrasjon$name"
        verifier(JwkProviderBuilder(URL(config.jwksUrl)).build(), config.issuer) {
            withAnyOfAudience(config.clientId)
            withIssuer(config.issuer)
        }
        validate {
            JWTPrincipal(it.payload)
        }

    }
