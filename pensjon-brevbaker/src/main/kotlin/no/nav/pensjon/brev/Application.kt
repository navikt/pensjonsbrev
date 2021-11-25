package no.nav.pensjon.brev

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.metrics.micrometer.*
import no.nav.pensjon.brev.JwtConfig.Companion.jwtAzureAdName
import no.nav.pensjon.brev.JwtConfig.Companion.jwtStsName
import no.nav.pensjon.brev.template.brevbakerConfig
import java.net.URL

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun requireEnv(key: String) =
    System.getenv(key)?: throw IllegalStateException("The environment variable $key is missing.")

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    install(ContentNegotiation) {
        jackson {
            brevbakerConfig()
        }
    }

    install(Authentication) {
        brevbakerJwt(JwtConfig.requireFromEnv(jwtStsName))
        brevbakerJwt(JwtConfig.requireFromEnv(jwtAzureAdName))
    }

    // TODO: handle Nav-Call-Id

    install(MicrometerMetrics) {
        registry = Metrics.prometheusRegistry
    }

    brevbakerRouting(arrayOf(jwtStsName, jwtAzureAdName), requireEnv("DEPLOYMENT_ENVIRONMENT"))
}

data class JwtConfig(val name: String, val issuer: String, val jwksUrl: String, val audience: String) {
    companion object {
        const val jwtStsName = "STS"
        const val jwtAzureAdName = "AZURE_AD"
        const val jwtEnvPrefix = "JWT"

        fun requireFromEnv(name: String) =
            JwtConfig(
                name = name,
                issuer = requireEnv("${jwtEnvPrefix}_ISSUER_$name"),
                jwksUrl = requireEnv("${jwtEnvPrefix}_JWKS_URL_$name"),
                audience = requireEnv("${jwtEnvPrefix}_AUDIENCE_$name"),
            )
    }
}

fun Authentication.Configuration.brevbakerJwt(config: JwtConfig) =
    jwt(config.name) {
        realm = "brevbaker-latex-$name"
        verifier(JwkProviderBuilder(URL(config.jwksUrl)).build(), config.issuer) {
            withAudience(config.audience)
            withIssuer(config.issuer)
        }
        validate {
            JWTPrincipal(it.payload)
        }
    }