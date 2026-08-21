package no.nav.pensjon.brev.pdfbygger

import com.auth0.jwk.JwkProviderBuilder
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
    val audience: String,
    val preAuthorizedApps: List<PreAuthorizedApp>,
) {
    companion object {
        fun requireAzureADConfig(azureAdConfig: ApplicationConfig) =
            JwtConfig(
                name = "AZURE_AD",
                issuer = azureAdConfig.property("issuer").getString(),
                jwksUrl = azureAdConfig.property("jwksUrl").getString(),
                audience = azureAdConfig.property("clientId").getString(),
                preAuthorizedApps = internalObjectMapper().readValue(System.getenv("AZURE_APP_PRE_AUTHORIZED_APPS")),
            )
    }
}

data class PreAuthorizedApp(val name: String, val clientId: String)

fun AuthenticationConfig.pdfByggerJwt(config: JwtConfig) =
    jwt(config.name) {
        realm = "pdf-bygger-$name"
        verifier(JwkProviderBuilder(URI(config.jwksUrl).toURL()).build(), config.issuer) {
            withAudience(config.audience)
            withIssuer(config.issuer)
            withClaimPresence("sub")
            withClaimPresence("exp")
            withClaimPresence("nbf")
            withClaimPresence("iat")
        }
        validate {
            val azp = it["azp"]

            if (config.preAuthorizedApps.any { app -> app.clientId == azp }) {
                JWTPrincipal(it.payload)
            } else {
                logger.info("Invalid authorization - claim 'azp' is not a preAuthorizedApp: $azp")
                null
            }
        }
    }
