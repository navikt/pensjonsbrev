package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Toggle

enum class FeatureToggles : Toggle {
    brevMedFritekst,
    brevmal_ut_avslag;

    override fun key() = name;
}