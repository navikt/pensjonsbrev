package no.nav.pensjon.brev.brev

import no.nav.pensjon.brev.api.model.FeatureToggle

enum class FeatureToggles(
    private val key: String,
) {
    endringAvAlderspensjonSivilstand("endringAvAlderspensjonSivilstand"),
    endringAvAlderspensjonSivilstandVurderSaerskiltSats("endringAvAlderspensjonSivilstandVurderSaerskiltSats"),
    endringAvAlderspensjonSivilstandGarantitillegg("endringAvAlderspensjonSivilstandGarantitillegg"),
    apAvslagGradsendringNormertPensjonsalder("ap_avslag_gradsendring_norm_redigerbar"),
    apAvslagGradsendringNormertPensjonsalderAP2016("ap_avslag_gradsendring_norm_redigerbar_ap2016"),
    apAvslagGradsendringNormertPensjonsalderFoerEttAar("ap_avslag_gradsendring_norm_foer_ett_aar_redigerbar"),
    apAvslagNormertPensjonsalder("ap_avslag_norm_redigerbar"),
    apAvslagNormertPensjonsalderAP2016("ap_avslag_norm_redigerbar_ap2016"),
    ;

    val toggle = FeatureToggle(key)
}
