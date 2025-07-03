package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.redigerbar.OrienteringOmSaksbehandlingstidV2
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAutoV2

fun hentMuligOverstyrtMal(kode: String) = when {
    kode == Pesysbrevkoder.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID.kode() && FeatureToggleSingleton.isEnabled(FeatureToggles.pl7231ForventetSvartid) -> OrienteringOmSaksbehandlingstidV2
    kode == Pesysbrevkoder.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO.kode() && FeatureToggleSingleton.isEnabled(FeatureToggles.pl7231ForventetSvartid) -> VarselSaksbehandlingstidAutoV2
    else -> null
}

fun isEnabled(kode: String) = when (kode) {
    Pesysbrevkoder.Redigerbar.PE_OVERSETTELSE_AV_DOKUMENTER.kode() -> FeatureToggles.brevMedFritekst
    Pesysbrevkoder.Redigerbar.UT_AVSLAG_UFOERETRYGD.kode() -> FeatureToggles.brevmalUtAvslag
    Pesysbrevkoder.Redigerbar.PE_AP_INNHENTING_INFORMASJON_FRA_BRUKER.kode() -> FeatureToggles.innhentingAvInformasjonFraBruker
    Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER.kode() -> FeatureToggles.apAvslagGradsendringNormertPensjonsalder
    Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_AP2016.kode() -> FeatureToggles.apAvslagGradsendringNormertPensjonsalderAP2016
    Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_ETT_AAR.kode() -> FeatureToggles.apAvslagGradsendringNormertPensjonsalderFoerEttAar
    Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER.kode() -> FeatureToggles.apAvslagNormertPensjonsalder
    Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER_AP2016.kode() -> FeatureToggles.apAvslagNormertPensjonsalderAP2016
    Pesysbrevkoder.Redigerbar.PE_VARSEL_REVURDERING_AV_PENSJON.kode() -> FeatureToggles.varselRevurderingAvPensjon
    Pesysbrevkoder.Redigerbar.PE_VARSEL_OM_TILBAKEKREVING_FEILUTBETALT_BELOEP.kode() -> FeatureToggles.varselTilbakekrevingAvFeilutbetaltBeloep
    Pesysbrevkoder.Redigerbar.PE_TILBAKEKREVING_AV_FEILUTBETALT_BELOEP.kode() -> FeatureToggles.vedtakTilbakekrevingAvFeilutbetaltBeloep
    Pesysbrevkoder.Redigerbar.PE_OMSORG_EGEN_MANUELL.kode() -> FeatureToggles.omsorgEgenManuell
    Pesysbrevkoder.Redigerbar.PE_INFORMASJON_OM_GJENLEVENDERETTIGHETER.kode() -> FeatureToggles.informasjonOmGjenlevenderettigheter
    Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_GJENLEVENDERETT.kode() -> FeatureToggles.vedtakEndringAvAlderspensjonGjenlevenderettigheter
    Pesysbrevkoder.Redigerbar.PE_AP_ENDRET_UTTAKSGRAD.kode() -> FeatureToggles.vedtakEndringAvUttaksgrad
    Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_SIVILSTAND.kode() -> FeatureToggles.endringAvAlderspensjonSivilstand
    Pesysbrevkoder.Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_BRUKER_ELLER_VERGE.kode(),
        Pesysbrevkoder.Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_IKKE_BRUKER_VERGE.kode() -> FeatureToggles.vedtakEndringAvUttaksgradStans
    Pesysbrevkoder.Redigerbar.PE_VEDTAK_OM_ENDRING.kode() -> FeatureToggles.vedtakOmEndring
    else -> null
}?.let { FeatureToggleSingleton.isEnabled(it) } ?: true