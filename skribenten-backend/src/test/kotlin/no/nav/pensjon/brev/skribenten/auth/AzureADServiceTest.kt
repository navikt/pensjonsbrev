package no.nav.pensjon.brev.skribenten.auth

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.Payload
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.AzureADConfig
import no.nav.pensjon.brev.skribenten.common.InMemoryCache
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private val fakeJwtPayload = object : Payload {
    override fun getIssuer() = null
    override fun getSubject() = null
    override fun getAudience() = emptyList<String>()
    override fun getExpiresAt() = null
    override fun getNotBefore() = null
    override fun getIssuedAt() = null
    override fun getId() = null
    override fun getClaim(name: String?) = object : Claim {
        override fun isNull() = false
        override fun isMissing() = false
        override fun asBoolean() = false
        override fun asInt() = 0
        override fun asLong() = 0L
        override fun asDouble() = 0.0
        override fun asString() = name
        override fun asDate() = null
        override fun <T> asArray(clazz: Class<T?>?) = null
        override fun <T> asList(clazz: Class<T?>?) = listOf<T>()
        override fun asMap() = mapOf<String, Any>()
        override fun <T> `as`(clazz: Class<T?>?) = null
    }
    override fun getClaims() = mapOf<String?, Claim?>()
}

class AzureADServiceTest {
    private val jwtConfig = AzureADConfig("utsteder", "jwks url", "skribenten-client-id", "http://localhost:9991/token", "skribenten-secret", emptyList())
    private val objectMapper = jacksonObjectMapper()
    private val principal = JwtUserPrincipal(UserAccessToken("access_token 123532"), fakeJwtPayload)

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
    fun `getOnBehalfOfToken throws error`() {
        val errorResponse = TokenResponse.ErrorResponse("an error", "a description", emptyList(), "123", "abc", "call", "abc")

        val service = createService {
            respond(
                content = ByteReadChannel(objectMapper.writeValueAsBytes(errorResponse)),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        runBlocking {
            assertThrows<AzureAdOnBehalfOfAuthorizationException> {
                service.getOnBehalfOfToken(principal, "abc")
            }
        }
    }

    @Test
    fun `getOnBehalfOfToken throws error with 401-status when error is invalid_grant`() {
        val errorResponse = TokenResponse.ErrorResponse("invalid_grant", "token expired", emptyList(), "123", "abc", "call", "abc")

        val service = createService {
            respond(
                content = ByteReadChannel(objectMapper.writeValueAsBytes(errorResponse)),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        runBlocking {
            val exception = assertThrows<AzureAdOnBehalfOfAuthorizationException> {
                service.getOnBehalfOfToken(principal, "abc")
            }
            assertEquals(HttpStatusCode.Unauthorized, exception.status)
        }
    }

    @Test
    fun `getOnBehalfOfToken throws error with 500-status when error is not invalid_grant`() {
        val errorResponse = TokenResponse.ErrorResponse("invalid_client", "bad client secret", emptyList(), "123", "abc", "call", "abc")

        val service = createService {
            respond(
                content = ByteReadChannel(objectMapper.writeValueAsBytes(errorResponse)),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        runBlocking {
            val exception = assertThrows<AzureAdOnBehalfOfAuthorizationException> {
                service.getOnBehalfOfToken(principal, "abc")
            }
            assertEquals(HttpStatusCode.InternalServerError, exception.status)
        }
    }

    private fun createService(handler: (suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) = { respond("") }) =
        AzureADService(
            jwtConfig = jwtConfig,
            engine = MockEngine.invoke(handler),
            cache = InMemoryCache()
        )
}
