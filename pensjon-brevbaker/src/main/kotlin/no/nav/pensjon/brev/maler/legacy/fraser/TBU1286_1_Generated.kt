package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_serkull
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class TBU1286_1_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        paragraph {
            text(
                Bokmal to "Barnetillegget ",
                Nynorsk to "Barnetillegget ",
                English to "You will not receive child supplement ",
            )

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                textExpr(
                    Bokmal to "for ".expr() + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, ",
                    Nynorsk to "for ".expr() + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra sine, ",
                    English to "for the ".expr() + pe.ut_barnet_barna_serkull() + " who do not live together with both parents ",
                )
            }
            text(
                Bokmal to "blir ikke utbetalt fordi du ",
                Nynorsk to "blir ikkje utbetalt fordi du ",
                English to "because your total income ",
            )

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    Bokmal to "alene ",
                    Nynorsk to "åleine ",
                    English to "on its own ",
                )
            }
            textExpr(
                Bokmal to "har en samlet inntekt som er høyere enn ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",
                Nynorsk to "har ei samla inntekt som er høgare enn ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg. ",
                English to "is higher than NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". You will not receive child supplement because your income exceeds the income limit. ",
            )
        }
    }
}
        