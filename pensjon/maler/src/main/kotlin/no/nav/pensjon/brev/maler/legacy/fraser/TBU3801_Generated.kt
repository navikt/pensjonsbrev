package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU3801_Generated(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
		paragraph {
			showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
				text (
					bokmal { + "Endringer i inntektene til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget " + quoted("uføretrygd") +" på $NAV_URL."  },
				)
			}.orShowIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()){
				text (
					bokmal { + "Endringer i inntekten din kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget " + quoted("uføretrygd") +" på $NAV_URL."},
				)
			}
		}
    }
}
        