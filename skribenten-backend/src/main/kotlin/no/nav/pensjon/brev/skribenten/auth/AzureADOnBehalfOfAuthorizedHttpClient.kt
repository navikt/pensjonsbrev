package no.nav.pensjon.brev.skribenten.auth

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.pensjon.brev.skribenten.callIdHeaders

sealed class AuthorizedHttpClientResult {
    class Response(val response: HttpResponse) : AuthorizedHttpClientResult()
    class Error(val error: TokenResponse.ErrorResponse) : AuthorizedHttpClientResult()
}

class AzureADOnBehalfOfAuthorizedHttpClient(
    private val scope: String,
    private val authService: AzureADService,
    clientEngine: HttpClientEngine = CIO.create(),
    clientConfigBlock: HttpClientConfig<*>.() -> Unit
) {
    private val client = HttpClient(clientEngine, clientConfigBlock)

    private suspend fun request(url: String, method: HttpMethod, block: HttpRequestBuilder.() -> Unit): AuthorizedHttpClientResult {
        val principal = PrincipalInContext.require()

        return authService.getOnBehalfOfToken(principal, scope).let { token ->
            when (token) {
                is TokenResponse.ErrorResponse -> AuthorizedHttpClientResult.Error(token)
                is TokenResponse.OnBehalfOfToken -> AuthorizedHttpClientResult.Response(
                    client.request(url) {
                        callIdHeaders()
                        headers { bearerAuth(token.accessToken) }
                        block()
                        this.method = method
                    }
                )
            }
        }
    }

    suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(url, HttpMethod.Get, block)

    suspend fun post(url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(url, HttpMethod.Post, block)

    suspend fun put(url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(url, HttpMethod.Put, block)

    suspend fun delete(url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(url, HttpMethod.Delete, block)

    suspend fun options(url: String, block: HttpRequestBuilder.() -> Unit = {}): AuthorizedHttpClientResult =
        request(url, HttpMethod.Options, block)
}