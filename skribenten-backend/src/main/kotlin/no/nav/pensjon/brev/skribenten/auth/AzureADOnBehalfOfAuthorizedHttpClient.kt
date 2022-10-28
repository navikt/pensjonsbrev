package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*

sealed class AuthorizedHttpClientResult {
    class Response(val response: HttpResponse) : AuthorizedHttpClientResult()
    class Error(val error: TokenResponse.ErrorResponse) : AuthorizedHttpClientResult()
}

class AzureADOnBehalfOfAuthorizedHttpClient(private val scope: String, private val authService: AzureADService, clientConfigBlock: HttpClientConfig<CIOEngineConfig>.() -> Unit) {
    private val client = HttpClient(CIO, clientConfigBlock)

    private suspend fun request(call: ApplicationCall, url: String, method: HttpMethod, block: HttpRequestBuilder.() -> Unit): AuthorizedHttpClientResult {
        return authService.getOnBehalfOfToken(call, scope).let { token ->
            when (token) {
                is TokenResponse.ErrorResponse -> AuthorizedHttpClientResult.Error(token)
                is TokenResponse.OnBehalfOfToken -> AuthorizedHttpClientResult.Response(
                    client.request(url) {
                        headers {
                            bearerAuth(token.accessToken)
                            call.callId?.also { append("Nav-Call-Id", it) }
                        }
                        block()
                        this.method = method
                    }
                )
            }
        }
    }

    suspend fun get(call: ApplicationCall, url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(call, url, HttpMethod.Get, block)

    suspend fun post(call: ApplicationCall, url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(call, url, HttpMethod.Post, block)

    suspend fun put(call: ApplicationCall, url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(call, url, HttpMethod.Put, block)

    suspend fun delete(call: ApplicationCall, url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(call, url, HttpMethod.Delete, block)
}