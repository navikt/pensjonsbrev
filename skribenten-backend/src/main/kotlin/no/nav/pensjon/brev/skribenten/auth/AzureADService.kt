package no.nav.pensjon.brev.skribenten.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.Cacheomraade
import no.nav.pensjon.brev.skribenten.common.cached
import no.nav.pensjon.brev.skribenten.services.installRetry
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(JsonSubTypes.Type(TokenResponse.OnBehalfOfToken::class), JsonSubTypes.Type(TokenResponse.ErrorResponse::class))
sealed class TokenResponse {
    data class OnBehalfOfToken(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("refresh_token") val refreshToken: String,
        @JsonProperty("token_type") val tokenType: String,
        @JsonProperty("scope") val scope: String,
        @JsonProperty("expires_in") val expiresIn: Long,
    ) : TokenResponse()

    data class ErrorResponse(
        @JsonProperty("error") val error: String,
        @JsonProperty("error_description") val error_description: String,
        @JsonProperty("error_codes") val error_codes: List<String>,
        @JsonProperty("timestamp") val timestamp: String,
        @JsonProperty("trace_id") val trace_id: String,
        @JsonProperty("correlation_id") val correlation_id: String,
        @JsonProperty("claims") val claims: String?,
    ) : TokenResponse()
}

interface AuthService {
    suspend fun getOnBehalfOfToken(principal: UserPrincipal, scope: String): TokenResponse.OnBehalfOfToken
}

class AzureADService(private val jwtConfig: JwtConfig, engine: HttpClientEngine = CIO.create(), private val cache: Cache) : AuthService {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val client = HttpClient(engine) {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        installRetry(logger, maxRetries = 2)
    }

    override suspend fun getOnBehalfOfToken(principal: UserPrincipal, scope: String) =
        cache.cached(Cacheomraade.AD, Pair(principal.navIdent, scope), { it.expiresIn.seconds.minus(5.minutes) }) {
            val response = client.submitForm(
                url = jwtConfig.tokenUri,
                formParameters = Parameters.build {
                    append("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                    append("client_id", jwtConfig.clientId)
                    append("client_secret", jwtConfig.clientSecret)
                    append("assertion", principal.accessToken.token)
                    append("scope", scope)
                    append("requested_token_use", "on_behalf_of")
                }
            ) {
                headers { append(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded) }
            }
            if (!response.status.isSuccess()) {
                throw AzureAdOnBehalfOfAuthorizationException(response.body<TokenResponse.ErrorResponse>())
            }
            response.body<TokenResponse.OnBehalfOfToken>()
        }
}