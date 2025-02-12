package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth

import com.auth0.jwk.JwkProviderBuilder
import com.typesafe.config.Config
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.net.ProxySelector
import java.net.URI
import java.net.URL

private const val JWT_AZURE_AD_NAME = "AZURE_AD"
private val logger =
    LoggerFactory.getLogger("no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.auth.Authentication")

data class JwtConfig(
    val name: String,
    val issuer: String,
    val jwksUrl: String,
    val clientId: String,
    val tokenUri: String,
    val clientSecret: String,
    val requireAzureAdClaims: Boolean,
)

fun Config.requireAzureADConfig() =
    getConfig("azureAD").let {
        JwtConfig(
            name = JWT_AZURE_AD_NAME,
            issuer = it.getString("issuer"),
            jwksUrl = it.getString("jwksUrl"),
            clientId = it.getString("clientId"),
            tokenUri = it.getString("tokenEndpoint"),
            clientSecret = it.getString("clientSecret"),
            requireAzureAdClaims = true,
        )
    }.also { logger.debug("AzureAD: $it") }

fun AuthenticationConfig.tjenestebusJwt(config: JwtConfig) =
    jwt(config.name) {
        realm = "tjenestebuss-integrasjon$name"
        val proxyUri: URI? = System.getenv("HTTP_PROXY")?.let { URI.create(it) }
        val jwkBuilder =
            JwkProviderBuilder(URL(config.jwksUrl))
                .apply {
                    if (proxyUri != null) {
                        proxied(ProxySelector.of(InetSocketAddress(proxyUri.host, proxyUri.port)).select(URI(config.jwksUrl)).first())
                    }
                }

        verifier(jwkBuilder.build(), config.issuer) {
            withAnyOfAudience(config.clientId)
            withIssuer(config.issuer)
        }
        validate {
            JWTPrincipal(it.payload)
        }
    }
