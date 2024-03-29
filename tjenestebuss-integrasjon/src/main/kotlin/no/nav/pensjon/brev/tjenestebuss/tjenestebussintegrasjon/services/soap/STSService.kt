package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap

import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.ServiceStatus
import java.time.LocalDateTime

data class UserToken(
    val access_token: String,
    val expires_in: Long,
    val issued_token_type: String,
    val token_type: String,
) {
    private val issuedTime = LocalDateTime.now()

    fun isExpired(expirationLeeway: Long): Boolean {
        return LocalDateTime.now().isAfter(issuedTime.plusSeconds(expires_in).minusSeconds(expirationLeeway))
    }

    fun isValid(expirationLeeway: Long) = !isExpired(expirationLeeway)
}

class STSService(stsConfig: Config) : ServiceStatus {
    private val mutex = Mutex()

    private var token: UserToken? = null
    private val stsEndpointUrl: String = stsConfig.getString("url")
    private val stsUser: String = stsConfig.getString("username")
    private val stsPassword: String = stsConfig.getString("password")
    private val client = HttpClient(CIO) {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(stsUser, stsPassword)
                }
            }
        }
        defaultRequest {
            url(stsEndpointUrl)
        }
        install(ContentNegotiation) {
            jackson()
        }
    }

    suspend fun getToken(): UserToken {
        mutex.withLock {
            val currentToken = token
            return if (currentToken != null && currentToken.isValid(10)) {
                currentToken
            } else {
                val newToken = fetchToken()
                token = newToken
                newToken
            }
        }
    }

    private suspend fun fetchToken(): UserToken {
        val response = client.get("/rest/v1/sts/samltoken") {
            headers {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
            parameter("grant_type", "client_credentials")
            parameter("scope", "openid")
        }
        return response.body()
    }

    override val name = "REST STS"
    override suspend fun ping(): Boolean = getToken().isValid(10)
}