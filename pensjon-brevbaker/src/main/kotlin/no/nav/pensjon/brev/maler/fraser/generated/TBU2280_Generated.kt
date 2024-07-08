

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU2280_Generated(
	val barnetilleggFelles_innvilget: Expression<Boolean>,
	val barnetilleggSerkull_innvilget: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU2280NN, TBU2280, TBU2280EN]

		paragraph {
			text (
				Bokmal to "Vi vil foreta et etteroppgjør hvis du har fått utbetalt for mye eller for lite uføretrygd ",
				Nynorsk to "Vi gjer eit etteroppgjer dersom du har fått utbetalt for mykje eller for lite uføretrygd ",
			)

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
			showIf(barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget){
				text (
					Bokmal to "og barnetillegg ",
					Nynorsk to "og barnetillegg ",
				)
			}
			text (
				Bokmal to "i løpet av året. Dette gjør vi når likningen fra Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd",
				Nynorsk to "i løpet av året. Dette gjer vi når likninga frå Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd",
			)

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
			showIf(barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget){
				text (
					Bokmal to " og barnetillegg",
					Nynorsk to " og barnetillegg",
				)
			}
			text (
				Bokmal to ", vil vi utbetale dette beløpet til deg. Hvis du har fått utbetalt for mye i uføretrygd",
				Nynorsk to ", vil vi betale deg dette beløpet. Dersom du har fått utbetalt for mykje i uføretrygd",
			)

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
			showIf(barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget){
				text (
					Bokmal to " og barnetillegg",
					Nynorsk to " og barnetillegg",
				)
			}
			text (
				Bokmal to ", må du betale dette tilbake.",
				Nynorsk to ", må du betale dette tilbake.",
			)
		}
    }
}
        