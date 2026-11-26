package no.nav.pensjon.brev.brevoppskrift.services

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.util.AttributeKey
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.CancellationException
import java.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class ExternalService(val timeout: kotlin.time.Duration) { VERTEX(20.seconds), SLACK(3.seconds) }
enum class ClientOperation { REQUEST, TOKEN, GENERATE, NOTIFY }

private val operationKey = AttributeKey<ClientOperation>("BrevoppskriftClientOperation")
fun HttpRequestBuilder.metricsOperation(operation: ClientOperation) { attributes.put(operationKey, operation) }

object HttpClientFactory {
    fun lagHttpClient(engine: HttpClientEngine, metrics: MeterRegistry, service: ExternalService): HttpClient {
        val instrumentation = createClientPlugin("BrevoppskriftClientMetrics") {
            on(Send) { request ->
                val sample = Timer.start(metrics)
                var status = "n/a"
                var outcome = "exception"
                try {
                    proceed(request).also { call ->
                        status = call.response.status.value.toString()
                        outcome = when (call.response.status.value) {
                            in 200..299 -> "success"
                            in 300..399 -> "redirect"
                            in 400..499 -> "client_error"
                            else -> "server_error"
                        }
                    }
                } catch (failure: Exception) {
                    outcome = when (failure.unwrapCancellationException()) {
                        is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException -> "timeout"
                        is CancellationException -> "cancelled"
                        is java.io.IOException -> "connection_error"
                        else -> "exception"
                    }
                    throw failure
                } finally {
                    sample.stop(
                        Timer.builder("brevoppskrift.http.client")
                            .tag("service", service.name)
                            .tag("operation", (request.attributes.getOrNull(operationKey) ?: ClientOperation.REQUEST).name)
                            .tag("method", request.method.value)
                            .tag("status", status)
                            .tag("outcome", outcome)
                            .publishPercentileHistogram()
                            .minimumExpectedValue(Duration.ofMillis(50))
                            .maximumExpectedValue(Duration.ofSeconds(30))
                            .register(metrics),
                    )
                }
            }
        }
        return HttpClient(engine) {
            followRedirects = false
            install(HttpTimeout) {
                requestTimeoutMillis = service.timeout.inWholeMilliseconds
                connectTimeoutMillis = 5.seconds.inWholeMilliseconds
                socketTimeoutMillis = service.timeout.inWholeMilliseconds
            }
            install(instrumentation)
        }
    }
}
