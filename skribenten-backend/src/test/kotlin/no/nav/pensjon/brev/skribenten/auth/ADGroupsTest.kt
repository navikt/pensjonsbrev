package no.nav.pensjon.brev.skribenten.auth


import no.nav.pensjon.brev.skribenten.initADGroups
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.memberProperties

class ADGroupsTest {

    init {
        initADGroups()
    }

    @Test
    fun `groups config loads with init`() {
        assertThat(ADGroups.pensjonUtland.id).isNotBlank()
        assertThat(ADGroups.pensjonSaksbehandler.id).isNotBlank()
        assertThat(ADGroups.fortroligAdresse.id).isNotBlank()
        assertThat(ADGroups.strengtFortroligAdresse.id).isNotBlank()
        assertThat(ADGroups.attestant.id).isNotBlank()
    }

    @Test
    fun `alle felter av typen ADGroup blir lastet inn`() {
        ADGroups::class.memberProperties.forEach { prop ->
            if (prop.returnType.classifier == ADGroup::class) {
                val group = prop.getter.call(ADGroups) as ADGroup
                assertThat(group.id).isNotBlank()
            }
        }
    }
}
