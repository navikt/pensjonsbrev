package no.nav.pensjon.brev.skribenten

import kotlinx.serialization.Serializable

@Serializable
data class SkribentenConfig(
    val azureAD: AzureADConfig,
    val database: DatabaseConfig,
    val unleash: UnleashCfg,
    val valkey: ValkeyConfig,
)

@Serializable
data class ValkeyConfig(
    val enabled: Boolean,
    val host: String,
    val port: String,
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
    val preAuthApps: List<PreAuthorizedApp>,
) {
    @Serializable
    data class PreAuthorizedApp(val name: String, val clientId: String)
}