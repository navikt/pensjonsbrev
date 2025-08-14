package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.mockk.coEvery
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
    private val authService = object : AuthService {
        override suspend fun getOnBehalfOfToken(principal: UserPrincipal, scope: String): TokenResponse {
            return TokenResponse.OnBehalfOfToken(
                accessToken = "",
                refreshToken = "",
                tokenType = "",
                scope = "",
                expiresIn = 1000
            )
        }

    }

    private val clientWithOBOPlugin =
        HttpClient(MockEngine { respond("Principal: ${it.headers[HttpHeaders.Authorization]?.substringAfter("Bearer ")}") }) {
            install(AzureAdOnBehalfOf) {
                this.authService = this@AzureAdOnBehalfOfTest.authService
                scope = clientScope
            }
        }

    @Test
    fun `feiler om principal ikke er i context`(): Unit = runBlocking {
        assertThrows<CoroutineContextValueException> {
            clientWithOBOPlugin.get("/something")
        }
    }

    @Test
    fun `utveksler obo-token med principal fra context`(): Unit = runBlocking {
        val aToken = TokenResponse.OnBehalfOfToken("Joe Pantoliano", "", "", clientScope, 10_000)
        coEvery { authService.getOnBehalfOfToken(eq(principal), eq(clientScope)) } returns aToken

        val response = withPrincipal(principal) {
            clientWithOBOPlugin.get("/something")
        }
        assertEquals("Principal: ${aToken.accessToken}", response.bodyAsText())
    }

    @Test
    fun `feiler om authService svarer med feil`(): Unit = runBlocking {
        val tokenError = TokenResponse.ErrorResponse("", "", emptyList(), "", "", "", null)
        coEvery { authService.getOnBehalfOfToken(eq(principal), eq(clientScope)) } returns tokenError

        assertThrows<AzureAdOnBehalfOfAuthorizationException> {
            withPrincipal(principal) {
                clientWithOBOPlugin.get("/something")
            }
        }
    }

}
