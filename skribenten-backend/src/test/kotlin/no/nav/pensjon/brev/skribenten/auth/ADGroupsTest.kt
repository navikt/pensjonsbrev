package no.nav.pensjon.brev.skribenten.auth

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isNullOrBlank
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions.defaults
import com.typesafe.config.ConfigResolveOptions
import org.junit.Test

class ADGroupsTest {

    private val groupsConfig = ConfigFactory
        .load("application-local", defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
        .getConfig("skribenten")
        .getConfig("groups")

    @Test
    fun `groups config loads with init`() {
        ADGroups.init(groupsConfig)
        assertThat(ADGroups.pensjonUtland.id, !isNullOrBlank)
        assertThat(ADGroups.pensjonSaksbehandler.id, !isNullOrBlank)
        assertThat(ADGroups.fortroligAdresse.id, !isNullOrBlank)
        assertThat(ADGroups.strengtFortroligAdresse.id, !isNullOrBlank)
        assertThat(ADGroups.strengtFortroligUtland.id, !isNullOrBlank)
    }

}