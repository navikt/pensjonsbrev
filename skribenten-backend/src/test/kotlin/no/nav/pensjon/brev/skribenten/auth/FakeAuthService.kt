package no.nav.pensjon.brev.skribenten.auth

object FakeAuthService : AuthService {
    override suspend fun getOnBehalfOfToken(principal: UserPrincipal, scope: String) = TokenResponse.OnBehalfOfToken(
        accessToken = "",
        refreshToken = "",
        tokenType = "",
        scope = "",
        expiresIn = 1000
    )
}