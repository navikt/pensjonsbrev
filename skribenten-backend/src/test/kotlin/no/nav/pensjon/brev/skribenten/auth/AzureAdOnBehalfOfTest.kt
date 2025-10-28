package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.context.CoroutineContextValueException
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class AzureAdOnBehalfOfTest {

    private val principal = MockPrincipal(NavIdent("Cypher"), "Mr. Reagan")
    private val clientScope = "Matrix"

    private fun settOppClient(authService: AuthService): HttpClient =
        HttpClient(MockEngine { respond("Principal: ${it.headers[HttpHeaders.Authorization]?.substringAfter("Bearer ")}") }) {
            install(AzureAdOnBehalfOf) {
                this.authService = authService
                scope = clientScope
            }
        }

    @Test
    fun `feiler om principal ikke er i context`(): Unit = runBlocking {
        assertThrows<CoroutineContextValueException> {
            settOppClient(FakeAuthService).get("/something")
        }
    }

    @Test
    fun `utveksler obo-token med principal fra context`(): Unit = runBlocking {
        val aToken = TokenResponse.OnBehalfOfToken("Joe Pantoliano", "", "", clientScope, 10_000)
        val tokenAuthService = object : AuthService {
            override suspend fun getOnBehalfOfToken(principal: UserPrincipal, scope: String) = aToken
        }

        val response = withPrincipal(principal) {
            settOppClient(tokenAuthService).get("/something")
        }
        assertEquals("Principal: ${aToken.accessToken}", response.bodyAsText())
    }

}
