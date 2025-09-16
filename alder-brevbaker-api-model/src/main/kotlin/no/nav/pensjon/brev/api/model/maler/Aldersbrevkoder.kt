package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.Brevkode.Automatisk
import no.nav.pensjon.brev.api.model.maler.Brevkode.Redigerbart

object Aldersbrevkoder {
    enum class AutoBrev : Automatisk {
        PE_AP_ENDRING_AV_ALDERSPENSJON_AUTO,
        INFO_FYLLER_67_AAR_SAERSKILT_SATS;
        override fun kode(): String = this.name
    }

    enum class Redigerbar : Redigerbart {
        ;

        override fun kode(): String = this.name
    }
}