package no.nav.pensjon.brev.skribenten

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions.defaults
import com.typesafe.config.ConfigResolveOptions
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.config.getAs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SkribentenConfigTest {
    @Test
    fun `database maxPoolSize has default value`() {
        val config = ConfigFactory
            .load("application-local", defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
            .getConfig("skribenten")

        assertThat(config.getInt("database.maxPoolSize")).isEqualTo(2)
    }

    @Test
    fun `can load typed config`() {
        val resolved = ConfigFactory.parseResources("application.conf")
            .withFallback(fakeSubstitutionsFor("application.conf"))
            .resolve()

        val config = HoconApplicationConfig(resolved).config("skribenten").getAs<SkribentenConfig>()

        // PreAuthorizedAppsSerializer decodes the raw JSON string HOCON gives us into a real list.
        assertThat(config.azureAD.preAuthApps).containsExactly(
            AzureADConfig.PreAuthorizedApp("abc:def:hij", "1234"),
            AzureADConfig.PreAuthorizedApp("klm:nop:qrs", "567"),
        )
    }

    /**
     * Finds every required substitution (i.e. `${VAR}`, not the optional `${?VAR}`) in the given
     * classpath resource and builds a fallback [com.typesafe.config.Config] with a fake value for
     * each one, so the file can be resolved in tests without real environment variables set.
     */
    private fun fakeSubstitutionsFor(resource: String): com.typesafe.config.Config {
        val text = requireNotNull(javaClass.classLoader.getResourceAsStream(resource)) {
            "Could not find resource $resource on classpath"
        }.bufferedReader().readText()

        val requiredSubstitution = Regex("""\$\{(?!\?)([^}]+)}""")
        val fakeValues: Map<String, Any> = requiredSubstitution.findAll(text)
            .map { it.groupValues[1] }
            .distinct()
            .associateWith { name -> fakeValueFor(name) }

        return ConfigFactory.parseMap(fakeValues)
    }

    /**
     * Produces a fake value of a plausible type for the given substitution name, since the config
     * schema requires e.g. numbers and lists in some places rather than plain strings.
     */
    private fun fakeValueFor(name: String): Any = when {
        name.contains("PORT") -> 0
        name == "AZURE_APP_PRE_AUTHORIZED_APPS" -> """[{"name":"abc:def:hij", "clientId": "1234"}, {"name":"klm:nop:qrs", "clientId": "567"}]"""
        else -> "fake-$name"
    }
}
