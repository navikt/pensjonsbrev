package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_felles
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_felles_en_entall_flertall
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text


data class TBU1286_2_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        paragraph {
            text(
                bokmal { + "Barnetillegget" },
                nynorsk { + "Barnetillegget " },
                english { + "You will not receive child supplement " },
            )

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) {
                text(
                    bokmal { + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre," },
                    nynorsk { + "for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, " },
                    english { + "for the " + pe.ut_barnet_barna_felles() + " who live" + pe.ut_barnet_barna_felles_en_entall_flertall() + " together with both parents " },
                )
            }
            text(
                bokmal { + " blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg." },
                nynorsk { + "blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },
                english { + "because your total income on its own is higher than " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". You will not receive child supplement because your combined incomes exceed the income limit." },
            )
        }
    }
}
        