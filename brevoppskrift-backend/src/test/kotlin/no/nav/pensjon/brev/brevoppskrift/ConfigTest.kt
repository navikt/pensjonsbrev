package no.nav.pensjon.brev.brevoppskrift

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigResolveOptions
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.config.getAs
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.WorkshopUrl
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ConfigTest {
    @Test
    fun `delegated settings follow copied config without changing its serialized structure`() {
        val original = loadConfig()
        val config = original.copy(
            azure = original.azure.copy(issuer = "https://updated.invalid"),
            database = original.database.copy(port = 5433),
            integrations = IntegrationsConfig(WorkshopUrl("https://updated-workshop.invalid")),
        )
        assertThat(config.issuer).isEqualTo(config.azure.issuer)
        assertThat(config.port).isEqualTo(config.database.port)
        assertThat(config.webUrl).isEqualTo(config.integrations.webUrl)
        val serialized = Json.encodeToJsonElement(BrevoppskriftConfig.serializer(), config)
        assertThat(serialized.jsonObject.keys).containsExactly("azure", "database", "integrations")
        val restored = Json.decodeFromJsonElement(BrevoppskriftConfig.serializer(), serialized)
        assertThat(restored).isEqualTo(config)
        assertThat(restored.issuer).isEqualTo(config.issuer)
        assertThat(restored.port).isEqualTo(config.port)
        assertThat(restored.webUrl).isEqualTo(config.webUrl)
    }

    @Test
    fun `typed HOCON configuration keeps defaults and optional integrations disabled`() {
        val config = loadConfig()
        assertThat(config.azure.editorGroupId.value).isEqualTo("editors")
        assertThat(config.database.jdbcUrl).isEqualTo("jdbc:postgresql://localhost/example")
        assertThat(config.database.maxPoolSize).isEqualTo(3)
        assertThat(config.integrations).isEqualTo(IntegrationsConfig())
        assertThat(resolvedConfig().getInt("ktor.deployment.port")).isEqualTo(8080)
    }

    @Test
    fun `Nais preauthorized apps JSON is decoded through HOCON without losing entries`() {
        val config = loadConfig(environment + ("AZURE_APP_PRE_AUTHORIZED_APPS" to """
            [
                {"name":"dev-gcp:pensjonsbrev:brevoppskrift-web","clientId":"bff"},
                {"name":"dev-gcp:pensjonsbrev:another-app","clientId":"another-client"}
            ]
        """.trimIndent()))
        assertThat(config.azure.preAuthApps.map { it.name to it.clientId.value }).containsExactly(
            "dev-gcp:pensjonsbrev:brevoppskrift-web" to "bff",
            "dev-gcp:pensjonsbrev:another-app" to "another-client",
        )
        assertThat(loadConfig(environment + ("AZURE_APP_PRE_AUTHORIZED_APPS" to "[]")).azure.preAuthApps).isEmpty()
        assertThatThrownBy { loadConfig(environment + ("AZURE_APP_PRE_AUTHORIZED_APPS" to "invalid")) }.isInstanceOf(SerializationException::class.java)
    }

    @Test
    fun `Nais database properties and port overrides are read from HOCON`() {
        val substitutions = environment - "DB_JDBC_URL" + mapOf(
            "DB_HOST" to "database",
            "DB_PORT" to "5433",
            "DB_DATABASE" to "brevoppskrift",
            "PORT" to "8085",
        )
        val config = loadConfig(substitutions)
        assertThat(config.database.jdbcUrl).isNull()
        assertThat(config.database.host).isEqualTo("database")
        assertThat(config.database.port).isEqualTo(5433)
        assertThat(config.database.name).isEqualTo("brevoppskrift")
        assertThat(resolvedConfig(substitutions).getInt("ktor.deployment.port")).isEqualTo(8085)
    }

    @Test
    fun `authentication and database cannot silently fall back to local configuration`() {
        (environment.keys - "DB_JDBC_URL").forEach { key ->
            assertThatThrownBy { loadConfig(environment - key) }.hasMessageContaining(key)
        }
        assertThatThrownBy { loadConfig(environment - "DB_JDBC_URL") }.hasStackTraceContaining("Database requires")
        assertThatThrownBy { loadConfig(environment + ("EDITOR_GROUP_ID" to "")) }.hasStackTraceContaining("Azure configuration must not be blank")
    }

    @Test
    fun `public workshop URL can be overridden independently of integrations`() {
        val config = loadConfig(environment + ("PUBLIC_WEB_URL" to "https://example.invalid"))
        assertThat(config.integrations.webUrl.value).isEqualTo("https://example.invalid")
    }

    private fun loadConfig(substitutions: Map<String, String> = environment) =
        HoconApplicationConfig(resolvedConfig(substitutions)).config("brevoppskrift").getAs<BrevoppskriftConfig>()

    private fun resolvedConfig(substitutions: Map<String, String> = environment) =
        ConfigFactory.parseResources("application.conf")
            .withFallback(ConfigFactory.parseMap(substitutions))
            .resolve(ConfigResolveOptions.defaults().setUseSystemEnvironment(false))

    private val environment = mapOf(
        "AZURE_OPENID_CONFIG_ISSUER" to "https://issuer.invalid",
        "AZURE_APP_CLIENT_ID" to "backend",
        "AZURE_OPENID_CONFIG_JWKS_URI" to "https://issuer.invalid/jwks",
        "AZURE_APP_PRE_AUTHORIZED_APPS" to """[{"name":"dev-gcp:pensjonsbrev:brevoppskrift-web","clientId":"bff"}]""",
        "EDITOR_GROUP_ID" to "editors",
        "DB_JDBC_URL" to "jdbc:postgresql://localhost/example",
        "DB_USERNAME" to "synthetic",
        "DB_PASSWORD" to "synthetic",
    )
}
