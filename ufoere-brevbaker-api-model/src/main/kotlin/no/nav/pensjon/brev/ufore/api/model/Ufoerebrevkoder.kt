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
        UT_AVSLAG_ALDER,
        UT_AVSLAG_MANGLENDE_DOK,
        UT_AVSLAG_INNTEKTSEVNE_50,
        UT_AVSLAG_INNTEKTSEVNE_40,
        UT_AVSLAG_INNTEKTSEVNE_30,
        UT_AVSLAG_UNG_UFOR_36,
        UT_AVSLAG_UNG_UFOR_26,
        UT_AVSLAG_YRKESSKADE_GODKJENT,
        UT_AVSLAG_YRKESSKADE_IKKE_GODKJENT,
        UT_AVSLAG_IFU_IKKE_VARIG,
        UT_AVSLAG_IFU_OKT_STILLING,
        UT_AVSLAG_OKT_GRAD_INNTEKTSEVNE,
        ;

        override fun kode(): String = this.name
    }
}