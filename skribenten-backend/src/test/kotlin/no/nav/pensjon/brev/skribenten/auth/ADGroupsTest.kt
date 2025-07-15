package no.nav.pensjon.brev.skribenten.auth


import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions.defaults
import com.typesafe.config.ConfigResolveOptions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ADGroupsTest {

    @Test
    fun `groups config loads with init`() {
        ADGroups.init(lesInnADGrupper())
        assertThat(ADGroups.pensjonUtland.id).isNotBlank()
        assertThat(ADGroups.pensjonSaksbehandler.id).isNotBlank()
        assertThat(ADGroups.fortroligAdresse.id).isNotBlank()
        assertThat(ADGroups.strengtFortroligAdresse.id).isNotBlank()
        assertThat(ADGroups.attestant.id).isNotBlank()
        assertThat(ADGroups.veileder.id).isNotBlank()
        assertThat(ADGroups.okonomi.id).isNotBlank()
        assertThat(ADGroups.brukerhjelpA.id).isNotBlank()
    }

}

private fun lesInnADGrupper(): Config = ConfigFactory
    .load("application-local", defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
    .getConfig("skribenten")
    .getConfig("groups")