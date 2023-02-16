package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening.KombinereUfoeretrygdMedInntekt
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object Kompensasjonsgrad {

    /* TBU056V - skal med ved innvilgelse, økning, endring
IF  ((brevkode.isOneOf(PE_UT_04_102, PE_UT_04_116, PE_UT_04_101, PE_UT_04_114, PE_UT_04_300, PE_UT_14_300, PE_UT_04_500)
OR (KravarsakType = endret_inntekt AND <BelopGammelUT> <> <BelopNyUT>)
)
AND
<KravArsakType> <> soknad_bt)
INCLUDE
    */
    data class KompenasjonsgradenDin(
        val XXX: Expression<Boolean>,
        val harFullUfoeregrad: Expression<Boolean>,
        val harDelvisUfoeregrad: Expression<Boolean>,
        val inntektFoerUfoere: Expression<Kroner>,
        val oppjustertInntektFoerUfoere: Expression<Kroner>,
        val ugradertBruttoPerAr: Expression<Kroner>,
        val ufoeregrad: Expression<Int>,
        val kompensasjonsgrad: Expression<Double>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(XXX) {
                title1 {
                    text(
                        Bokmal to "Slik har vi fastsatt kompensasjonsgraden din",
                        Nynorsk to "Slik har vi fastsett kompensasjonsgraden din",
                        English to "This is your degree of compensation"
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi fastsetter kompensasjonsgraden ved å sammenligne det du ".expr() +
                                ifElse(harFullUfoeregrad, ifTrue = "har rett til", ifFalse = "ville hatt rett til") +
                                " i 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Kompensasjonsgraden brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen.".expr(),
                        Nynorsk to "Vi fastset kompensasjonsgraden ved å samanlikne det du ".expr() +
                                ifElse(harFullUfoeregrad, ifTrue = "har rett til", ifFalse = "ville hatt rett til") +
                                " i 100 prosent uføretrygd, med den oppjusterte inntekta di før du blei ufør. Kompensasjonsgraden blir brukt til å berekne kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa.".expr(),
                        English to "Your degree of compensation is established by comparing what you ".expr() +
                                ifElse(harFullUfoeregrad, ifTrue = "are entitled to with a degree of disability of", ifFalse = "would have been entitled to had your degree of disability been") +
                                " 100 percent, and your recalculated income prior to your disability. The degree of compensation is used to calculate how much your disability benefit will be reduced if your income exceeds the income limit.".expr()
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Inntekten din før du ble ufør er fastsatt til ".expr() + inntektFoerUfoere.format() + " kroner. For å kunne fastsette kompensasjonsgraden din, må denne inntekten oppjusteres til dagens verdi. Oppjustert til dagens verdi tilsvarer dette en inntekt på ".expr() + oppjustertInntektFoerUfoere.format() + " kroner.".expr(),
                        Nynorsk to "Inntekta di før du blei ufør er fastsett til ".expr() + inntektFoerUfoere.format() + " kroner. For å kunne fastsetje kompensasjonsgraden din, må inntekta oppjusterast til dagens verdi. Oppjustert til dagens verdi utgjer dette ei inntekt på ".expr() + oppjustertInntektFoerUfoere.format() + " kroner.".expr(),
                        English to "".expr()
                    )
                }
                showIf(harFullUfoeregrad) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du har rett til 100 prosent uføretrygd, som utgjør <UgradertBruttoPerAr> kroner per år.".expr(),
                            Nynorsk to "Du har rett til 100 prosent uføretrygd, som utgjer <PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr> kroner per år.".expr(),
                            English to "".expr(),
                        )
                    }
                }
                showIf(harDelvisUfoeregrad) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du har rett til ".expr() + ufoeregrad.format() + " prosent uføretrygd. Regnet om til 100 prosent uføretrygd, utgjør dette ".expr() + ugradertBruttoPerAr.format() + " kroner per år.".expr(),
                            Nynorsk to "Du har rett til ".expr() + ufoeregrad.format() + " prosent uføretrygd. Rekna om til 100 prosent uføretrygd, utgjer dette ".expr() + ugradertBruttoPerAr.format() + " kroner per år.".expr(),
                            English to "You are entitled to <PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad>".expr()
                        )
                    }
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi beregner kompensasjonsgraden din slik: ".expr() + ugradertBruttoPerAr.format() + " / ".expr() + oppjustertInntektFoerUfoere.format() + " * 100 = ".expr() + kompensasjonsgrad.format() + " prosent.".expr(),
                        Nynorsk to "Vi bereknar kompensasjonsgraden din slik: ".expr() + ugradertBruttoPerAr.format() + " / ".expr() + oppjustertInntektFoerUfoere.format() + " * 100 = ".expr() + kompensasjonsgrad.format() + " prosent.".expr(),
                        English to  "We have calculated your degree of compensation as follows: ".expr() + ugradertBruttoPerAr.format() + " / ".expr() + oppjustertInntektFoerUfoere.format() + " * 100 = ".expr() + kompensasjonsgrad.format() + " percent.".expr(),
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Kompensasjonsgraden skal ved beregningen ikke settes høyere enn 70 prosent. Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
                        Nynorsk to "Kompensasjonsgraden skal ikkje setjast høgare enn 70 prosent i berekninga. Dersom uføretrygda di blir endra i løpet av eit kalenderår, vil vi bruke ein gjennomsnittleg kompensasjonsgrad i berekninga.",
                        English to "Your degree of compensation will not be set higher than 70 percent. If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation."
                    )
                }
            }
        }
    }
}