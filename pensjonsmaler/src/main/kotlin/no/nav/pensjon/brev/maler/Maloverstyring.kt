package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.redigerbar.OrienteringOmSaksbehandlingstidV2
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAutoV2

fun hentMuligOverstyrtMal(kode: String) = when (kode) {
    Pesysbrevkoder.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID.kode() if FeatureToggleSingleton.isEnabled(
        FeatureToggles.pl7231ForventetSvartid.toggle
    ) -> OrienteringOmSaksbehandlingstidV2
    Pesysbrevkoder.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO.kode() if FeatureToggleSingleton.isEnabled(
        FeatureToggles.pl7231ForventetSvartid.toggle
    ) -> VarselSaksbehandlingstidAutoV2
    else -> null
}