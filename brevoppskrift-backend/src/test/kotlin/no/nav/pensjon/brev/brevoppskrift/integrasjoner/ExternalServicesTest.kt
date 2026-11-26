package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.*
import io.ktor.http.content.TextContent
import kotlinx.coroutines.*
import no.nav.pensjon.brev.brevoppskrift.VertexConfig
import no.nav.pensjon.brev.brevoppskrift.api.ApiFailure
import no.nav.pensjon.brev.brevoppskrift.createMetrics
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.model.DraftLanguage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import java.io.IOException

class ExternalServicesTest {
    private val metrics = createMetrics()

    @AfterEach
    fun closeMetrics() { metrics.close() }

    @Test
    fun `AI preserves markers and rejects malformed or unsafe responses`(): Unit = runBlocking {
        val request = AiRequest("Du får {0} <b>beløp</b>", DraftLanguage.NB)
            for (text in listOf("Du får {0} <b>beløp</b>", "Du får beløp", "<b>{0}</i>", "{0} <b>beløp</b><script>x</script>")) {
                MockEngine { call ->
                    val body = if (call.url.host == "metadata.google.internal") {
                        assertThat(call.headers["Metadata-Flavor"]).isEqualTo("Google")
                        assertThat(call.headers[HttpHeaders.Authorization]).isNull()
                        """{"access_token":"synthetic-token"}"""
                    } else {
                        assertThat(call.url.host).isEqualTo("aiplatform.eu.rep.googleapis.com")
                        assertThat(call.headers[HttpHeaders.Authorization]).isEqualTo("Bearer synthetic-token")
                        assertThat((call.body as TextContent).text).contains("nynorsk")
                        aiResponse(text)
                    }
                    respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
                }.use { engine ->
                    VertexAi(engine, metrics, vertex).use { service ->
                        val result = runCatching { service.suggest(request, true) }
                        if (text == request.text) assertThat(result.getOrThrow().text).isEqualTo(text)
                        else assertThat((result.exceptionOrNull() as IntegrationFailure).code).isEqualTo("INVALID_AI_RESPONSE")
                }
            }
        }
    }

    @Test
    fun `upstream errors redirects invalid JSON and oversized bodies are safe failures`(): Unit = runBlocking {
            for (failure in listOf("status", "redirect", "json", "large", "token", "large-token", "invalid-token")) {
                var calls = 0
                MockEngine { request ->
                    calls++
                    if (failure.endsWith("token") || request.url.host != "metadata.google.internal") {
                        when (failure) {
                            "status" -> respond("synthetic-private-error", HttpStatusCode.ServiceUnavailable)
                            "redirect" -> respond("", HttpStatusCode.TemporaryRedirect, headersOf(HttpHeaders.Location, "https://other.invalid/secret"))
                            "json" -> respond("{synthetic-private-error")
                            "large" -> respond("x".repeat(1024 * 1024 + 1))
                            "large-token" -> respond("""{"access_token":"${"x".repeat(16 * 1024)}"}""")
                            "invalid-token" -> respond("""{"access_token":"invalid\nheader"}""")
                            else -> respond("""{"access_token":123}""")
                        }
                    } else respond("""{"access_token":"synthetic-token"}""")
                }.use { engine ->
                    VertexAi(engine, metrics, vertex).use { service ->
                        val failureResult = runCatching { service.suggest(AiRequest("synthetic", DraftLanguage.NB), false) }.exceptionOrNull()
                        assertThat(failureResult).isInstanceOf(IntegrationFailure::class.java)
                        assertThat(failureResult?.message).isEqualTo("VERTEX_UNAVAILABLE")
                        assertThat(calls).isEqualTo(if (failure.endsWith("token")) 1 else 2)
                }
            }
        }
    }

    @Test
    fun `invalid AI input never reaches transport and connection errors are sanitized`(): Unit = runBlocking {
            var calls = 0
            MockEngine { calls++; throw IOException("synthetic-private-details") }.use { engine ->
                VertexAi(engine, metrics, vertex).use { service ->
                    for (request in listOf(AiRequest("", DraftLanguage.NB), AiRequest("x".repeat(100_001), DraftLanguage.NB), AiRequest("x", DraftLanguage.NB, "x".repeat(1001)))) {
                        assertThat((runCatching { service.suggest(request, false) }.exceptionOrNull() as ApiFailure).code).isEqualTo("INVALID_AI_REQUEST")
                    }
                    assertThat(calls).isZero()
                    assertThat((runCatching { service.suggest(AiRequest("x", DraftLanguage.NB), false) }.exceptionOrNull() as IntegrationFailure).code).isEqualTo("VERTEX_UNAVAILABLE")
                }
                assertThat(calls).isEqualTo(1)
                assertThat(engine.coroutineContext[Job]?.isActive).isTrue()
        }
    }

    @Test
    fun `Slack omits document names and does not follow webhook redirects`(): Unit = runBlocking {
            for (status in listOf(HttpStatusCode.OK, HttpStatusCode.TemporaryRedirect, HttpStatusCode.InternalServerError)) {
                var calls = 0
                MockEngine { call ->
                    calls++
                    assertThat(call.url.host).isEqualTo("hooks.slack.com")
                    assertThat((call.body as TextContent).text).doesNotContain("synthetic-private-name").contains("Nytt utkast", "arbeidsutkast/id")
                    respond("synthetic-private-details", status, headersOf(HttpHeaders.Location, "https://other.invalid"))
                }.use { engine ->
                    SlackNotifications(engine, metrics, webhook).use { service ->
                        val result = runCatching { service.send(Notification("Nytt utkast", "synthetic-private-name", 1, "https://workshop.invalid/arbeidsutkast/id")) }
                        if (status.isSuccess()) assertThat(result.isSuccess).isTrue()
                        else assertThat(result.exceptionOrNull()?.message).isEqualTo("SLACK_UNAVAILABLE")
                    }
                    assertThat(calls).isEqualTo(1)
            }
            assertThat(metrics.scrape()).doesNotContain("services/", "synthetic-private", "workshop.invalid", "hooks.slack.com")
        }
    }

    companion object {
        val vertex = VertexConfig(VertexProjectId("synthetic-project"), VertexLocation("europe-west4"), VertexModelId("synthetic-model"))
        val webhook = SlackWebhookUrl("https://hooks.slack.com/services/synthetic")

        fun aiResponse(text: String): String = draftObjectMapper.writeValueAsString(
            mapOf("candidates" to listOf(mapOf("content" to mapOf("parts" to listOf(mapOf("text" to text)))))),
        )
    }
}
