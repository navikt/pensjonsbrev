package no.nav.pensjon.brev.ufore.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    feilutbetaling("ut.tilbakekreving"),
    avslagMedlemskap("ut.avslagmedlemskap");

    val toggle = FeatureToggle(key)
}