package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.Brevkode.Automatisk
import no.nav.pensjon.brev.api.model.maler.Brevkode.Redigerbart

object Ufoerebrevkoder {
    enum class AutoBrev : Automatisk {

        ;
        override fun kode(): String = this.name
    }

    enum class Redigerbar : Redigerbart {
        ;

        override fun kode(): String = this.name
    }
}