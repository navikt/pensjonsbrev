package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.harInntektsgrenseLessThanInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.nettoAkkumulertePlussNettoRestAar
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.ufoeretrygdPlussInntekt
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.harForventetInntektLargerThanInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.harInntektsgrenseLargerThanOrEqualToInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.harUtbetalingsgradLessThanUfoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harNyUTBeloep
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harTotalNettoUT
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoAkkumulerteBeloepUtbetalt
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoTilUtbetalingRestenAvAaret
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.reduksjonIUfoeretrygd
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

// TBU062V, TBU063V, TBU064V, TBU074V, TBU600V, TBU065V
data class TabellSlikBlirDinUtbetalingFoerSkatt(
    val beregningUfoere: Expression<OpplysningerBruktIBeregningUTDto.BeregningUfoere>,
    val inntektsAvkortingGjeldende: Expression<OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende>,
    val kravAarsakType: Expression<KravAarsakType>,
    val ufoeretrygdGjeldende: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende>,
    val ufoeretrygdOrdinaer: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val bruttoBeregnetUfoeretrygd = beregningUfoere.nettoAkkumulertePlussNettoRestAar.format()
        val inntektsgrenseAar = inntektsAvkortingGjeldende.inntektsgrenseAar.format()
        val inntektstak = inntektsAvkortingGjeldende.inntektstak.format()
        val reduksjonIUfoeretrygd = ufoeretrygdOrdinaer.reduksjonIUfoeretrygd.format()
        val ufoeregrad = ufoeretrygdGjeldende.ufoeregrad.format()
        val ufoeretrygdPlussInntekt = beregningUfoere.ufoeretrygdPlussInntekt.format()
        val ufoeretrygdUtbetaltRestenAvAaret = ufoeretrygdOrdinaer.nettoTilUtbetalingRestenAvAaret.format()
        val utbetaltUfoeretrygd = ufoeretrygdOrdinaer.nettoAkkumulerteBeloepUtbetalt.format()

        showIf(ufoeretrygdGjeldende.harUtbetalingsgradLessThanUfoeregrad and kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {
            title1 {
                text(
                    Bokmal to "Slik blir din utbetaling før skatt",
                    Nynorsk to "Slik blir den månadlege utbetalinga di før skatt",
                    English to "This is your monthly payment before tax"
                )
            }
            paragraph {
                table(header = {
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT) {
                        text(
                            Bokmal to "", Nynorsk to "", English to ""

                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Beløp",
                            Nynorsk to "Beløp",
                            English to "Amount",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    }
                }) {
                    row {
                        cell {
                            text(
                                Bokmal to "Brutto beregnet uføretrygd som følge av innmeldt inntekt:",
                                Nynorsk to "Brutto berekna uføretrygd som følgje av innmeld inntekt:",
                                English to "Gross estimated disability benefit corresponding to a reported income:"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to bruttoBeregnetUfoeretrygd,
                                Nynorsk to bruttoBeregnetUfoeretrygd,
                                English to bruttoBeregnetUfoeretrygd

                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "- Utbetalt uføretrygd hittil i år:",
                                Nynorsk to "- Utbetalt uføretrygd hittil i år:",
                                English to "- Disability benefit payments so far this year:"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to utbetaltUfoeretrygd,
                                Nynorsk to utbetaltUfoeretrygd,
                                English to utbetaltUfoeretrygd
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "= Utbetaling av uføretrygd for resterende måneder i året:",
                                Nynorsk to "= Utbetaling av uføretrygd for resterande månader i året:",
                                English to "= Disability benefit payments for the remaining months of the year:"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to ufoeretrygdUtbetaltRestenAvAaret,
                                Nynorsk to ufoeretrygdUtbetaltRestenAvAaret,
                                English to ufoeretrygdUtbetaltRestenAvAaret
                            )
                        }
                    }
                }
            }
        }
        // TBU063V  / brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_06_300)
        showIf(
            ufoeretrygdOrdinaer.harBeloepRedusert and ufoeretrygdOrdinaer.harTotalNettoUT
                    and beregningUfoere.harInntektsgrenseLessThanInntektstak and ufoeretrygdOrdinaer.harNyUTBeloep
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Du vil få en månedlig reduksjon i uføretrygden din på ".expr() + reduksjonIUfoeretrygd + " kroner i resterende måneder i kalenderåret.".expr(),
                    Nynorsk to "Du får ein månadleg reduksjon i uføretrygda di på ".expr() + reduksjonIUfoeretrygd + " kroner i resterande månader i kalenderåret.".expr(),
                    English to "Your monthly disability benefit payments will be reduced by NOK ".expr() + reduksjonIUfoeretrygd + " for the remaining months of the calendar year.".expr()
                )
            }
            // TBU064V  / brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_06_300, PE_UT_07_200)
            paragraph {
                textExpr(
                    Bokmal to "Uføretrygden og inntekten din vil ut fra dette til sammen utgjøre ".expr() + ufoeretrygdPlussInntekt + " kroner for dette året.".expr(),
                    Nynorsk to "Uføretrygda di og inntekta di utgjer til saman ".expr() + ufoeretrygdPlussInntekt + " kroner i dette året.".expr(),
                    English to "Your disability benefit and income together will total NOK ".expr() + ufoeretrygdPlussInntekt + " for this year.".expr()
                )
            }
        }
        // TBU074V  / brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_06_300, PE_UT_07_200)
        showIf(
            inntektsAvkortingGjeldende.harInntektsgrenseLargerThanOrEqualToInntektstak and ufoeretrygdGjeldende.harUtbetalingsgradLessThanUfoeregrad
                    and not(ufoeretrygdOrdinaer.harNyUTBeloep)
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Utbetalingen av uføretrygden din er redusert, fordi du har inntekt. Den innmeldte inntekten er høyere enn inntektsgrensen din på ".expr() + inntektsgrenseAar + " kroner og uføretrygden blir derfor ikke utbetalt.".expr(),
                    Nynorsk to "Utbetalinga av uføretrygda di er redusert, fordi du har inntekt. Den innmelde inntekta er høgare enn inntektsgrensa di på ".expr() + inntektsgrenseAar + " kroner og uføretrygda blir derfor ikkje utbetalt.".expr(),
                    English to "Your payment have been reduced because you have an income. The reported income is higher then your income cap of NOK ".expr() + inntektsgrenseAar + ". Your disability benefit will therefore not be paid.".expr()

                )
            }
        }
        // TBU600V
        showIf(not(ufoeretrygdOrdinaer.harNyUTBeloep) and beregningUfoere.harInntektsgrenseLessThanInntektstak and inntektsAvkortingGjeldende.harForventetInntektLargerThanInntektstak) {
            paragraph {
                textExpr(
                    Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + inntektstak + " kroner.".expr(),
                    Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil si ".expr() + inntektstak + " kroner.".expr(),
                    English to "You will not receive disability benefit because your income exceeds 80 percent of your income prior to disability, which is NOK ".expr() + inntektstak + ".".expr()
                )
            }
        }
        // TBU064V  / brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_06_300, PE_UT_07_200)
        showIf(ufoeretrygdGjeldende.harUtbetalingsgradLessThanUfoeregrad) {
            paragraph {
                textExpr(
                    Bokmal to "Du vil få tilbake ".expr() + ufoeregrad + " prosent uføretrygd uten søknad, dersom du tjener mindre enn inntektsgrensen din. Hvis du allerede har fått utbetalt det du har rett til i uføretrygd for kalenderåret, vil du ikke få utbetalt uføretrygd med den opprinnelige uføregraden din før neste kalenderår.".expr(),
                    Nynorsk to "Du får tilbake ".expr() + ufoeregrad + " prosent uføretrygd utan søknad dersom du tener mindre enn inntektsgrensa di. Dersom du allereie har fått utbetalt det du har rett til i uføretrygd for kalenderåret, får du ikkje utbetalt uføretrygd med den opphavlege uføregraden din før neste kalenderår.".expr(),
                    English to "You will receive NOK ".expr() + ufoeregrad + " percent disability benefit without an application if your income falls below your income limit. If you already have been paid what you are entitled to in disability benefits this calendar year, you will not receive any disability benefit payments at your original degree of disability until the next calendar year.".expr()
                )
            }
        }
    }
}

