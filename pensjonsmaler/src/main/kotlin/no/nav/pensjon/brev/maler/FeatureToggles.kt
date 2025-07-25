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
    val apAvslagGradsendringNormertPensjonsalder = FeatureToggle("ap_avslag_gradsendring_norm_redigerbar")
    val apAvslagGradsendringNormertPensjonsalderAP2016 = FeatureToggle("ap_avslag_gradsendring_norm_redigerbar_ap2016")
    val apAvslagGradsendringNormertPensjonsalderFoerEttAar = FeatureToggle("ap_avslag_gradsendring_norm_foer_ett_aar_redigerbar")
    val apAvslagNormertPensjonsalder = FeatureToggle("ap_avslag_norm_redigerbar")
    val apAvslagNormertPensjonsalderAP2016 = FeatureToggle("ap_avslag_norm_redigerbar_ap2016")
    val informasjonOmGjenlevenderettigheter = FeatureToggle("informasjonOmGjenlevenderettigheter")
    val omsorgEgenManuell = FeatureToggle("omsorgEgenManuell")
    val varselRevurderingAvPensjon = FeatureToggle("varselRevideringAvPensjon")
    val varselTilbakekrevingAvFeilutbetaltBeloep = FeatureToggle("varselTilbakekrevingAvFeilutbetaltBeloep")
    val vedtakTilbakekrevingAvFeilutbetaltBeloep = FeatureToggle("vedtakTilbakekrevingAvFeilutbetaltBeloep")
    val vedtakEndringAvAlderspensjonGjenlevenderettigheter = FeatureToggle("vedtakEndringAvAlderspensjonGjenlevenderettigheter")
    val vedtakEndringAvAlderspensjonInstitusjonsopphold = FeatureToggle("vedtakEndringAvAlderspensjonInstitusjonsopphold")
    val vedtakEndringAvUttaksgrad = FeatureToggle("vedtakEndringAvUttaksgrad")
    val vedtakEndringAvUttaksgradStans = FeatureToggle("vedtakEndringAvUttaksgradStans")
    val endringAvAlderspensjonSivilstand = FeatureToggle("endringAvAlderspensjonSivilstand")
    val innvilgelseAvAlderspensjon = FeatureToggle("innvilgelseAvAlderspensjon")
    val innvilgelseAvAlderspensjonTrygdeavtale = FeatureToggle("innvilgelseAvAlderspensjonTrygdeavtale")
    val vedtakOmFjerningAvOmsorgspoeng = FeatureToggle("vedtakOmFjerningAvOmsorgspoeng")
    val vedtakEndringOpptjeningEndret = FeatureToggle("vedtakEndringOpptjeningEndret")
    val vedtakStansFlyttingMellomLand = FeatureToggle("vedtakStansFlyttingMellomLand")
    val avslagForLiteTrygdetidAP = FeatureToggle("avslagForLiteTrygdetidAP")
}