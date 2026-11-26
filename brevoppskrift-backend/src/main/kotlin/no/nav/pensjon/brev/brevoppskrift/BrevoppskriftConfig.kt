package no.nav.pensjon.brev.brevoppskrift

import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import no.nav.pensjon.brev.brevoppskrift.lagring.DatabaseConfig
import no.nav.pensjon.brev.brevoppskrift.lagring.DatabaseSettings
import no.nav.pensjon.brev.brevoppskrift.auth.AzureSettings
import no.nav.pensjon.brev.brevoppskrift.auth.EntraAudience
import no.nav.pensjon.brev.brevoppskrift.auth.EntraClientId
import no.nav.pensjon.brev.brevoppskrift.auth.EntraGroupId
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*

@Serializable
data class BrevoppskriftConfig(
    val azure: AzureConfig,
    val database: DatabaseConfig,
    val integrations: IntegrationsConfig = IntegrationsConfig(),
) : AzureSettings by azure, DatabaseSettings by database, NotificationSettings by integrations, ExternalIntegrationSettings by integrations

@Serializable
data class AzureConfig(
    override val issuer: String,
    override val audience: EntraAudience,
    override val jwksUri: String,
    @Serializable(with = PreAuthorizedAppsSerializer::class)
    override val preAuthApps: List<PreAuthorizedApp>,
    override val editorGroupId: EntraGroupId,
) : AzureSettings {
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
    override val webUrl: WorkshopUrl = WorkshopUrl("https://pensjon-brevoppskrift.intern.dev.nav.no"),
    override val vertex: VertexConfig = VertexConfig(),
    override val slackWebhookUrl: SlackWebhookUrl? = null,
) : NotificationSettings, ExternalIntegrationSettings

@Serializable
data class VertexConfig(
    val projectId: VertexProjectId? = null,
    val location: VertexLocation? = null,
    val model: VertexModelId? = null,
) {
    init {
        require(listOf(projectId, location, model).all { it == null } ||
            listOf(projectId?.value, location?.value, model?.value).all { it != null && it !in setOf(".", "..") && Regex("[A-Za-z0-9_.-]+").matches(it) }
        ) { "Vertex requires valid projectId, location and model" }
    }
}
