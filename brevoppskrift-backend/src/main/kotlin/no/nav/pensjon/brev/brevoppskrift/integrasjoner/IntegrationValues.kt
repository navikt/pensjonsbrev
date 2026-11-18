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
value class GitHubAppId(val value: String)

@Serializable
@JvmInline
value class GitHubInstallationId(val value: String)

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
    override fun toString(): String = "[redacted]"
}

@Serializable
@JvmInline
value class WorkshopUrl(val value: String) {
    override fun toString(): String = value
}
