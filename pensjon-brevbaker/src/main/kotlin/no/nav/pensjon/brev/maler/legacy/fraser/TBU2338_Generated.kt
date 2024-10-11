package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU2338_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2338NN, TBU2338, TBU2338EN]

		paragraph {
			text (
				Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. ",
				Nynorsk to "Inntekta di har noko å seie for kva du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. ",
				English to "Your income affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. ",
			)

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0))){
				text (
					Bokmal to "Denne grensen kaller vi for fribeløp. ",
					Nynorsk to "Denne grensa kallar vi for fribeløp. ",
					English to "We call this limit the exemption amount. ",
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> "enslig" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> "enslig separert") THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("enslig") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("enslig separert"))){
				textExpr (
					Bokmal to "Inntekten til ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " din har ikke betydning for størrelsen på barnetillegget.",
					Nynorsk to "Inntekta til ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din har ikkje noko å seie for storleiken på barnetillegget. ",
					English to "The income of your ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + " does not affect the size of your child supplement.",
				)
			}
		}
    }
}
        