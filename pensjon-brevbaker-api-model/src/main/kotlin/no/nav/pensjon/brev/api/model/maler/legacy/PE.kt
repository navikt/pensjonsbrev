package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.Vedtaksbrev
import no.nav.pensjon.brevbaker.api.model.Kroner

data class PE(
    val Vedtaksbrev: Vedtaksbrev,
    val functions: ExstreamFunctions,
) : BrevbakerBrevdata {
    data class ExstreamFunctions(
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT: String,
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner: String,
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall: String,
        val PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt: Kroner,
        val PE_UT_OpplyningerOmBergeningen_NettoPerAr: Kroner,
        val PE_UT_Overskytende: Kroner,
        val PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12: Kroner,
        val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Kroner,
        val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Kroner,
        val PE_UT_NettoAkk_pluss_NettoRestAr: Kroner,
        val PE_UT_VirkningstidpunktArMinus1Ar: Int,
    )
}
