package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.redigerbar.OrienteringOmSaksbehandlingstidV2
import no.nav.pensjon.brev.maler.ufoereBrev.EndretUfoeretrygdPGAInntekt
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAutoV2

fun hentMuligOverstyrtMal(kode: String) = when {
    kode == Pesysbrevkoder.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID.kode() && FeatureToggleSingleton.isEnabled(FeatureToggles.pl7231ForventetSvartid) -> OrienteringOmSaksbehandlingstidV2
    kode == Pesysbrevkoder.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO.kode() && FeatureToggleSingleton.isEnabled(FeatureToggles.pl7231ForventetSvartid) -> VarselSaksbehandlingstidAutoV2
    kode == Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT.kode() && FeatureToggleSingleton.isEnabled(FeatureToggles.pl7914EndretInntektPilot) -> EndretUfoeretrygdPGAInntekt
    else -> null
}

fun isEnabled(kode: String) = when (kode) {
    Pesysbrevkoder.Redigerbar.PE_OVERSETTELSE_AV_DOKUMENTER.kode() -> FeatureToggles.brevMedFritekst
    Pesysbrevkoder.Redigerbar.UT_AVSLAG_UFOERETRYGD.kode() -> FeatureToggles.brevmalUtAvslag
    else -> null
}?.let { FeatureToggleSingleton.isEnabled(it) } ?: true