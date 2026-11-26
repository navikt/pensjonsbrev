package no.nav.pensjon.brev.brevoppskrift.services

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.timeout
import io.ktor.http.*
import kotlinx.coroutines.*
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

class HttpClientFactoryTest {
    @Test
    fun `request timeout cancels transport and records exactly one attempt`(): Unit = runBlocking {
        var requests = 0
        MockEngine {
            requests++
            delay(10.seconds)
            respond("unexpected")
        }.use { engine ->
            val metrics = createMetrics()
            try {
                HttpClientFactory.lagHttpClient(engine, metrics, ExternalService.VERTEX).use { client ->
                    val failure = runCatching {
                        client.post("https://aiplatform.invalid/private") {
                            metricsOperation(ClientOperation.GENERATE)
                            timeout { requestTimeoutMillis = 100 }
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
    fun `failed calls are measured without URLs and closing clients does not close shared engines`(): Unit = runBlocking {
        var requests = 0
        MockEngine { requests++; throw IOException("synthetic-private-url") }.use { engine ->
            val metrics = createMetrics()
            try {
                val client = HttpClientFactory.lagHttpClient(engine, metrics, ExternalService.SLACK)
                try {
                    assertThat(runCatching { client.get("https://hooks.slack.com/services/private") }.exceptionOrNull()).isInstanceOf(IOException::class.java)
                    assertThat(requests).isEqualTo(1)
                    assertThat(metrics.get("brevoppskrift.http.client").tag("outcome", "connection_error").timer().count()).isEqualTo(1)
                    assertThat(metrics.scrape()).doesNotContain("synthetic-private-url", "services/", "hooks.slack.com")
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
