package no.nav.pensjon.brev

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FeatureToggleHandlerTest {

    @Test
    fun `skal ikke kunne reinitialisere oppsettet`() {
        val config = FeatureToggleConfig("a", "b", "http://localhost", "d")
        val builder = FeatureToggleHandler.Builder()
        builder.setConfig(config).build()
        assertThrows<IllegalStateException> {
            builder.setConfig(config).build()
        }
    }

    @Test
    fun `skal ikke kunne initialisere uten konfig`() {
        assertThrows<IllegalStateException> { FeatureToggleHandler.Builder().build() }
    }
}