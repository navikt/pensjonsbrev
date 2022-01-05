package no.nav.pensjon.brev

import com.auth0.jwk.JwkProviderBuilder
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.metrics.micrometer.*
import no.nav.pensjon.brev.JwtConfig.Companion.jwtAzureAdName
import no.nav.pensjon.brev.JwtConfig.Companion.jwtStsName
import no.nav.pensjon.brev.template.brevbakerConfig
import no.nav.pensjon.brev.template.jacksonObjectMapper
import org.slf4j.LoggerFactory
import java.net.URL

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun requireEnv(key: String) =
    System.getenv(key) ?: throw IllegalStateException("The environment variable $key is missing.")

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    install(ContentNegotiation) {
        jackson {
            brevbakerConfig()
        }
    }

    install(Authentication) {
//        brevbakerJwt(JwtConfig.requireFromEnv(jwtStsName))
        brevbakerJwt(JwtConfig.requireFromEnv(jwtAzureAdName))
    }

    // TODO: handle Nav-Call-Id

    install(MicrometerMetrics) {
        registry = Metrics.prometheusRegistry
    }

    brevbakerRouting(arrayOf(jwtAzureAdName))
}

data class JwtConfig(val name: String, val issuer: String, val jwksUrl: String, val clientId: String, val preAuthorizedApps: List<PreAuthorizedApp>?) {
    companion object {
        private val logger = LoggerFactory.getLogger(JwtConfig::class.java)

        const val jwtStsName = "STS"
        const val jwtAzureAdName = "AZURE_AD"
        private const val jwtEnvPrefix = "JWT"

        fun requireFromEnv(name: String) =
            JwtConfig(
                name = name,
                issuer = requireEnv("AZURE_OPENID_CONFIG_ISSUER"),
                jwksUrl = requireEnv("AZURE_OPENID_CONFIG_JWKS_URI"),
                clientId = requireEnv("AZURE_APP_CLIENT_ID"),
                preAuthorizedApps = getPreAuthorizedApps()
            ).also { logger.info("JwtConfig: $it") }

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
            withAudience(config.clientId)
            withIssuer(config.issuer)
            withClaimPresence("sub")
            withClaimPresence("exp")
            withClaimPresence("nbf")
            withClaimPresence("iat")
        }
        validate {
            JWTPrincipal(it.payload)
        }
    }