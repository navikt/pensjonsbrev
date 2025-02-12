package no.nav.pensjon.brev

import com.auth0.jwk.JwkProviderBuilder
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import no.nav.pensjon.brev.template.jacksonObjectMapper
import org.slf4j.LoggerFactory
import java.net.URL

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.Authorization")

data class JwtConfig(val name: String, val issuer: String, val jwksUrl: String, val audience: List<String>, val preAuthorizedApps: List<PreAuthorizedApp>?, val requireAzureAdClaims: Boolean) {
    companion object {

        private const val JWT_AZURE_AD_NAME = "AZURE_AD"

        fun requireAzureADConfig(azureAdConfig: ApplicationConfig) =
            JwtConfig(
                name = JWT_AZURE_AD_NAME,
                issuer = azureAdConfig.property("issuer").getString(),
                jwksUrl = azureAdConfig.property("jwksUrl").getString(),
                audience = listOf(azureAdConfig.property("clientId").getString()),
                preAuthorizedApps = getPreAuthorizedApps(),
                requireAzureAdClaims = true,
            )

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

fun AuthenticationConfig.brevbakerJwt(config: JwtConfig) =
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
            val azp = it["azp"]

            if (config.preAuthorizedApps == null || config.preAuthorizedApps.any { app -> app.clientId == azp }) {
                JWTPrincipal(it.payload)
            } else {
                logger.info("Invalid authorization - claim 'azp' is not a preAuthorizedApp: $azp")
                null
            }
        }
    }