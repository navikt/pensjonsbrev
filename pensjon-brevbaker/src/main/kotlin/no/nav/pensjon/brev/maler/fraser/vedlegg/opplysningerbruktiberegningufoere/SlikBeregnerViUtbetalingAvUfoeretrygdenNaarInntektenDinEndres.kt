package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingSelectors.forventetInntektAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harNyUTBeloep
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.overskytendeInntekt_safe
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

// <> brevkode PE_UT_04_102, PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_07_200,

// TBU057V, TBU058V, TBU059V, TBU060V
data class SlikBeregnerViUtbetalingAvUfoeretrygdenNaarInntektenDinEndres(
    val inntektsAvkorting: Expression<OpplysningerBruktIBeregningUTDto.InntektsAvkorting>,
    val ufoeretrygd: Expression<OpplysningerBruktIBeregningUTDto.Ufoeretrygd>,
    val ufoeretrygdOrdinaer: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val forventetInntektAar = inntektsAvkorting.forventetInntektAar.format()
        val inntektsgrenseAar = inntektsAvkorting.inntektsgrenseAar.format()
        val kompensasjonsgrad = ufoeretrygd.kompensasjonsgrad.format()

        title1 {
            text(
                Bokmal to "Slik beregner vi utbetaling av uføretrygden når inntekten din endres",
                Nynorsk to "Slik bereknar vi utbetaling av uføretrygda når inntekta di er endra",
                English to "This is how your disability benefit payments are calculated when your income changes"
            )
        }
        paragraph {
            text(
                Bokmal to "Utbetalingen av uføretrygden din er beregnet på nytt, fordi inntekten din er endret. Det er den innmeldte inntekten din og uføretrygden du har fått utbetalt hittil i år, som avgjør hvor mye du får utbetalt i de månedene som er igjen i kalenderåret.",
                Nynorsk to "Utbetalinga av uføretrygda di er berekna på nytt fordi inntekta di er endra. Det er den innmelde inntekta di og uføretrygda du har fått utbetalt hittil i år, som avgjer kor mykje du får utbetalt i dei månadene som er att av kalenderåret.",
                English to "Your disability benefit payment has been recalculated, because your income has changed. It is your reported income and the disability benefit you have been paid so far this year that determine how much you will be paid for the remainder of the calendar year."
            )
        }
        showIf(inntektsAvkorting.forventetInntektAar.greaterThanOrEqual(inntektsAvkorting.inntektsgrenseAar) and ufoeretrygdOrdinaer.harNyUTBeloep) {
            paragraph {
                textExpr(
                    Bokmal to "Uføretrygden reduseres med ".expr() + kompensasjonsgrad + " prosent av inntekten over ".expr() + inntektsgrenseAar + " kroner fordi du har en kompensasjonsgrad som er ".expr() + kompensasjonsgrad + " prosent.".expr(),
                    Nynorsk to "Uføretrygda blir redusert med ".expr() + kompensasjonsgrad + " prosent av inntekta over ".expr() + inntektsgrenseAar + " kroner fordi du har ein kompensasjonsgrad som er ".expr() + kompensasjonsgrad + " prosent.".expr(),
                    English to "Your disability benefit is reduced by ".expr() + kompensasjonsgrad + " percent of your income in excess of NOK ".expr() + inntektsgrenseAar + ", because your degree of compensation is ".expr() + kompensasjonsgrad + " percent.".expr()
                )
            }

            ifNotNull(ufoeretrygdOrdinaer.overskytendeInntekt_safe) { overskytendeInntekt ->
                paragraph {
                    textExpr(
                        Bokmal to "Du har en inntektsgrense på ".expr() + inntektsgrenseAar + " kroner og den innmeldte inntekten din er ".expr() + forventetInntektAar + " kroner. Dette betyr at overskytende inntekt er ".expr() + overskytendeInntekt.format() + " kroner.".expr(),
                        Nynorsk to "Du har ei inntektsgrense på ".expr() + inntektsgrenseAar + " kroner, og den innmelde inntekta di er ".expr() + forventetInntektAar + " kroner. Dette vil seie at overskytande inntekt er ".expr() + overskytendeInntekt.format() + " kroner.".expr(),
                        English to "Your income cap is NOK ".expr() + inntektsgrenseAar + ", and your reported income is NOK ".expr() + forventetInntektAar + ". This means that your excess income is NOK ".expr() + overskytendeInntekt.format() + ".".expr()
                    )
                }
            }
        }
    }
}



