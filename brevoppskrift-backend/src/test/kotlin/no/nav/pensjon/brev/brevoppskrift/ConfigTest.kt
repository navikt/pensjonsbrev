package no.nav.pensjon.brev.brevoppskrift

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigResolveOptions
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.config.getAs
import kotlinx.serialization.SerializationException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.*

class ConfigTest {
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
    fun `configured integrations require complete typed configuration`() {
        val integrations = mapOf(
            "VERTEX_PROJECT_ID" to "synthetic-project",
            "VERTEX_LOCATION" to "europe-west1",
            "VERTEX_MODEL" to "synthetic-model",
            "SLACK_WEBHOOK_URL" to "https://hooks.slack.com/synthetic",
            "GITHUB_APP_ID" to "1",
            "GITHUB_INSTALLATION_ID" to "2",
            "GITHUB_APP_PRIVATE_KEY" to "synthetic",
            "PUBLIC_WEB_URL" to "https://example.invalid",
        )
        val config = loadConfig(environment + integrations).integrations
        assertThat(config.vertex).isEqualTo(VertexConfig(VertexProjectId("synthetic-project"), VertexLocation("europe-west1"), VertexModelId("synthetic-model")))
        assertThat(config.github).isEqualTo(GitHubConfig(GitHubAppId("1"), GitHubInstallationId("2"), "synthetic"))
        assertThat(config.slackWebhookUrl?.value).isEqualTo(integrations["SLACK_WEBHOOK_URL"])
        assertThat(config.webUrl.value).isEqualTo(integrations["PUBLIC_WEB_URL"])
        assertThat(config.toString()).doesNotContain(requireNotNull(integrations["SLACK_WEBHOOK_URL"]))
        assertThatThrownBy { loadConfig(environment + ("VERTEX_PROJECT_ID" to "synthetic-project")) }.hasStackTraceContaining("Vertex requires")
        assertThatThrownBy { loadConfig(environment + ("GITHUB_APP_ID" to "1")) }.hasStackTraceContaining("GitHub requires")
    }

    @Test
    fun `PAT enables GitHub configuration without app credentials and is not included in diagnostics`() {
        val config = loadConfig(environment + ("GITHUB_PAT" to "synthetic-personal-access-token"))
        assertThat(config.integrations.github.personalAccessToken).isEqualTo("synthetic-personal-access-token")
        assertThat(config.integrations.github.appId).isNull()
        assertThat(config.toString()).doesNotContain("synthetic-personal-access-token")
        val app = GitHubConfig(GitHubAppId("1"), GitHubInstallationId("2"), "synthetic-private-key")
        assertThat(app.toString()).doesNotContain("synthetic-private-key")
    }

    @Test
    fun `blank PAT and mixed GitHub authentication fail configuration`() {
        for (pat in listOf("", "   ")) {
            assertThatThrownBy { loadConfig(environment + ("GITHUB_PAT" to pat)) }
                .hasStackTraceContaining("personal access token must not be blank")
        }
        val app = mapOf(
            "GITHUB_APP_ID" to "1",
            "GITHUB_INSTALLATION_ID" to "2",
            "GITHUB_APP_PRIVATE_KEY" to "synthetic-private-key",
        )
        assertThatThrownBy { loadConfig(environment + app + ("GITHUB_PAT" to "synthetic-pat")) }
            .hasStackTraceContaining("either a GitHub App or a personal access token")
        assertThatThrownBy { loadConfig(environment + mapOf("GITHUB_APP_ID" to "1", "GITHUB_PAT" to "synthetic-pat")) }
            .hasStackTraceContaining("GitHub requires")
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
