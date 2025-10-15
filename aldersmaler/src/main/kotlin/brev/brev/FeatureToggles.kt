package no.nav.pensjon.brev.brev

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(
    private val key: String,
) {
    endringAvAlderspensjonSivilstand("endringAvAlderspensjonSivilstand"),
    endringAvAlderspensjonSivilstandVurderSaerskiltSats("endringAvAlderspensjonSivilstandVurderSaerskiltSats"),
    endringAvAlderspensjonSivilstandGarantitillegg("endringAvAlderspensjonSivilstandGarantitillegg"),
    ;

    val toggle = FeatureToggle(key)
}
