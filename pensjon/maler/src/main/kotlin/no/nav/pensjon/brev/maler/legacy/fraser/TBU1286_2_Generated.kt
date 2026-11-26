package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_felles
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text


data class TBU1286_2_Generated(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        paragraph {
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) {
                text(
                    bokmal { + "Barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg. "},
                    nynorsk { + "Barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg. "},
                )
            }.orShow {
                text(
                    bokmal { + "Barnetillegget blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg."},
                    nynorsk { + "Barnetillegget blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg."},
                )
            }
        }
    }
}
        