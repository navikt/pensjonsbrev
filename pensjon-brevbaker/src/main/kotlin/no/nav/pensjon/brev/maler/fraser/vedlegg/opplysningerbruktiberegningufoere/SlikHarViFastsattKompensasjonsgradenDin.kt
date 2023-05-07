package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereSelectors.inntektFoerUfoer
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereSelectors.oppjustertInntektFoerUfoer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdSelectors.fullUfoeretrygdPerAar
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdSelectors.ufoeregrad
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

// Brevkode <> PE_UT_04_101, PE_UT_04_102, PE_UT_04_114, PE_UT_04_116, PE_UT_04_300, PE_UT_04_500, PE_UT_14_300

// TBU056V

data class SlikHarViFastsattKompensasjonsgradenDin(
    val inntektFoerUfoere: Expression<OpplysningerBruktIBeregningUTDto.InntektFoerUfoere>,
    val inntektsAvkorting: Expression<OpplysningerBruktIBeregningUTDto.InntektsAvkorting>,
    val kravAarsakType: Expression<KravAarsakType>,
    val ufoeretrygd: Expression<OpplysningerBruktIBeregningUTDto.Ufoeretrygd>,
    val ufoeretrygdOrdinaer: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val harDelvisUfoeregrad = ufoeretrygd.ufoeregrad.greaterThan(0) and ufoeretrygd.ufoeregrad.lessThan(100)
        val harFullUfoeregrad = ufoeretrygd.ufoeregrad.equalTo(100)
        val inntektFoerUfoer = inntektFoerUfoere.inntektFoerUfoer.format()
        val oppjustertInntektFoerUfoer = inntektFoerUfoere.oppjustertInntektFoerUfoer.format()
        val fullUfoeretrygdPerAar = ufoeretrygd.fullUfoeretrygdPerAar.format()
        val ufoeregrad = ufoeretrygd.ufoeregrad.format()
        val kompensasjonsgrad = ufoeretrygd.kompensasjonsgrad.format()


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
                        ifElse(
                            harFullUfoeregrad,
                            ifTrue = "har rett til",
                            ifFalse = "ville hatt rett til"
                        ) + " i 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør".expr(),
                Nynorsk to "Vi fastset kompensasjonsgraden ved å samanlikne det du ".expr() +
                        ifElse(
                            harFullUfoeregrad,
                            ifTrue = "har rett til",
                            ifFalse = "ville hatt rett til"
                        ) + " i 100 prosent uføretrygd, med den oppjusterte inntekta di før du blei ufør.".expr(),
                English to "Your degree of compensation is established by comparing what you ".expr() +
                        ifElse(
                            harFullUfoeregrad,
                            ifTrue = "are entitled to with a degree of disability of",
                            ifFalse = "would have been entitled to had your degree of disability been"
                        ) + " 100 percent, and your recalculated income prior to your disability.".expr()
            )
            text(
                Bokmal to "Kompensasjonsgraden brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen.",
                Nynorsk to "Kompensasjonsgraden blir brukt til å berekne kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa.",
                English to "The degree of compensation is used to calculate how much your disability benefit will be reduced if your income exceeds the income limit."
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Inntekten din før du ble ufør er fastsatt til ".expr() + inntektFoerUfoer + " kroner. For å kunne fastsette kompensasjonsgraden din, må denne inntekten oppjusteres til dagens verdi. Oppjustert til dagens verdi tilsvarer dette en inntekt på ".expr() + oppjustertInntektFoerUfoer + " kroner.".expr(),
                Nynorsk to "Inntekta di før du blei ufør er fastsett til ".expr() + inntektFoerUfoer + " kroner. For å kunne fastsetje kompensasjonsgraden din, må inntekta oppjusterast til dagens verdi. Oppjustert til dagens verdi utgjer dette ei inntekt på ".expr() + oppjustertInntektFoerUfoer + " kroner.".expr(),
                English to "Your income before you became disabled has been determined to be NOK ".expr() + inntektFoerUfoer + ". To establish your degree of compensation this is adjusted to today’s value and is equivalent to an income of NOK ".expr() + oppjustertInntektFoerUfoer + ".".expr()
            )
        }
        showIf(harFullUfoeregrad) {
            paragraph {
                textExpr(
                    Bokmal to "Du har rett til 100 prosent uføretrygd, som utgjør ".expr() + fullUfoeretrygdPerAar + " kroner per år.".expr(),
                    Nynorsk to "Du har rett til 100 prosent uføretrygd, som utgjer ".expr() + fullUfoeretrygdPerAar + " kroner per år.".expr(),
                    English to "For you, a 100 percent disability benefit will total NOK ".expr() + fullUfoeretrygdPerAar + " per year.".expr()
                )
            }
        }
        showIf(harDelvisUfoeregrad) {
            paragraph {
                textExpr(
                    Bokmal to "Du har rett til ".expr() + ufoeregrad + " prosent uføretrygd. Regnet om til 100 prosent uføretrygd, utgjør dette ".expr() + fullUfoeretrygdPerAar + " kroner per år.".expr(),
                    Nynorsk to "Du har rett til ".expr() + ufoeregrad + " prosent uføretrygd. Rekna om til 100 prosent uføretrygd, utgjer dette ".expr() + fullUfoeretrygdPerAar + " kroner per år.".expr(),
                    English to "You are entitled to ".expr() + ufoeregrad + " percent disability benefit. Recalculated to 100 prosent disability, this totals NOK ".expr() + fullUfoeretrygdPerAar + " per year".expr(),
                )
            }
        }
        paragraph {
            textExpr(
                Bokmal to "Vi beregner kompensasjonsgraden din slik: ".expr() + fullUfoeretrygdPerAar + " / ".expr() + oppjustertInntektFoerUfoer + " * 100 = ".expr() + kompensasjonsgrad + " prosent.".expr(),
                Nynorsk to "Vi bereknar kompensasjonsgraden din slik: ".expr() + fullUfoeretrygdPerAar + " / ".expr() + oppjustertInntektFoerUfoer + " * 100 = ".expr() + kompensasjonsgrad + " prosent.".expr(),
                English to "We have calculated your degree of compensation as follows: ".expr() + fullUfoeretrygdPerAar + " / ".expr() + oppjustertInntektFoerUfoer + " * 100 = ".expr() + kompensasjonsgrad + " percent.".expr()
            )
        }
    }
}





