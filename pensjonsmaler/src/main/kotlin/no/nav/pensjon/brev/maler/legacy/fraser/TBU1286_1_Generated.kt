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
                bokmal { + "Barnetillegget " },
                nynorsk { + "Barnetillegget " },
                english { + "You will not receive child supplement " },
            )

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    bokmal { + "for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, " },
                    nynorsk { + "for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra sine, " },
                    english { + "for the " + pe.ut_barnet_barna_serkull() + " who do not live together with both parents " },
                )
            }
            text(
                bokmal { + "blir ikke utbetalt fordi du " },
                nynorsk { + "blir ikkje utbetalt fordi du " },
                english { + "because your total income " },
            )

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    bokmal { + "alene " },
                    nynorsk { + "åleine " },
                    english { + "on its own " },
                )
            }
            text(
                bokmal { + "har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg." },
                nynorsk { + "har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg. " },
                english { + "is higher than " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". You will not receive child supplement because your income exceeds the income limit. " },
            )
        }
    }
}
        