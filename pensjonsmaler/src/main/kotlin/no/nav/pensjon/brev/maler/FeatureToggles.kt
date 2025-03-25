package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    val pl7231ForventetSvartid = FeatureToggle("pl_7231.foreventet_svartid")
    val pl7822EndretInntekt = FeatureToggle("pl_7822.endringer_ut_endret_pga_inntekt")
    val pl7914EndretInntektPilot = FeatureToggle("pl_7914.ut_endret_pga_inntekt_test_pilot")
    val brevMedFritekst = FeatureToggle("brevMedFritekst")
    val brevmalUtAvslag = FeatureToggle("brevmalUtAvslag")
    val innhentingAvInformasjonFraBruker = FeatureToggle("innhentingAvInformasjonFraBruker")
    val apAvslagNormertPensjonsalder = FeatureToggle("pensjonsbrev.brev.ap_avslag_norm_auto")
    val apAvslagGradsendringNormertPensjonsalder = FeatureToggle("ap_avslag_gradsendring_norm_auto")
    val varselRevurderingAvPensjon = FeatureToggle("varselRevideringAvPensjon")
}