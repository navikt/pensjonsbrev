package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text


data class TBU2278_Generated(val pe: Expression<PE>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU2278NN, TBU2278]

		paragraph {
			text (
				Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden ",
				Nynorsk to "Det er viktig at du melder frå om inntektsendringar, slik at uføretrygda ",
			)

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
			showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
				text (
					Bokmal to "og barnetillegget ",
					Nynorsk to "og barnetillegget ",
				)
			}
			text (
				Bokmal to "blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget ${quoted("uføretrygd")} når du logger deg inn på $NAV_URL.",
				Nynorsk to "blir så riktig som mogleg. Du kan enkelt melde frå om inntektsendringar under menyvalet ${quoted("uføretrygd")} når du loggar deg inn på $NAV_URL.",
			)
		}
    }
}
        