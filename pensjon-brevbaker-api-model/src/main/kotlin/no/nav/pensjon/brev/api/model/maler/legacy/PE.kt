package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.PersonSak
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.Vedtaksbrev
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class PE(
    val vedtaksbrev: Vedtaksbrev,
    val pebrevkode: String,
    val personsak: PersonSak,
    val functions: ExstreamFunctions,
) : BrevbakerBrevdata {
    data class ExstreamFunctions(
        val avdodHarOpptjeningUTMedFoerstegangstjenesteOgIkkeOmsorg: Boolean,
        val avdodHarOpptjeningUTMedFoerstegangstjenesteOgOmsorg: Boolean,
        val avdodHarOpptjeningUTMedOmsorgOgIkkeFoerstegangstjeneste: Boolean,
        val fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_erstatning_innttap_erstoppgj: Boolean,
        val fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_etterslepsinnt_avslt_akt: Boolean,
        val harOpptjeningUTMedFoerstegangstjenesteOgIkkeOmsorg: Boolean,
        val harOpptjeningUTMedFoerstegangstjenesteOgOmsorg: Boolean,
        val harOpptjeningUTMedOmsorg: Boolean,
        val harOpptjeningUTMedOmsorgOgIkkeFoerstegangstjeneste: Boolean,
        val pe_sivilstand_ektefelle_partner_samboer_bormed_ut: String,
        val pe_sivilstand_ektefelle_partner_samboer_bormed_ut_en: String,
        val pe_sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner: String,
        val pe_sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall: String,
        val pe_ut_antallbarnserkullogfelles: Int,
        val pe_ut_btfbinntektbruktiavkortningminusbtfbfribelop: Kroner,
        val pe_ut_btsbinntektbruktiavkortningminusbtsbfribelop: Kroner,
        val pe_ut_etteroppgjorfratrekklistebrukeretterbetaling: Boolean,
        val pe_ut_fattnorgeplusfatta10netto_avdod: Int,
        val pe_ut_fattnorgeplusfattbilateral_avdod: Int,
        val pe_ut_fattnorgeplusfatteos_avdod: Int,
        val pe_ut_forstegangstjenesteikkenull: Boolean,
        val pe_ut_grunnikkereduksjon_lik_erstatning_innttap_ertstoppgj_finnes: Boolean,
        val pe_ut_inntekt_trukket_fra_personinntekt: Kroner,
        val pe_ut_inntektsgrense_faktisk_minus_60000: Kroner,
        val pe_ut_inntektslandtruehvorbruktlikfalse_avdod: Boolean,
        val pe_ut_inntektslandtruehvorbruktliktrue_avdod: Boolean,
        val pe_ut_nettoakk_pluss_nettorestar: Kroner,
        val pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt: Kroner,
        val pe_ut_opplyningerombergeningen_nettoperar: Kroner,
        val pe_ut_overskytende: Kroner,
        val pe_ut_sisteopptjeningarlikuforetidspunkt: Boolean,
        val pe_ut_sum_fattnorge_fatt_a10_netto: Int,
        val pe_ut_sum_fattnorge_fattbilateral: Int,
        val pe_ut_sum_fattnorge_fatteos: Int,
        val pe_ut_sum_fattnorge_framtidigttnorge_div_12: Int,
        val pe_ut_sum_fattnorge_framtidigttnorge_div_12_avdod: Int,
        val pe_ut_vilfylle67ivirkningfomar: Boolean,
        val pe_ut_vilkargjelderpersonalder: Int,
        val pe_ut_virkningfomar: Int,
        val pe_ut_virkningstidpunktarminus1ar: Int,
        val pe_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu_x_08: Kroner,
        val pe_ut_kravlinjekode_vedtakresultat_forekomst_bt_innv: Int,
        val harOpptjeningUTMedOpptjeningBruktAaretFoerOgFoerstegangstjeneste: Boolean,
        val foedselsdatoTilBarnTilleggErInnvilgetFor: List<LocalDate>,
        )
}
