package no.nav.pensjon.brev.skribenten.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class AzureADServiceTest {
    private val jwtConfig = JwtConfig("navn", "utsteder", "jwks url", "skribenten-client-id", "http://localhost:9991/token", "skribenten-secret", emptyList(), true)
    private val objectMapper = jacksonObjectMapper()
    private val principal = UserPrincipal(UserAccessToken("access_token 123532"), mockk())

    @Test
    fun `getOnBehalfOfToken can exchange token`() {
        val onBehalfOfToken = TokenResponse.OnBehalfOfToken("obo token", "refresh obo", "Bearer", "bla", 1024L)

        val service = createService {
            // verify that clientId, clientSecret and principal access token is used
            val body = assertInstanceOf(FormDataContent::class.java, it.body)
            assertEquals(principal.accessToken.token, body.formData["assertion"])
            assertEquals(jwtConfig.clientId, body.formData["client_id"])
            assertEquals(jwtConfig.clientSecret, body.formData["client_secret"])

            respond(
                content = ByteReadChannel(objectMapper.writeValueAsBytes(onBehalfOfToken)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        runBlocking {
            assertEquals(onBehalfOfToken, service.getOnBehalfOfToken(principal, onBehalfOfToken.scope))
        }
    }

    @Test
    fun `getOnBehalfOfToken returns error`() {
        val errorResponse = TokenResponse.ErrorResponse("an error", "a description", emptyList(), "123", "abc", "call", "abc")

        val service = createService {
            respond(
                content = ByteReadChannel(objectMapper.writeValueAsBytes(errorResponse)),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        runBlocking {
            assertEquals(errorResponse, service.getOnBehalfOfToken(principal, "abc"))
        }
    }

    @Test
    fun `getOnBehalfOfToken caches the aqcuired token`() {
        val onBehalfOfToken = TokenResponse.OnBehalfOfToken("obo token", "refresh obo", "Bearer", "bla2", 1024L)
        val userPrincipal = UserPrincipal(UserAccessToken("access_token 123532"), mockk())

        val service = createService {
            respond(
                content = ByteReadChannel(objectMapper.writeValueAsBytes(onBehalfOfToken)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        runBlocking {
            service.getOnBehalfOfToken(userPrincipal, "bla2")
        }
        assertEquals(onBehalfOfToken, userPrincipal.getOnBehalfOfToken(onBehalfOfToken.scope))
    }

    @Test
    fun `getOnBehalfOfToken uses cached token`() {
        val onBehalfOfToken = TokenResponse.OnBehalfOfToken("obo token", "refresh obo", "Bearer", "bla3", 1024L)
        val userPrincipal = UserPrincipal(UserAccessToken("access_token 123532"), mockk()).apply { setOnBehalfOfToken("bla3", onBehalfOfToken) }

        val service = createService()
        runBlocking {
            service.getOnBehalfOfToken(userPrincipal, "bla3")
        }
        assertEquals(onBehalfOfToken, userPrincipal.getOnBehalfOfToken(onBehalfOfToken.scope))
    }


    private fun createService(handler: (suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) = { respond("") }) =
        AzureADService(
            jwtConfig = jwtConfig,
            engine = MockEngine.invoke(handler),
        )
}
