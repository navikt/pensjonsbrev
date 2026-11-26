package no.nav.pensjon.brev.brevoppskrift.services

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.api.SendingRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.util.AttributeKey
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.CancellationException
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import java.time.Duration

enum class ClientOperation { REQUEST, TOKEN, GENERATE, NOTIFY }

private val operationKey = AttributeKey<ClientOperation>("BrevoppskriftClientOperation")
private val measureKey = AttributeKey<ClientCallMeasure>("BrevoppskriftHttpClientMeasure")
private val logger = LoggerFactory.getLogger("brevoppskrift.http.client.metrics")

fun HttpRequestBuilder.metricsOperation(operation: ClientOperation) { attributes.put(operationKey, operation) }

private class ClientCallMeasure(val sample: Timer.Sample) {
    var recorded: Boolean = false
}

class HttpClientMetricsConfig {
    lateinit var registry: MeterRegistry
    lateinit var service: ExternalService
}

val HttpClientMetrics = createClientPlugin("HttpClientMetrics", ::HttpClientMetricsConfig) {
    val registry = pluginConfig.registry
    val service = pluginConfig.service

    fun record(measure: ClientCallMeasure?, operation: ClientOperation?, method: String, status: String, outcome: String) {
        if (measure == null) {
            logger.warn("Missing HTTP client measurement for service {}", service)
            return
        }
        if (measure.recorded) return
        // Flere hooks kan se samme feil; bare det første registrerer forsøket.
        measure.recorded = true
        measure.sample.stop(
            Timer.builder("brevoppskrift.http.client")
                .tag("service", service.name)
                .tag("operation", (operation ?: ClientOperation.REQUEST).name)
                .tag("method", method)
                .tag("status", status)
                .tag("outcome", outcome)
                .publishPercentileHistogram()
                .minimumExpectedValue(Duration.ofMillis(50))
                .maximumExpectedValue(Duration.ofSeconds(30))
                .register(registry),
        )
    }

    fun classifyException(failure: Throwable): String = when (failure.unwrapCancellationException()) {
        is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException -> "timeout"
        is CancellationException -> "cancelled"
        is UnresolvedAddressException, is IOException -> "connection_error"
        else -> "exception"
    }

    client.requestPipeline.intercept(HttpRequestPipeline.Before) {
        val measure = ClientCallMeasure(Timer.start(registry))
        context.attributes.put(measureKey, measure)
        try {
            proceed()
        } catch (failure: Throwable) {
            record(context.attributes.getOrNull(measureKey) ?: measure, context.attributes.getOrNull(operationKey), context.method.value, "n/a", classifyException(failure))
            throw failure
        }
    }

    on(SendingRequest) { request, _ ->
        request.attributes.put(measureKey, ClientCallMeasure(Timer.start(registry)))
    }

    onResponse { response ->
        val request = response.call.request
        val outcome = when (response.status.value) {
            in 200..299 -> "success"
            in 300..399 -> "redirect"
            in 400..499 -> "client_error"
            in 500..599 -> "server_error"
            else -> "unknown"
        }
        record(request.attributes.getOrNull(measureKey), request.attributes.getOrNull(operationKey), request.method.value, response.status.value.toString(), outcome)
    }

    client.sendPipeline.intercept(HttpSendPipeline.Before) {
        try {
            proceed()
        } catch (failure: Throwable) {
            record(context.attributes.getOrNull(measureKey), context.attributes.getOrNull(operationKey), context.method.value, "n/a", classifyException(failure))
            throw failure
        }
    }
}
