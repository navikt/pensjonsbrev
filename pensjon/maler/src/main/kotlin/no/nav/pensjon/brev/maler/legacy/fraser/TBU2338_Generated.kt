package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text


data class TBU2338_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

		paragraph {
			text (
				bokmal { + "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " },
				nynorsk { + "Inntekta di har noko å seie for kva du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. " },
				english { + "Your income affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. " },
			)

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0))){
				text (
					bokmal { + "Denne grensen kaller vi for fribeløp. " },
					nynorsk { + "Denne grensa kallar vi for fribeløp. " },
					english { + "We call this limit the exemption amount. " },
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> "enslig" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt <> "enslig separert") THEN      INCLUDE ENDIF
			// Bokmål var egentlig: sivilstand_ektefelle_partner_samboer_bormed_ut_alle_spraak_entall
			showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("enslig") and pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().notEqualTo("enslig separert"))){
				text (
					bokmal { + "Inntekten til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " din har ikke betydning for størrelsen på barnetillegget." },
					nynorsk { + "Inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din har ikkje noko å seie for storleiken på barnetillegget. " },
					english { + "The income of your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + " does not affect the size of your child supplement." },
				)
			}
		}
    }
}
        