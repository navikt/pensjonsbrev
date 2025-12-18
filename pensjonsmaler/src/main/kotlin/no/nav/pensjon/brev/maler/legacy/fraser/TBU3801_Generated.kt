package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU3801_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		paragraph {
			text (
				bokmal { + "Endringer i " },
				nynorsk { + "Endringar i " },
				english { + "Changes in your " },
			)

			//PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
			showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
				text (
					bokmal { + "inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " din " },
					nynorsk { + "inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
					english { + "and your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + "'s " },
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
			showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
				text (
					bokmal { + "inntekten din " },
					nynorsk { + "inntekta di " },
					english { + "" },
				)
			}
			text (
				bokmal { + "kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget " + quoted("uføretrygd") +" på $NAV_URL." },
				nynorsk { + "kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet " + quoted("uføretrygd") +" på $NAV_URL." },
				english { + "income may affect your child supplement. You can easily report income changes under the menu option " + quoted("disability benefit") +" at $NAV_URL." },
			)
		}
    }
}
