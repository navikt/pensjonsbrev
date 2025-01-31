package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.ToggleName

enum class FeatureToggles : ToggleName {
    brevMedFritekst,
    brevmal_ut_avslag;

    override fun key() = name;
}