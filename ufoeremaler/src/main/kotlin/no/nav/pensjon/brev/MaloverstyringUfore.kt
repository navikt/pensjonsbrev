package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder


fun isEnabled(kode: String) = when (kode) {
    Ufoerebrevkoder.Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_BEHANDLING.kode() -> FeatureToggles.uforeAvslag
    Ufoerebrevkoder.Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I1.kode() -> FeatureToggles.uforeAvslag
    Ufoerebrevkoder.Redigerbar.UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I2.kode() -> FeatureToggles.uforeAvslag

    else -> null
}?.let { FeatureToggleSingleton.isEnabled(it.toggle) } ?: true