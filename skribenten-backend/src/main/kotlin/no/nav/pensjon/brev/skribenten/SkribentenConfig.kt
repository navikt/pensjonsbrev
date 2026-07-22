package no.nav.pensjon.brev.skribenten

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import no.nav.pensjon.brev.skribenten.auth.ADGroup
import no.nav.pensjon.brev.skribenten.auth.SkribentenADGroups

@Serializable
data class SkribentenConfig(
    val azureAD: AzureADConfig,
    val database: DatabaseConfig,
    val unleash: UnleashCfg,
    val valkey: ValkeyConfig,
    val groups: GroupsConfig,
    val cors: CorsConfig,
    val krypteringsnoekkel: String,
    val services: ServicesConfig,
)

@Serializable
data class ValkeyConfig(
    val enabled: Boolean,
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val ssl: Boolean,
)

@Serializable
data class DatabaseConfig(
    val host: String,
    val port: String,
    val name: String,
    val username: String,
    val password: String,
    val maxPoolSize: Int,
)

@Serializable
data class UnleashCfg(
    val appName: String,
    // TODO: Slett environment her når vi oppgraderer til neste unleash-sdk
    val environment: String,
    val host: String,
    val apiToken: String,
)


@Serializable
data class AzureADConfig(
    val issuer: String,
    val jwksUrl: String,
    val clientId: String,
    val tokenEndpoint: String,
    val clientSecret: String,
    @Serializable(with = PreAuthorizedAppsSerializer::class)
    val preAuthApps: List<PreAuthorizedApp>,
) {
    @Serializable
    data class PreAuthorizedApp(val name: String, val clientId: String)
}

// Ktor's HoconApplicationConfig#getAs flattens config into a Map<String, String> and detects
// lists via a "path.size" key. HOCON env-var substitution (${AZURE_APP_PRE_AUTHORIZED_APPS})
// only ever exposes this as a single opaque JSON string, never a real HOCON list, so the
// default list decoding silently yields an empty list. PreAuthorizedAppsSerializer works
// around this by decoding the raw string ourselves and parsing it as JSON.
object PreAuthorizedAppsSerializer : KSerializer<List<AzureADConfig.PreAuthorizedApp>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PreAuthorizedApps", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): List<AzureADConfig.PreAuthorizedApp> =
        Json.decodeFromString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: List<AzureADConfig.PreAuthorizedApp>) {
        encoder.encodeString(Json.encodeToString(value))
    }
}

@Serializable
data class GroupsConfig(
    override val pensjonUtland: ADGroup,
    override val fortroligAdresse: ADGroup,
    override val strengtFortroligAdresse: ADGroup,
    override val pensjonSaksbehandler: ADGroup,
    override val attestant: ADGroup,
    override val veileder: ADGroup,
    override val okonomi: ADGroup,
    override val brukerhjelpA: ADGroup,
    override val klagebehandler: ADGroup,
): SkribentenADGroups

@Serializable
data class CorsConfig(
    val host: String,
    val schemes: List<String>,
)

@Serializable
data class OboClientConfig(val url: String, val scope: String)

@Serializable
data class SafConfig(
    val url: String,
    val restUrl: String,
    val scope: String,
)

@Serializable
data class FoerstesideConfig(
    val url: String,
    val restUrl: String,
    val scope: String,
)

@Serializable
data class NoAuthClientConfig(val url: String)

@Serializable
data class ExternalApiConfig(val skribentenWebUrl: String)

@Serializable
data class ServicesConfig(
    val pen: OboClientConfig,
    val pdl: OboClientConfig,
    val saf: SafConfig,
    val pensjonPersondata: OboClientConfig,
    val pensjonRepresentasjon: OboClientConfig,
    val krr: OboClientConfig,
    val brevbaker: OboClientConfig,
    val brevmetadata: NoAuthClientConfig,
    val navansatt: OboClientConfig,
    val samhandlerProxy: OboClientConfig,
    val norg2: NoAuthClientConfig,
    val externalApi: ExternalApiConfig,
    val leader: NoAuthClientConfig? = null,
    val skjerming: OboClientConfig,
    val foerstesideConfig: FoerstesideConfig,
)