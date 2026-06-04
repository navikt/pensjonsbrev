package no.nav.pensjon.brev.skribenten

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions.defaults
import com.typesafe.config.ConfigResolveOptions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SkribentenConfigTest {
    @Test
    fun `database maxPoolSize has default value`() {
        val config = ConfigFactory
            .load("application-local", defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
            .getConfig("skribenten")
            .getConfig("services")

        assertThat(config.getInt("database.maxPoolSize")).isEqualTo(2)
    }
}
