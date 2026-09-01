package no.nav.pensjon.brev.skribenten.services

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
import kotlinx.io.IOException
import no.nav.pensjon.brev.skribenten.Metrics
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.channels.UnresolvedAddressException

private val RouteAttributeKey = AttributeKey<String>("SkribentenHttpClientRoute")
private val MeasureAttributeKey = AttributeKey<ClientCallMeasure>("SkribentenHttpClientMetricsMeasure")
private const val UKJENT_ROUTE = "ukjent"
private val logger: Logger = LoggerFactory.getLogger("no.nav.pensjon.brev.skribenten.services.HttpClientMetrics")

/**
 * Tilstand for ett HTTP-forsøk: en [Timer.Sample] pluss om forsøket er registrert. `recorded`
 * unngår at samme forsøk telles to ganger av flere hooks - se [HttpClientMetrics].
 */
private class ClientCallMeasure(val sample: Timer.Sample) {
    var recorded: Boolean = false
}

/**
 * Merker et utgående kall med hvilken logiske rute det gjelder, for bruk som `route`-label i
 * [HttpClientMetrics]. Bruk en URL-mal med path-parametre som `{navn}` (f.eks.
 * `sak/{saksId}/brevdata`) for å unngå uendelig kardinalitet, eller et kort, stabilt
 * operasjonsnavn for klienter mot ett enkelt endepunkt (GraphQL o.l.).
 */
fun HttpRequestBuilder.metricsRoute(template: String) {
    attributes.put(RouteAttributeKey, template)
}

/**
 * Konfigurasjon for [HttpClientMetrics]: hvilket [MeterRegistry] og metrikknavn som skal brukes.
 */
class HttpClientMetricsConfig {
    var registry: MeterRegistry = Metrics.registry
    var metricName: String = Metrics.clientMetricName
}

/**
 * Instrumenterer HTTP-kall med varighet og utfall, slik at vi kan alarmere per nedstrøms
 * tjeneste og rute i stedet for å måtte lese logger.
 *
 * - [SendingRequest] starter [Timer.Sample] per forsøk.
 * - `onResponse` stopper timeren og registrerer metrikken for forsøk som fikk svar (også 4xx/5xx).
 * - En intercept på `sendPipeline` fanger og registrerer forsøk som aldri fikk svar
 *   (timeout/tilkoblingsfeil).
 * - En intercept på `requestPipeline` fanger og registrerer unntak som oppstår *før* Send-fasen
 *   (f.eks. feil i serialisering), som de over ikke ser.
 * `ClientCallMeasure.recorded` hindrer at samme forsøk telles to ganger.
 *
 * Labels: `downstream_host` (fra `request.url.host`), `route` (fra [metricsRoute], default
 * [UKJENT_ROUTE]), `method`, `status` (`"n/a"` uten svar) og `outcome`
 * (`success`/`client_error`/`server_error`/`timeout`/`connection_error`/`exception`).
 */
val HttpClientMetrics = createClientPlugin("HttpClientMetrics", ::HttpClientMetricsConfig) {
    val registry = pluginConfig.registry
    val metricName = pluginConfig.metricName

    fun recordHttpClientMetric(
        measure: ClientCallMeasure?,
        downstreamHost: String,
        route: String,
        method: String,
        status: String,
        outcome: String,
    ) {
        if (measure == null) {
            logger.warn("Mangler Timer.Sample for nedstrøms HTTP-kall mot {}, hopper over metrikk", downstreamHost)
            return
        }
        if (measure.recorded) return

        measure.recorded = true
        measure.sample.stop(
            Timer.builder(metricName)
                .description("Varighet og utfall for nedstrøms HTTP-kall fra skribenten-backend")
                .tag("downstream_host", downstreamHost)
                .tag("route", route)
                .tag("method", method)
                .tag("status", status)
                .tag("outcome", outcome)
                .register(registry)
        )
    }

    fun classifyExceptionOutcome(e: Throwable): String = when (e.unwrapCancellationException()) {
        is HttpRequestTimeoutException,
        is ConnectTimeoutException,
        is SocketTimeoutException -> "timeout"
        is UnresolvedAddressException,
        is IOException -> "connection_error"
        else -> "exception"
    }

    client.requestPipeline.intercept(HttpRequestPipeline.Before) {
        val measure = ClientCallMeasure(Timer.start(registry))
        context.attributes.put(MeasureAttributeKey, measure)
        try {
            proceed()
        } catch (e: Throwable) {
            recordHttpClientMetric(
                context.attributes.getOrNull(MeasureAttributeKey) ?: measure,
                downstreamHost = context.url.host,
                route = context.attributes.getOrNull(RouteAttributeKey) ?: UKJENT_ROUTE,
                method = context.method.value,
                status = "n/a",
                outcome = classifyExceptionOutcome(e),
            )
            throw e
        }
    }

    on(SendingRequest) { request, _ ->
        request.attributes.put(MeasureAttributeKey, ClientCallMeasure(Timer.start(registry)))
    }

    onResponse { response ->
        val request = response.call.request
        val outcome = when (response.status.value) {
            in 200..399 -> "success"
            in 400..499 -> "client_error"
            in 500..599 -> "server_error"
            else -> "unknown"
        }
        recordHttpClientMetric(
            request.attributes.getOrNull(MeasureAttributeKey),
            downstreamHost = request.url.host,
            route = request.attributes.getOrNull(RouteAttributeKey) ?: UKJENT_ROUTE,
            method = request.method.value,
            status = response.status.value.toString(),
            outcome = outcome,
        )
    }

    // Fanger forsøk som aldri fikk svar (timeout/tilkoblingsfeil). Measure kan mangle om unntaket
    // oppstod før SendingRequest fikk kjørt - det ytre sikkerhetsnettet i requestPipeline fanger
    // det tilfellet i stedet.
    client.sendPipeline.intercept(HttpSendPipeline.Before) {
        try {
            proceed()
        } catch (e: Throwable) {
            recordHttpClientMetric(
                context.attributes.getOrNull(MeasureAttributeKey),
                downstreamHost = context.url.host,
                route = context.attributes.getOrNull(RouteAttributeKey) ?: UKJENT_ROUTE,
                method = context.method.value,
                status = "n/a",
                outcome = classifyExceptionOutcome(e),
            )
            throw e
        }
    }
}
