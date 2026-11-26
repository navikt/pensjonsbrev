package no.nav.pensjon.brev.brevoppskrift.services

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.nio.channels.UnresolvedAddressException

class HttpClientMetricsTest {
    private val metrics = createMetrics()

    @AfterEach
    fun closeMetrics() { metrics.close() }

    private fun count(outcome: String): Long =
        metrics.find("brevoppskrift.http.client").tag("outcome", outcome).timers().sumOf { it.count() }

    @Test
    fun `response outcomes are measured once with stable labels`(): Unit = runBlocking {
        MockEngine { request ->
            respond("synthetic", HttpStatusCode.fromValue(request.url.parameters["status"]!!.toInt()))
        }.use { engine ->
            HttpClientFactory.lagHttpClient(engine, metrics, ExternalService.SLACK).use { client ->
                for (status in listOf(200, 307, 404, 503)) {
                    client.get("https://hooks.slack.com/services/private?status=$status") { metricsOperation(ClientOperation.NOTIFY) }
                }
                for (outcome in listOf("success", "redirect", "client_error", "server_error")) {
                    assertThat(count(outcome)).isEqualTo(1)
                }
                assertThat(metrics.get("brevoppskrift.http.client").tag("service", "SLACK").tag("operation", "NOTIFY").tag("status", "307").timer().count()).isEqualTo(1)
                assertThat(metrics.scrape()).doesNotContain("hooks.slack.com", "services/private", "?status=")
            }
        }
    }

    @Test
    fun `every retry attempt is recorded regardless of plugin order`(): Unit = runBlocking {
        for (metricsFirst in listOf(true, false)) {
            var calls = 0
            MockEngine {
                calls++
                respond("synthetic", if (calls == 1) HttpStatusCode.BadGateway else HttpStatusCode.OK)
            }.use { engine ->
                HttpClient(engine) {
                    if (metricsFirst) install(HttpClientMetrics) { registry = metrics; service = ExternalService.VERTEX }
                    install(HttpRequestRetry) {
                        retryOnServerErrors(maxRetries = 1)
                        delayMillis { 0 }
                    }
                    if (!metricsFirst) install(HttpClientMetrics) { registry = metrics; service = ExternalService.VERTEX }
                }.use { client ->
                    val errorsBefore = count("server_error")
                    val successesBefore = count("success")
                    client.get("https://vertex.invalid/private") { metricsOperation(ClientOperation.GENERATE) }
                    assertThat(calls).isEqualTo(2)
                    assertThat(count("server_error") - errorsBefore).isEqualTo(1)
                    assertThat(count("success") - successesBefore).isEqualTo(1)
                }
            }
        }
    }

    @Test
    fun `failures before sending are measured without executing transport`(): Unit = runBlocking {
        var calls = 0
        val failBeforeSend = createClientPlugin("FailBeforeSend") {
            onRequest { _, _ -> throw IllegalStateException("synthetic-private-details") }
        }
        MockEngine { calls++; respond("unexpected") }.use { engine ->
            HttpClient(engine) {
                install(HttpClientMetrics) { registry = metrics; service = ExternalService.SLACK }
                install(failBeforeSend)
            }.use { client ->
                assertThat(runCatching { client.get("https://slack.invalid/private") }.exceptionOrNull()).isInstanceOf(IllegalStateException::class.java)
                assertThat(count("exception")).isEqualTo(1)
            }
            HttpClientFactory.lagHttpClient(engine, metrics, ExternalService.SLACK).use { client ->
                assertThat(runCatching { client.post("https://slack.invalid/private") { setBody(Any()) } }.isFailure).isTrue()
                assertThat(count("exception")).isEqualTo(2)
            }
            assertThat(calls).isZero()
            assertThat(metrics.get("brevoppskrift.http.client").tag("operation", "REQUEST").tag("status", "n/a").tag("method", "POST").timer().count()).isEqualTo(1)
            assertThat(metrics.scrape()).doesNotContain("synthetic-private-details", "slack.invalid", "private")
        }
    }

    @Test
    fun `unresolved addresses and cancellation are classified without double counting`(): Unit = runBlocking {
        for (failure in listOf(UnresolvedAddressException(), CancellationException("synthetic-private-details"))) {
            MockEngine { throw failure }.use { engine ->
                HttpClientFactory.lagHttpClient(engine, metrics, ExternalService.VERTEX).use { client ->
                    assertThat(runCatching { client.get("https://vertex.invalid/private") }.exceptionOrNull()).isInstanceOf(failure.javaClass)
                }
            }
        }
        assertThat(count("connection_error")).isEqualTo(1)
        assertThat(count("cancelled")).isEqualTo(1)
    }
}
