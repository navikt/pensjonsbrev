package no.nav.pensjon.brev.ufore.api.model

import no.nav.pensjon.brev.api.model.maler.Brevkode.Automatisk
import no.nav.pensjon.brev.api.model.maler.Brevkode.Redigerbart

object Ufoerebrevkoder {
    enum class AutoBrev : Automatisk {

        ;
        override fun kode(): String = this.name
    }

    enum class Redigerbar : Redigerbart {
        UT_AVSLAG_UFOERETRYGD_DEMO
        ;

        override fun kode(): String = this.name
    }
}