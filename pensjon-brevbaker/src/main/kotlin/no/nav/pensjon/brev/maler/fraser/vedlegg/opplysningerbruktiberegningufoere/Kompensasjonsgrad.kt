package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening.KombinereUfoeretrygdMedInntekt
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
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
        val inntektFoerUfoere: Expression<Boolean>,
        val oppjustertInntektFoerUfoere: Expression<Boolean>,
        val ugradertBruttoPerAr: Expression<Boolean>,
        val ufoeregrad: Expression<Int>,
        val kompensasjonsgrad: Expression<Double>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(XXX) {
                title1 {
                    text(
                        Bokmal to "Slik har vi fastsatt kompensasjonsgraden din ",
                        Nynorsk to "",
                        English to ""
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi fastsetter kompensasjonsgraden ved å sammenligne det du ".expr() +
                                ifElse(harFullUfoeregrad, ifTrue = "har rett til", ifFalse = "ville hatt rett til") +
                                " i 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Kompensasjonsgraden brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Inntekten din før du ble ufør er fastsatt til <IFUInntekt> kroner. For å kunne fastsette kompensasjonsgraden din, må denne inntekten oppjusteres til dagens verdi. Oppjustert til dagens verdi tilsvarer dette en inntekt på <Oifu> kroner.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
                showIf(harFullUfoeregrad) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du har rett til 100 prosent uføretrygd, som utgjør <UgradertBruttoPerAr> kroner per år.".expr(),
                            Nynorsk to "".expr(),
                            English to "".expr(),
                        )
                    }
                }
                showIf(harDelvisUfoeregrad) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du har rett til <uforegrad> prosent uføretrygd. Regnet om til 100 prosent uføretrygd, utgjør dette <UgradertBruttoPerAr> kroner per år.".expr(),
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                    }
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi beregner kompensasjonsgraden din slik:(<UgradertBruttoPerAr> / <Oifu>)*100 = <Kompensasjonsgrad> prosent.\n".expr(),
                        Nynorsk to "".expr(),
                        English to  "".expr()
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Kompensasjonsgraden skal ved beregningen ikke settes høyere enn 70 prosent. Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }
        }
    }
}