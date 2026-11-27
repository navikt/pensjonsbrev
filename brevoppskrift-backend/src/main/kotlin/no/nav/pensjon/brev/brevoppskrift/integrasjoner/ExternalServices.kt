package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.*
import io.ktor.utils.io.readBuffer
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.io.readByteArray
import no.nav.pensjon.brev.brevoppskrift.VertexConfig
import no.nav.pensjon.brev.brevoppskrift.api.ApiFailure
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import no.nav.pensjon.brev.brevoppskrift.services.ClientOperation
import no.nav.pensjon.brev.brevoppskrift.services.ExternalService
import no.nav.pensjon.brev.brevoppskrift.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brev.brevoppskrift.services.metricsOperation
import java.io.Closeable
import java.io.IOException

interface ExternalIntegrationSettings {
    val vertex: VertexConfig
    val slackWebhookUrl: SlackWebhookUrl?
}

class SlackNotifications(engine: HttpClientEngine, metrics: MeterRegistry, private val webhook: SlackWebhookUrl) : NotificationService, Closeable {
    private val client = lagHttpClient(engine, metrics, ExternalService.SLACK)

    override suspend fun send(notification: Notification) {
        val text = "${notification.type} (${notification.count})${notification.url?.let { " $it" } ?: ""}"
        try {
            client.preparePost(webhook.value) {
                metricsOperation(ClientOperation.NOTIFY)
                contentType(ContentType.Application.Json)
                setBody(draftObjectMapper.writeValueAsString(mapOf("text" to text)))
            }.execute { response ->
                if (!response.status.isSuccess()) throw IntegrationFailure("SLACK_UNAVAILABLE")
            }
        } catch (_: IOException) {
            throw IntegrationFailure("SLACK_UNAVAILABLE")
        }
    }

    override fun close() { client.close() }
}

class VertexAi(engine: HttpClientEngine, metrics: MeterRegistry, private val config: VertexConfig) : AiService, Closeable {
    init { require(config.projectId != null && config.location != null && config.model != null) }
    private val client = lagHttpClient(engine, metrics, ExternalService.VERTEX)

    override suspend fun suggest(request: AiRequest, nynorsk: Boolean): AiResult {
        validateAiRequest(request)
        try {
            val token = client.prepareGet("http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/token") {
                metricsOperation(ClientOperation.TOKEN)
                header("Metadata-Flavor", "Google")
            }.execute { response ->
                response.json("VERTEX_UNAVAILABLE", 16 * 1024).path("access_token").takeIf { it.isTextual }?.asText()
                    ?.takeIf { Regex("[A-Za-z0-9._~+/-]+=*").matches(it) } ?: throw IntegrationFailure("VERTEX_UNAVAILABLE")
            }
            val instruction = if (nynorsk) "Oversett til nynorsk, med same faglege innhald." else "Forbedre klarspråket på ${request.language}. Ikke endre faglig innhold."
            val payload = mapOf(
                "systemInstruction" to mapOf("parts" to listOf(mapOf("text" to "$instruction Bevar alle {nummer}-plassholdere og <b>/<i>-markeringer. Returner bare forslagets tekst."))),
                "contents" to listOf(mapOf("role" to "user", "parts" to listOf(mapOf("text" to "${request.instruction}\n${request.text}")))),
            )
            val response = client.preparePost("https://aiplatform.eu.rep.googleapis.com/v1/projects/${requireNotNull(config.projectId).value}/locations/${requireNotNull(config.location).value}/publishers/google/models/${requireNotNull(config.model).value}:generateContent") {
                metricsOperation(ClientOperation.GENERATE)
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(draftObjectMapper.writeValueAsString(payload))
            }.execute { it.json("VERTEX_UNAVAILABLE", 1024 * 1024) }
            val suggested = response.path("candidates").path(0).path("content").path("parts").path(0).path("text")
                .takeIf { it.isTextual }?.asText() ?: throw IntegrationFailure("INVALID_AI_RESPONSE")
            if (suggested.isBlank() || suggested.length > 200_000 || markers(request.text) != markers(suggested) || !balanced(suggested) ||
                Regex("</?[^>]+>").findAll(suggested.take(suggested.lastIndexOf('>') + 1)).any { it.value !in setOf("<b>", "</b>", "<i>", "</i>") }
            ) throw IntegrationFailure("INVALID_AI_RESPONSE")
            return AiResult(suggested)
        } catch (_: IOException) {
            throw IntegrationFailure("VERTEX_UNAVAILABLE")
        }
    }

    private fun markers(text: String) = Regex("\\{\\d+}|</?[bi]>").findAll(text).map { it.value }.groupingBy { it }.eachCount()

    private fun balanced(text: String): Boolean {
        val stack = ArrayDeque<String>()
        for (match in Regex("</?([bi])>").findAll(text)) {
            if (!match.value.startsWith("</")) stack.addLast(match.groupValues[1])
            else if (stack.removeLastOrNull() != match.groupValues[1]) return false
        }
        return stack.isEmpty()
    }

    override fun close() { client.close() }
}

internal fun validateAiRequest(request: AiRequest) {
    if (request.text.isBlank() || request.text.length > 100_000 || request.instruction.length > 1000) {
        throw ApiFailure(HttpStatusCode.BadRequest, "INVALID_AI_REQUEST", "KI-forespørselen må ha tekst innenfor lengdegrensene.")
    }
}

private suspend fun HttpResponse.json(error: String, maxBytes: Int): JsonNode {
    if (!status.isSuccess()) throw IntegrationFailure(error)
    val bytes = bodyAsChannel().readBuffer(maxBytes.toLong() + 1).readByteArray()
    if (bytes.size > maxBytes) throw IntegrationFailure(error)
    return draftObjectMapper.readTree(bytes)?.takeUnless { it.isNull || it.isMissingNode } ?: throw IntegrationFailure(error)
}
