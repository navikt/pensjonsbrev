package no.nav.pensjon.brev.brevoppskrift

import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import no.nav.pensjon.brev.brevoppskrift.lagring.DatabaseConfig
import no.nav.pensjon.brev.brevoppskrift.auth.EntraAudience
import no.nav.pensjon.brev.brevoppskrift.auth.EntraClientId
import no.nav.pensjon.brev.brevoppskrift.auth.EntraGroupId
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*

@Serializable
data class BrevoppskriftConfig(
    val azure: AzureConfig,
    val database: DatabaseConfig,
    val integrations: IntegrationsConfig = IntegrationsConfig(),
)

@Serializable
data class AzureConfig(
    val issuer: String,
    val audience: EntraAudience,
    val jwksUri: String,
    @Serializable(with = PreAuthorizedAppsSerializer::class)
    val preAuthApps: List<PreAuthorizedApp>,
    val editorGroupId: EntraGroupId,
) {
    init {
        require(listOf(issuer, audience.value, jwksUri, editorGroupId.value).all { it.isNotBlank() }) {
            "Azure configuration must not be blank"
        }
    }

    @Serializable
    data class PreAuthorizedApp(val name: String, val clientId: EntraClientId)
}

// HOCON exposes the Nais environment variable as a JSON string, not a list, to Ktor getAs.
object PreAuthorizedAppsSerializer : KSerializer<List<AzureConfig.PreAuthorizedApp>> {
    override val descriptor = PrimitiveSerialDescriptor("PreAuthorizedApps", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): List<AzureConfig.PreAuthorizedApp> =
        Json.decodeFromString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: List<AzureConfig.PreAuthorizedApp>) {
        encoder.encodeString(Json.encodeToString(value))
    }
}

@Serializable
data class IntegrationsConfig(
    val webUrl: WorkshopUrl = WorkshopUrl("https://pensjon-brevoppskrift.intern.dev.nav.no"),
    val vertex: VertexConfig = VertexConfig(),
    val slackWebhookUrl: SlackWebhookUrl? = null,
    val github: GitHubConfig = GitHubConfig(),
)

@Serializable
data class VertexConfig(
    val projectId: VertexProjectId? = null,
    val location: VertexLocation? = null,
    val model: VertexModelId? = null,
) {
    init {
        require(listOf(projectId, location, model).all { it == null } || listOf(projectId?.value, location?.value, model?.value).all { !it.isNullOrBlank() }) {
            "Vertex requires projectId, location and model"
        }
    }
}

@Serializable
data class GitHubConfig(
    val appId: GitHubAppId? = null,
    val installationId: GitHubInstallationId? = null,
    val privateKey: String? = null,
    val personalAccessToken: String? = null,
) {
    init {
        require(listOf(appId, installationId, privateKey).all { it == null } || listOf(appId?.value, installationId?.value, privateKey).all { !it.isNullOrBlank() }) {
            "GitHub requires appId, installationId and privateKey"
        }
        require(personalAccessToken == null || personalAccessToken.isNotBlank()) {
            "GitHub personal access token must not be blank"
        }
        require(personalAccessToken == null || appId == null) {
            "Configure either a GitHub App or a personal access token, not both"
        }
    }

    override fun toString() = "GitHubConfig(appId=$appId, installationId=$installationId, privateKeyConfigured=${privateKey != null}, personalAccessTokenConfigured=${personalAccessToken != null})"
}
