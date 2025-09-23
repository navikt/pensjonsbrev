package no.nav.pensjon.brev.ufore.api.model

import no.nav.pensjon.brev.api.model.maler.Brevkode.Automatisk
import no.nav.pensjon.brev.api.model.maler.Brevkode.Redigerbart

object Ufoerebrevkoder {
    enum class AutoBrev : Automatisk {

        ;
        override fun kode(): String = this.name
    }

    enum class Redigerbar : Redigerbart {
        UT_AVSLAG_HENSIKTSMESSIG_BEHANDLING,
        UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I1,
        UT_AVSLAG_HENSIKTSMESSIG_ARB_TILTAK_I2,
        UT_AVSLAG_SYKDOM,
        ;

        override fun kode(): String = this.name
    }
}