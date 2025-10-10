package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    feilutbetaling("ut.tilbakekreving");

    val toggle = FeatureToggle(key)
}