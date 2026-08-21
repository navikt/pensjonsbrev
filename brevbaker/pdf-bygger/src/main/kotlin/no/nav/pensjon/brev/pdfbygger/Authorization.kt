package no.nav.pensjon.brev.pdfbygger

import com.auth0.jwk.JwkProviderBuilder
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import no.nav.brev.brevbaker.serialization.internalObjectMapper
import org.slf4j.LoggerFactory
import java.net.URI

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.pdfbygger.Authorization")

data class JwtConfig(
    val name: String,
    val issuer: String,
    val jwksUrl: String,
    val audience: List<String>,
    val preAuthorizedApps: List<PreAuthorizedApp>?,
) {
    companion object {
        private const val jwtAzureAdName = "AZURE_AD"

        fun requireAzureADConfig(azureAdConfig: ApplicationConfig) =
            JwtConfig(
                name = jwtAzureAdName,
                issuer = azureAdConfig.property("issuer").getString(),
                jwksUrl = azureAdConfig.property("jwksUrl").getString(),
                audience = listOf(azureAdConfig.property("clientId").getString()),
                preAuthorizedApps = getPreAuthorizedApps(),
            )

        private fun getPreAuthorizedApps(): List<PreAuthorizedApp>? =
            System.getenv("AZURE_APP_PRE_AUTHORIZED_APPS")?.let {
                try {
                    internalObjectMapper().readValue(it)
                } catch (e: JacksonException) {
                    logger.warn("Failed to deserialize preAuthorized apps, value was: $it", e)
                    emptyList()
                }
            }
    }
}

data class PreAuthorizedApp(val name: String, val clientId: String)

fun AuthenticationConfig.pdfByggerJwt(config: JwtConfig) =
    jwt(config.name) {
        realm = "pdf-bygger-$name"
        verifier(JwkProviderBuilder(URI(config.jwksUrl).toURL()).build(), config.issuer) {
            withAnyOfAudience(*config.audience.toTypedArray())
            withIssuer(config.issuer)
            withClaimPresence("sub")
            withClaimPresence("exp")
            withClaimPresence("nbf")
            withClaimPresence("iat")
        }
        validate {
            val azp = it["azp"]
            val isPreAuthorized = config.preAuthorizedApps?.any { app -> app.clientId == azp } == true

            if (isPreAuthorized) {
                JWTPrincipal(it.payload)
            } else {
                logger.info("Invalid authorization - claim 'azp' is not a preAuthorizedApp: $azp")
                null
            }
        }
    }
