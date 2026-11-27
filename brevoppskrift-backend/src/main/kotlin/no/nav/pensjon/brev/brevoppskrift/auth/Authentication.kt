package no.nav.pensjon.brev.brevoppskrift.auth

import com.auth0.jwk.JwkProvider
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.jwt.jwt
import no.nav.pensjon.brev.brevoppskrift.AzureConfig
import no.nav.pensjon.brev.brevoppskrift.api.respondProblem
import io.ktor.http.HttpStatusCode

const val ENTRA_AUTH = "entra"

data class DraftUser(
    val navIdent: NavIdent,
    val name: AuthorName,
    val canEdit: Boolean,
)

fun AuthenticationConfig.draftAuthentication(config: AzureConfig, jwks: JwkProvider) {
    jwt(ENTRA_AUTH) {
        verifier(jwks, config.issuer) {
            withAudience(config.audience.value)
            withClaimPresence("sub")
            withClaimPresence("exp")
            withClaimPresence("nbf")
            withClaimPresence("iat")
        }
        validate { credential ->
            val payload = credential.payload
            val navIdent = payload.getClaim("NAVident").asString()?.takeIf { it.isNotBlank() }
            val name = payload.getClaim("name").asString()?.takeIf { it.isNotBlank() }
            val azp = payload.getClaim("azp").asString()
            if (config.preAuthApps.none { it.clientId.value.equals(azp, ignoreCase = true) } || navIdent == null || name == null) {
                null
            } else {
                DraftUser(
                    navIdent = NavIdent(navIdent),
                    name = AuthorName(name),
                    canEdit = payload.getClaim("groups").asList(String::class.java)?.any { it.equals(config.editorGroupId.value, ignoreCase = true) } == true,
                )
            }
        }
        challenge { _, _ ->
            call.respondProblem(HttpStatusCode.Unauthorized, "UNAUTHORIZED", "Innlogging kreves.")
        }
    }
}
