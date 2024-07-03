

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU2278_Generated(
	val barnetilleggFelles_BTFBinnvilget: Expression<Boolean>,
	val barnetilleggSerkull_innvilget: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU2278NN, TBU2278]

		paragraph {
			text (
				Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden ",
				Nynorsk to "Det er viktig at du melder frå om inntektsendringar, slik at uføretrygda ",
			)

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
			showIf(barnetilleggSerkull_innvilget or barnetilleggFelles_BTFBinnvilget){
				text (
					Bokmal to "og barnetillegget ",
					Nynorsk to "og barnetillegget ",
				)
			}
			text (
				Bokmal to "blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL.",
				Nynorsk to "blir så riktig som mogleg. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» når du loggar deg inn på $NAV_URL.",
			)
		}
    }
}
        