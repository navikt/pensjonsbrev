package no.nav.pensjon.brev.skribenten.auth


import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions.defaults
import com.typesafe.config.ConfigResolveOptions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ADGroupsTest {

    private val groupsConfig = ConfigFactory
        .load("application-local", defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
        .getConfig("skribenten")
        .getConfig("groups")

    @Test
    fun `groups config loads with init`() {
        ADGroups.init(groupsConfig)
        assertThat(ADGroups.pensjonUtland.id).isNotBlank()
        assertThat(ADGroups.pensjonSaksbehandler.id).isNotBlank()
        assertThat(ADGroups.fortroligAdresse.id).isNotBlank()
        assertThat(ADGroups.strengtFortroligAdresse.id).isNotBlank()
        assertThat(ADGroups.strengtFortroligUtland.id).isNotBlank()
        assertThat(ADGroups.attestant.id).isNotBlank()
    }

}