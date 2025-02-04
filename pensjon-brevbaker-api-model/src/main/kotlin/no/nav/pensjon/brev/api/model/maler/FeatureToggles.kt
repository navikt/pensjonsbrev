package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.FeatureToggle


enum class FeatureToggles : FeatureToggle {
    brevMedFritekst,
    brevmal_ut_avslag;

    override fun key() = name;
}