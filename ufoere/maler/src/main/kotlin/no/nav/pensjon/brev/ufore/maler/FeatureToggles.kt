package no.nav.pensjon.brev.ufore.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    testmal("ut.testmal"),
    simulering("ut.simulering"),
    ;

    val toggle = FeatureToggle(key)
}