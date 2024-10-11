package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_felles
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_felles_en_entall_flertall
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU2339_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2339NN, TBU2339, TBU2339EN]

		paragraph {
			textExpr (
				Bokmal to "Inntekten til deg og ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. ",
				Nynorsk to "Inntekta til deg og ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert. ",
				English to "The incomes of you and your ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + " affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. ",
			)

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0))){
				text (
					Bokmal to "Denne grensen kaller vi for fribeløp. ",
					Nynorsk to "Denne grensa kallar vi for fribeløp. ",
					English to "We call this limit the exemption amount. ",
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())){
				textExpr (
					Bokmal to "Inntekten til  ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " din har kun betydning for størrelsen på barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor sammen med begge sine foreldre.",
					Nynorsk to "Inntekta til ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din har berre betydning for storleiken på barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. ",
					English to "The income of your ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + " only affects the size of the child supplement for the " + pe.ut_barnet_barna_felles() + " who live" + pe.ut_barnet_barna_felles_en_entall_flertall() + " together with both parents. ",
				)
			}
		}
    }
}
        