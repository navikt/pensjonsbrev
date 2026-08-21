package no.nav.pensjon.brev

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.time.Instant

class AzureAdM2mTokenClient(
    private val tokenEndpoint: String,
    private val clientId: String,
    private val clientSecret: String,
    private val scope: String,
) : Closeable {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val mutex = Mutex()
    private var cachedToken: CachedToken? = null

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson {}
        }
    }

    suspend fun getToken(): String = mutex.withLock {
        cachedToken?.takeIf { Instant.now().isBefore(it.expiresAt) }?.accessToken
            ?: fetchToken().also { cachedToken = it }.accessToken
    }

    private suspend fun fetchToken(): CachedToken {
        val response = client.submitForm(
            url = tokenEndpoint,
            formParameters = Parameters.build {
                append("grant_type", "client_credentials")
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("scope", scope)
            }
        ) {
            headers { append(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString()) }
        }

        if (!response.status.isSuccess()) {
            val errorBody = response.body<String>()
            logger.error("Failed to fetch Azure AD client credentials token (status=${response.status.value}): $errorBody")
            throw AzureAdClientCredentialsException(ClientCredentialsErrorResponse("http_${response.status.value}", errorBody))
        }

        val token = response.body<ClientCredentialsTokenResponse>()
        return CachedToken(token.accessToken, Instant.now().plusSeconds(token.expiresIn).minusSeconds(30))
    }

    override fun close() = client.close()

    private data class CachedToken(val accessToken: String, val expiresAt: Instant)
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class ClientCredentialsTokenResponse(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClientCredentialsErrorResponse(
    @JsonProperty("error") val error: String,
    @JsonProperty("error_description") val errorDescription: String?,
)

class AzureAdClientCredentialsException(error: ClientCredentialsErrorResponse) :
    Exception("Failed to fetch Azure AD client credentials token: ${error.error} - ${error.errorDescription}")
