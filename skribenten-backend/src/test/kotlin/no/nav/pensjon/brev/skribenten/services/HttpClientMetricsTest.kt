package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import no.nav.pensjon.brev.skribenten.Metrics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * Verifiserer HttpClientMetrics (services/HttpClientMetrics.kt), som instrumenterer nedstrøms
 * HTTP-kall for skribenten-backend.
 */
class HttpClientMetricsTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    private fun timerCount(route: String, outcome: String): Double =
        Metrics.registry.find("skribenten_http_client_requests_seconds")
            .tag("route", route)
            .tag("outcome", outcome)
            .timers()
            .sumOf { it.count().toDouble() }

    @Test
    suspend fun `vellykket kall registreres med downstream_host, route, status og outcome`() {
        val route = "test/success/${System.nanoTime()}"
        val engine = MockEngine { respond("OK", HttpStatusCode.OK) }
        val client = HttpClientFactory.lagHttpClient(engine) {
            defaultRequest { url("http://nedstroms-test.example") }
        }

        client.get("/") { metricsRoute(route) }

        val timer = Metrics.registry.find("skribenten_http_client_requests_seconds")
            .tag("downstream_host", "nedstroms-test.example")
            .tag("route", route)
            .tag("status", "200")
            .tag("outcome", "success")
            .timer()

        assertThat(timer).isNotNull
        assertThat(timer!!.count()).isEqualTo(1)
    }

    @Test
    suspend fun `5xx-svar registreres som server_error`() {
        val route = "test/server-error/${System.nanoTime()}"
        val engine = MockEngine { respond("feil", HttpStatusCode.InternalServerError) }
        val client = HttpClientFactory.lagHttpClient(engine) {
            defaultRequest { url("http://nedstroms-test.example") }
        }

        client.get("/") { metricsRoute(route) }

        assertThat(timerCount(route, "server_error")).isEqualTo(1.0)
    }

    @Test
    suspend fun `4xx-svar registreres som client_error`() {
        val route = "test/client-error/${System.nanoTime()}"
        val engine = MockEngine { respond("feil", HttpStatusCode.NotFound) }
        val client = HttpClientFactory.lagHttpClient(engine) {
            defaultRequest { url("http://nedstroms-test.example") }
        }

        client.get("/") { metricsRoute(route) }

        assertThat(timerCount(route, "client_error")).isEqualTo(1.0)
    }

    @Test
    suspend fun `hvert forsoek telles separat naar retry gjor flere forsoek`() {
        val route = "test/retry/${System.nanoTime()}"
        val attempts = AtomicInteger(0)
        val engine = MockEngine {
            if (attempts.getAndIncrement() == 0) {
                respond("feil", HttpStatusCode.BadGateway)
            } else {
                respond("OK", HttpStatusCode.OK)
            }
        }
        val client = HttpClientFactory.lagHttpClient(engine) {
            defaultRequest { url("http://nedstroms-test.example") }
            installRetry(logger)
        }

        client.get("/") { metricsRoute(route) }

        // Beviser at hvert forsøk telles separat, ikke bare det endelige, aggregerte utfallet.
        assertThat(attempts.get()).isEqualTo(2)
        assertThat(timerCount(route, "server_error")).isEqualTo(1.0)
        assertThat(timerCount(route, "success")).isEqualTo(1.0)
    }

    @Test
    suspend fun `hvert forsoek telles separat ogsaa naar metrikk-pluginen installeres foer retry`() {
        // Motsatt installasjonsrekkefølge av lagHttpClient - beviser at telling per forsøk ikke
        // avhenger av rekkefølgen mellom HttpClientMetrics og retry-plugins.
        val route = "test/retry-reversed-order/${System.nanoTime()}"
        val attempts = AtomicInteger(0)
        val engine = MockEngine {
            if (attempts.getAndIncrement() == 0) {
                respond("feil", HttpStatusCode.BadGateway)
            } else {
                respond("OK", HttpStatusCode.OK)
            }
        }
        val client = HttpClient(engine) {
            defaultRequest { url("http://nedstroms-test.example") }
            install(HttpClientMetrics) { registry = Metrics.registry }
            installRetry(logger)
        }

        client.get("/") { metricsRoute(route) }

        assertThat(attempts.get()).isEqualTo(2)
        assertThat(timerCount(route, "server_error")).isEqualTo(1.0)
        assertThat(timerCount(route, "success")).isEqualTo(1.0)
    }

    @Test
    suspend fun `kall uten eksplisitt route far ukjent-label`() {
        val engine = MockEngine { respond("OK", HttpStatusCode.OK) }
        val client = HttpClientFactory.lagHttpClient(engine) {
            defaultRequest { url("http://nedstroms-test-ukjent.example") }
        }

        client.get("/")

        val timer = Metrics.registry.find("skribenten_http_client_requests_seconds")
            .tag("downstream_host", "nedstroms-test-ukjent.example")
            .tag("route", "ukjent")
            .timer()

        assertThat(timer).isNotNull
    }

    @Test
    suspend fun `unntak uten respons registreres med status n-slash-a`() {
        val route = "test/exception/${System.nanoTime()}"
        val engine = MockEngine { throw kotlinx.io.IOException("boom") }
        val client = HttpClientFactory.lagHttpClient(engine) {
            defaultRequest { url("http://nedstroms-test.example") }
        }

        try {
            client.get("/") { metricsRoute(route) }
        } catch (_: Exception) {
            // Forventet: MockEngine kaster videre uten at kallet noensinne fikk et svar.
        }

        val timer = Metrics.registry.find("skribenten_http_client_requests_seconds")
            .tag("downstream_host", "nedstroms-test.example")
            .tag("route", route)
            .tag("status", "n/a")
            .timer()

        assertThat(timer).isNotNull
        assertThat(timer!!.count()).isEqualTo(1)
    }

    @Test
    suspend fun `unntak foer send-fasen fanges av det ytre sikkerhetsnettet uten dobbel telling`() {
        val route = "test/pre-send-exception/${System.nanoTime()}"
        // Simulerer en feil før Send-fasen, som verken SendingRequest, onResponse eller on(Send)
        // ser.
        val failBeforeSend = createClientPlugin("FailBeforeSend") {
            onRequest { _, _ -> throw IllegalStateException("boom foer send-fasen") }
        }
        val engine = MockEngine { respond("OK", HttpStatusCode.OK) }
        val client = HttpClientFactory.lagHttpClient(engine) {
            defaultRequest { url("http://nedstroms-test.example") }
            install(failBeforeSend)
        }

        try {
            client.get("/") { metricsRoute(route) }
        } catch (_: Exception) {
            // Forventet: pluginen kaster før requestet noensinne blir sendt.
        }

        val timer = Metrics.registry.find("skribenten_http_client_requests_seconds")
            .tag("downstream_host", "nedstroms-test.example")
            .tag("route", route)
            .tag("status", "n/a")
            .timer()

        assertThat(timer).isNotNull
        // Nøyaktig én registrering - verken 0 (uten sikkerhetsnett) eller 2 (dobbeltregistrering).
        assertThat(timer!!.count()).isEqualTo(1)
    }
}
