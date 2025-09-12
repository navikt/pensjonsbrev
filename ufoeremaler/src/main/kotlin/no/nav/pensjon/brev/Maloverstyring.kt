package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder


fun isEnabled(kode: String) = when (kode) {
    Ufoerebrevkoder.Redigerbar.UT_AVSLAG_UFOERETRYGD_DEMO.kode() -> FeatureToggles.uforeAvslagDemo

    else -> null
}?.let { FeatureToggleSingleton.isEnabled(it.toggle) } ?: true