package no.nav.pensjon.brev

import com.auth0.jwk.JwkProviderBuilder
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import no.nav.pensjon.brev.template.jacksonObjectMapper
import org.slf4j.LoggerFactory
import java.net.URL

data class JwtConfig(val name: String, val issuer: String, val jwksUrl: String, val audience: List<String>, val preAuthorizedApps: List<PreAuthorizedApp>?, val requireAzureAdClaims: Boolean) {
    companion object {
        private val logger = LoggerFactory.getLogger(JwtConfig::class.java)

        const val jwtStsName = "STS"
        const val jwtAzureAdName = "AZURE_AD"

        fun requireAzureADConfig() =
            JwtConfig(
                name = jwtAzureAdName,
                issuer = requireEnv("AZURE_OPENID_CONFIG_ISSUER"),
                jwksUrl = requireEnv("AZURE_OPENID_CONFIG_JWKS_URI"),
                audience = listOf(requireEnv("AZURE_APP_CLIENT_ID")),
                preAuthorizedApps = getPreAuthorizedApps(),
                requireAzureAdClaims = true,
            ).also { logger.info("AzureAD: $it") }

        fun requireStsConfig() =
            JwtConfig(
                name = jwtStsName,
                issuer = requireEnv("${jwtStsName}_ISSUER"),
                jwksUrl = requireEnv("${jwtStsName}_JWKS_URI"),
                audience = requireEnv("${jwtStsName}_ACCEPTED_AUDIENCE").split(","),
                preAuthorizedApps = null,
                requireAzureAdClaims = false,
            ).also { logger.info("STS: $it") }

        private fun getPreAuthorizedApps(): List<PreAuthorizedApp>? =
            System.getenv("AZURE_APP_PRE_AUTHORIZED_APPS")?.let {
                try {
                    jacksonObjectMapper().readValue(it)
                } catch (e: JacksonException) {
                    logger.warn("Failed to deserialize preAuthorized apps, value was: $it", e)
                    emptyList()
                }
            }
    }
}

data class PreAuthorizedApp(val name: String, val clientId: String)

fun Authentication.Configuration.brevbakerJwt(config: JwtConfig) =
    jwt(config.name) {
        realm = "brevbaker-latex-$name"
        verifier(JwkProviderBuilder(URL(config.jwksUrl)).build(), config.issuer) {
            withAnyOfAudience(*config.audience.toTypedArray())
            withIssuer(config.issuer)

            if (config.requireAzureAdClaims) {
                withClaimPresence("sub")
                withClaimPresence("exp")
                withClaimPresence("nbf")
                withClaimPresence("iat")
            }
        }
        validate {
            JWTPrincipal(it.payload)
        }
    }