package no.nav.pensjon.brev.skribenten.auth

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.interfaces.Payload
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import no.nav.pensjon.brev.skribenten.AzureADConfig
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.slf4j.LoggerFactory
import java.net.URI

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.skribenten.auth.Authentication")

const val AUTHENTICATION_REALM_NAME = "AZURE_AD"

// HOCON env-var substitution only exposes preAuthApps as an opaque JSON string, not a real
// HOCON list, so it must be parsed explicitly rather than relying on Hocon deserialization.
private fun parsePreAuthorizedApps(preAuthApps: String): List<AzureADConfig.PreAuthorizedApp> =
    try {
        Json.decodeFromString(preAuthApps)
    } catch (e: SerializationException) {
        logger.error("Failed to deserialize preAuthorizedApps, value was: $preAuthApps", e)
        emptyList()
    }

fun AuthenticationConfig.skribentenJwt(config: AzureADConfig) =
    jwt(AUTHENTICATION_REALM_NAME) {
        val preAuthorizedApps = parsePreAuthorizedApps(config.preAuthApps)

        realm = "skribenten-$name"
        verifier(JwkProviderBuilder(URI(config.jwksUrl).toURL()).build(), config.issuer) {
            withAnyOfAudience(config.clientId)
            withIssuer(config.issuer)

            withClaimPresence("sub")
            withClaimPresence("exp")
            withClaimPresence("nbf")
            withClaimPresence("iat")
        }
        validate {
            val azp = it["azp"]

            if (preAuthorizedApps.any { app -> app.clientId == azp }) {
                JwtUserPrincipal(userAccessToken(), it.payload)
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

class InvalidAuthorization(msg: String, cause: Throwable? = null) : Exception(msg, cause)

class MissingClaimException(msg: String) : UnauthorizedException(msg)

@JvmInline
value class UserAccessToken(val token: String) {
    override fun toString() = token
}

interface UserPrincipal {
    val accessToken: UserAccessToken
    val navIdent: NavIdent
    val fullName: String

    fun isInGroup(groupId: ADGroup): Boolean
    fun isAttestant(): Boolean = isInGroup(ADGroups.attestant)
}

data class JwtUserPrincipal(override val accessToken: UserAccessToken, private val jwtPayload: Payload) : UserPrincipal {
    override val navIdent: NavIdent by lazy { NavIdent(getClaimAsString("NAVident")) }
    override val fullName: String by lazy { getClaimAsString("name") }

    override fun isInGroup(groupId: ADGroup) = groups.contains(groupId)

    private fun getClaimAsString(claim: String): String =
        jwtPayload.getClaim(claim).asString()
            ?: throw MissingClaimException("Missing claim: $claim")


    private val groups: List<ADGroup> by lazy {
        jwtPayload.getClaim("groups")?.asList(String::class.java)?.map { ADGroup(it) }
            ?: emptyList()
    }
}