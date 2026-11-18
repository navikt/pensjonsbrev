package no.nav.pensjon.brev.brevoppskrift.services

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.timeout
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.IOException

class HttpClientFactoryTest {
    @Test
    fun `request timeout cancels transport and records one attempt`(): Unit = runBlocking {
        var requests = 0
        val engine = MockEngine {
            requests++
            delay(10_000)
            respond("unexpected")
        }
        engine.use {
            val metrics = createMetrics()
            try {
                HttpClientFactory.lagHttpClient(engine, metrics, ExternalService.VERTEX).use { client ->
                    val failure = runCatching {
                        client.post("https://aiplatform.invalid/private") {
                            metricsOperation(ClientOperation.GENERATE)
                            timeout { requestTimeoutMillis = 50 }
                        }
                    }.exceptionOrNull()
                    assertThat(failure).isInstanceOf(HttpRequestTimeoutException::class.java)
                    assertThat(requests).isEqualTo(1)
                    assertThat(metrics.get("brevoppskrift.http.client").tag("outcome", "timeout").timer().count()).isEqualTo(1)
                }
            } finally {
                metrics.close()
            }
        }
    }

    @Test
    fun `clients do not retry writes follow redirects or expose URLs in metrics`(): Unit = runBlocking {
        var requests = 0
        val engine = MockEngine { call ->
            requests++
            assertThat(call.headers[HttpHeaders.XRequestId]).isNull()
            respond("", HttpStatusCode.Found, headersOf(HttpHeaders.Location, "https://other.invalid/private"))
        }
        engine.use {
            val metrics = createMetrics()
            try {
                HttpClientFactory.lagHttpClient(engine, metrics, ExternalService.SLACK).use { client ->
                    val response = client.post("https://hooks.slack.com/services/secret-webhook") {
                        metricsOperation(ClientOperation.NOTIFY)
                        setBody("secret-body")
                    }
                    assertThat(response.status).isEqualTo(HttpStatusCode.Found)
                    assertThat(requests).isEqualTo(1)
                    assertThat(metrics.get("brevoppskrift.http.client").timer().count()).isEqualTo(1)
                    assertThat(metrics.scrape()).contains("operation=\"NOTIFY\"", "service=\"SLACK\"")
                        .doesNotContain("secret-webhook", "secret-body", "other.invalid", "hooks.slack.com")
                }
            } finally {
                metrics.close()
            }
        }
    }

    @Test
    fun `failed calls are measured once and client shutdown does not own the engine`(): Unit = runBlocking {
        var requests = 0
        val engine = MockEngine { requests++; throw IOException("secret-url") }
        engine.use {
            val metrics = createMetrics()
            try {
                val client = HttpClientFactory.lagHttpClient(engine, metrics, ExternalService.GITHUB)
                try {
                    assertThat(runCatching { client.get("https://api.github.com/private/identifier") }.exceptionOrNull()).isInstanceOf(IOException::class.java)
                    assertThat(requests).isEqualTo(1)
                    assertThat(metrics.get("brevoppskrift.http.client").tag("outcome", "connection_error").timer().count()).isEqualTo(1)
                    assertThat(metrics.scrape()).doesNotContain("secret-url", "identifier")
                } finally {
                    client.close()
                }
                assertThat(client.coroutineContext[Job]?.isActive).isFalse()
                assertThat(engine.coroutineContext[Job]?.isActive).isTrue()
            } finally {
                metrics.close()
            }
        }
    }
}
