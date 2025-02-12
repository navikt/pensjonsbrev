package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val toggleName: String) : FeatureToggle {
    BREV_MED_FRITEKST("brevMedFritekst"),
    BREVMAL_UT_AVSLAG("brevmal_ut_avslag"),
    ;

    override fun key() = toggleName
}
