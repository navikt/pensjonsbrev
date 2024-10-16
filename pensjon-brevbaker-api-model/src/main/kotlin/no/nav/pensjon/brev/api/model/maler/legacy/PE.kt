package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.PersonSak
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.Vedtaksbrev
import no.nav.pensjon.brevbaker.api.model.Kroner

data class PE(
    val vedtaksbrev: Vedtaksbrev,
    val pebrevkode: String,
    val personsak: PersonSak,
    val functions: ExstreamFunctions,
) : BrevbakerBrevdata {
    data class ExstreamFunctions(
        val pe_sivilstand_ektefelle_partner_samboer_bormed_ut: String,
        val pe_sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner: String,
        val pe_sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall: String,
        val pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt: Kroner,
        val pe_ut_opplyningerombergeningen_nettoperar: Kroner,
        val pe_ut_overskytende: Kroner,
        val pe_ut_sum_fattnorge_framtidigttnorge_div_12: Int,
        val pe_ut_nettoakk_pluss_nettorestar: Kroner,
        val pe_ut_virkningstidpunktarminus1ar: Int,
        val pe_ut_vilfylle67ivirkningfomar: Boolean,
        val pe_ut_virkningfomar: Int,
        val pe_sivilstand_ektefelle_partner_samboer_bormed_ut_en: String,
        val pe_ut_btsbinntektbruktiavkortningminusbtsbfribelop: Kroner,
        val pe_ut_btfbinntektbruktiavkortningminusbtfbfribelop: Kroner,
        val pe_ut_sum_fattnorge_fatteos: Int,
        val pe_ut_sum_fattnorge_fatt_a10_netto: Int,
        val pe_ut_sum_fattnorge_fattbilateral: Int,
        val pe_ut_antallbarnserkullogfelles: Int,
        val fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_etterslepsinnt_avslt_akt: Boolean,
        val fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_erstatning_innttap_erstoppgj: Boolean,
        val pe_ut_grunnikkereduksjon_lik_erstatning_innttap_ertstoppgj_finnes: Boolean,
        val pe_ut_etteroppgjorfratrekklistebrukeretterbetaling: Boolean,
        val pe_ut_inntekt_trukket_fra_personinntekt: Kroner,
    )
}
