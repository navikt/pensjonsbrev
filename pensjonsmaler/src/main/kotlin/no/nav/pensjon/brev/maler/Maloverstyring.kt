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