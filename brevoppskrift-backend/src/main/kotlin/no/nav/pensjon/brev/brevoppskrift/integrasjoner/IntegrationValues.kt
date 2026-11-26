package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import kotlinx.serialization.Serializable
import java.util.UUID

@JvmInline
value class PrJobId(val value: UUID) {
    override fun toString(): String = value.toString()
}

@JvmInline
value class PrLeaseToken(val value: UUID)

@JvmInline
value class PrUrl(val value: String) {
    override fun toString(): String = value
}

@Serializable
@JvmInline
value class VertexProjectId(val value: String)

@Serializable
@JvmInline
value class VertexLocation(val value: String)

@Serializable
@JvmInline
value class VertexModelId(val value: String)

@Serializable
@JvmInline
value class SlackWebhookUrl(val value: String) {
    init {
        val uri = runCatching { java.net.URI(value) }.getOrNull()
        require(uri != null && uri.scheme == "https" && uri.host == "hooks.slack.com" && uri.port == -1 && uri.userInfo == null &&
            uri.query == null && uri.fragment == null && uri.path.startsWith("/services/") && uri.path.length > "/services/".length
        ) { "Slack requires an HTTPS hooks.slack.com services URL" }
    }

    override fun toString(): String = "[redacted]"
}

@Serializable
@JvmInline
value class WorkshopUrl(val value: String) {
    override fun toString(): String = value
}
