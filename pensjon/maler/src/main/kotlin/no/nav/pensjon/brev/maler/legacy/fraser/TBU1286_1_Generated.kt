package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_serkull
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class TBU1286_1_Generated(
    val pe: Expression<PEgruppe10>,
    val uniqueness: String? = null,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        paragraph(uniqueness = uniqueness) {
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    bokmal { + "Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg. "},
                    nynorsk { + "Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra sine, blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg. "},
                )
            }.orShow {
                text(
                    bokmal { +"Barnetillegget blir ikke utbetalt fordi du har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg. "},
                    nynorsk { +"Barnetillegget blir ikkje utbetalt fordi du har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg. "},
                )
            }
        }
    }
}
        