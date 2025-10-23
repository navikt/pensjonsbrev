package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(private val key: String) {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    pl7231ForventetSvartid("pl_7231.foreventet_svartid"),
    oversettelseAvDokumenter("oversettelseAvDokumenter"),
    brevmalUtAvslag("brevmalUtAvslag"),
    apAvslagGjenlevenderett("apAvslagGjenlevenderett"),
    apAvslagGradsendringNormertPensjonsalder("ap_avslag_gradsendring_norm_redigerbar"),
    apAvslagGradsendringNormertPensjonsalderAP2016("ap_avslag_gradsendring_norm_redigerbar_ap2016"),
    apAvslagGradsendringNormertPensjonsalderFoerEttAar("ap_avslag_gradsendring_norm_foer_ett_aar_redigerbar"),
    apAvslagNormertPensjonsalder("ap_avslag_norm_redigerbar"),
    apAvslagNormertPensjonsalderAP2016("ap_avslag_norm_redigerbar_ap2016"),
    informasjonOmGjenlevenderettigheter("informasjonOmGjenlevenderettigheter"),
    omsorgEgenManuell("omsorgEgenManuell"),
    vedtakTilbakekrevingAvFeilutbetaltBeloep("vedtakTilbakekrevingAvFeilutbetaltBeloep"),
    vedtakEndringAvAlderspensjonGjenlevenderettigheter("vedtakEndringAvAlderspensjonGjenlevenderettigheter"),
    vedtakEndringAvAlderspensjonInstitusjonsopphold("vedtakEndringAvAlderspensjonInstitusjonsopphold"),
    vedtakEndringAvUttaksgrad("vedtakEndringAvUttaksgrad"),
    vedtakEndringAvUttaksgradStans("vedtakEndringAvUttaksgradStans"),
    innvilgelseAvAlderspensjon("innvilgelseAvAlderspensjon"),
    innvilgelseAvAlderspensjonTrygdeavtale("innvilgelseAvAlderspensjonTrygdeavtale"),
    orienteringOmForlengetSaksbehandlingstid("orienteringOmForlengetSaksbehandlingstid"),
    samletMeldingOmPensjonsvedtak("samletMeldingOmPensjonsvedtak"),
    vedtakOmFjerningAvOmsorgspoeng("vedtakOmFjerningAvOmsorgspoeng"),
    vedtakEndringOpptjeningEndret("vedtakEndringOpptjeningEndret"),
    vedtakEndringVedFlyttingMellomLand("vedtakEndringVedFlyttingMellomLand"),
    brukertestbrev2025("brukertestbrev2025"),
    vedtakStansFlyttingMellomLand("vedtakStansFlyttingMellomLand"),
    avslagForLiteTrygdetidAP("avslagForLiteTrygdetidAP"),
    omregningAlderUfore2016("omregningAlderUfore2016");

    val toggle = FeatureToggle(key)
}