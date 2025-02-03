package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.ToggleImpl

object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    val pl7231ForventetSvartid = ToggleImpl("pl_7231.foreventet_svartid")
    val pl7822EndretInntekt = ToggleImpl("pl_7822.endringer_ut_endret_pga_inntekt")
    val pl7914EndretInntektPilot = ToggleImpl("pl_7914.ut_endret_pga_inntekt_test_pilot")
}