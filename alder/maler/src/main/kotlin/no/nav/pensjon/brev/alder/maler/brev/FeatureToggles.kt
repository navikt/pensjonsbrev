package no.nav.pensjon.brev.alder.maler.brev

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(
    private val key: String,
) {
    omregningAlderUfore2016("omregningAlderUfore2016");

    val toggle = FeatureToggle(key)
}
