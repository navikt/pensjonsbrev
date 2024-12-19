package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_felles
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_felles_en_entall_flertall
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_serkull
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_serkull_en_entall_flertall
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU2490_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

		paragraph {
			textExpr (
				Bokmal to "Barnetillegget for ".expr() + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntektene er over grensen for å få utbetalt barnetillegg.",
				Nynorsk to "Barnetillegget for ".expr() + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntektene er over grensa for å få utbetalt barnetillegg.",
				English to "You will not receive child supplement for the ".expr() + pe.ut_barnet_barna_felles() + " who live" + pe.ut_barnet_barna_felles_en_entall_flertall() + " together with both parents because your total income is higher than NOK " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". You will not receive child supplement for the " + pe.ut_barnet_barna_serkull() + " who do not live" + pe.ut_barnet_barna_serkull_en_entall_flertall() + " together with both parents because your income alone is higher than NOK " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". You will not receive child supplement because your income exceeds the income limit. ",
			)
		}
    }
}
