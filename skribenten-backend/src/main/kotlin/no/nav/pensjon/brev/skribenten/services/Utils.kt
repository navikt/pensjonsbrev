package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.*
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.auth.AzureAdOnBehalfOf
import no.nav.pensjon.brev.skribenten.context.CallIdFromContext
import org.slf4j.Logger
import kotlin.math.pow
import kotlin.random.Random

fun HttpClientConfig<*>.callIdAndOnBehalfOfClient(scope: String, authService: AuthService) {
    install(CallIdFromContext)
    install(AzureAdOnBehalfOf) {
        this.scope = scope
        this.authService = authService
    }
}

fun HttpClientConfig<*>.installRetry(logger: Logger, maxRetries: Int = 10, unntak: ((req: HttpRequest) -> Boolean) = { false } ) {
    install(HttpRequestRetry) {
        delayMillis {
            minOf(2.0.pow(it).toLong(), 1000L) + Random.nextLong(100)
        }
        retryIf(maxRetries) { req, res ->
            when {
                res.status.isSuccess() -> false
                unntak(req) -> false
                res.status in setOf(HttpStatusCode.GatewayTimeout, HttpStatusCode.RequestTimeout, HttpStatusCode.BadGateway) -> true
                else -> false
            }
        }
        retryOnExceptionIf { _, cause ->
            val actualCause = cause.unwrapCancellationException()
            val doRetry = actualCause is HttpRequestTimeoutException
                    || actualCause is ConnectTimeoutException
                    || actualCause is IOException
            if (!doRetry) {
                logger.error("Won't retry for exception: ${actualCause.message}", actualCause)
            }
            doRetry
        }
    }
}