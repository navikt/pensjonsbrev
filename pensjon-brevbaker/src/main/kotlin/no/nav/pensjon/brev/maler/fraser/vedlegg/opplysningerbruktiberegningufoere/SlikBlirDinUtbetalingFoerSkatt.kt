package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoAkkumulerteBeloepUtbetalt
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoAkkumulertePlussNettoRestAar
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoTilUtbetalingRestenAvAaret
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

// TBU062V, TBU063V, TBU064V, TBU074V, TBU600V, TBU065V
data class TabellSlikBlirDinUtbetalingFoerSkatt(
    val forventetInntektAar: Expression<Kroner>,
    val fradrag: Expression<Kroner>,
    val harBeloepRedusert: Expression<Boolean>,
    val harForventetInntektLargerThanInntektstak: Expression<Boolean>,
    val harInntektsgrenseLargerThanOrEqualToInntektstak: Expression<Boolean>,
    val harInntektsgrenseLessThanInntektstak: Expression<Boolean>,
    val harNyUTBeloep: Expression<Boolean>,
    val harUtbetalingsgradLessThanUfoeregrad: Expression<Boolean>,
    val inntektsgrenseAar: Expression<Kroner>,
    val inntektstak: Expression<Kroner>,
    val kravAarsakType: Expression<KravAarsakType>,
    val nettoAkkumulerteBeloepUtbetalt: Expression<Kroner>,
    val nettoAkkumulertePlussNettoRestAar: Expression<Kroner>,
    val nettoTilUtbetalingRestenAvAaret: Expression<Kroner>,
    val harTotalNettoUT: Expression<Boolean>,
    val ufoeregrad: Expression<Int>,
    val ufoeretrygdOrdinaer: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer>,


    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(harUtbetalingsgradLessThanUfoeregrad and kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {
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
                            Bokmal to "Beregning", Nynorsk to "Berekning", English to "Calculation"

                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT) {
                        text(
                            Bokmal to "Beløp",
                            Nynorsk to "Beløp",
                            English to "Amount"

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
                                Bokmal to ufoeretrygdOrdinaer.nettoAkkumulertePlussNettoRestAar.format(),
                                Nynorsk to ufoeretrygdOrdinaer.nettoAkkumulertePlussNettoRestAar.format(),
                                English to ufoeretrygdOrdinaer.nettoAkkumulertePlussNettoRestAar.format()

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
                                Bokmal to ufoeretrygdOrdinaer.nettoAkkumulerteBeloepUtbetalt.format(),
                                Nynorsk to ufoeretrygdOrdinaer.nettoAkkumulerteBeloepUtbetalt.format(),
                                English to ufoeretrygdOrdinaer.nettoAkkumulerteBeloepUtbetalt.format()
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
                                Bokmal to ufoeretrygdOrdinaer.nettoTilUtbetalingRestenAvAaret.format(),
                                Nynorsk to ufoeretrygdOrdinaer.nettoTilUtbetalingRestenAvAaret.format(),
                                English to ufoeretrygdOrdinaer.nettoTilUtbetalingRestenAvAaret.format()
                            )
                        }
                    }
                }
            }
        }
        // TBU063V  / brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_06_300)
        showIf(
            harBeloepRedusert and harTotalNettoUT and harInntektsgrenseLessThanInntektstak and harNyUTBeloep
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Du vil få en månedlig reduksjon i uføretrygden din på ".expr() + fradrag.format() + " kroner i resterende måneder i kalenderåret.".expr(),
                    Nynorsk to "Du får ein månadleg reduksjon i uføretrygda di på ".expr() + fradrag.format() + " kroner i resterande månader i kalenderåret.".expr(),
                    English to "Your monthly disability benefit payments will be reduced by NOK ".expr() + fradrag.format() + " for the remaining months of the calendar year.".expr()
                )
            }
            // TBU064V  / brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_06_300, PE_UT_07_200)
            paragraph {
                textExpr(
                    Bokmal to "Uføretrygden og inntekten din vil ut fra dette til sammen utgjøre ".expr() + nettoAkkumulerteBeloepUtbetalt.format() + " + ".expr() + nettoTilUtbetalingRestenAvAaret.format() + " + ".expr() + forventetInntektAar.format() + " kroner for dette året.".expr(),
                    Nynorsk to "Uføretrygda di og inntekta di utgjer til saman ".expr() + nettoAkkumulerteBeloepUtbetalt.format() + " + ".expr() + nettoTilUtbetalingRestenAvAaret.format() + " + ".expr() + forventetInntektAar.format() + " kroner i dette året.".expr(),
                    English to "Your disability benefit and income together will total NOK ".expr() + nettoAkkumulerteBeloepUtbetalt.format() + " + ".expr() + nettoTilUtbetalingRestenAvAaret.format() + " + ".expr() + forventetInntektAar.format() + " for this year.".expr()
                )
            }
        }
        // TBU074V  / brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_06_300, PE_UT_07_200)
        showIf(
            harInntektsgrenseLargerThanOrEqualToInntektstak and harUtbetalingsgradLessThanUfoeregrad and not(
                harNyUTBeloep
            )
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Utbetalingen av uføretrygden din er redusert, fordi du har inntekt. Den innmeldte inntekten er høyere enn inntektsgrensen din på ".expr() + inntektsgrenseAar.format() + " kroner og uføretrygden blir derfor ikke utbetalt.".expr(),
                    Nynorsk to "Utbetalinga av uføretrygda di er redusert, fordi du har inntekt. Den innmelde inntekta er høgare enn inntektsgrensa di på ".expr() + inntektsgrenseAar.format() + " kroner og uføretrygda blir derfor ikkje utbetalt.".expr(),
                    English to "Your payment have been reduced because you have an income. The reported income is higher then your income cap of NOK ".expr() + inntektsgrenseAar.format() + ". Your disability benefit will therefore not be paid.".expr()

                )
            }
        }
        // TBU600V
        showIf(not(harNyUTBeloep) and harInntektsgrenseLessThanInntektstak and harForventetInntektLargerThanInntektstak) {
            paragraph {
                textExpr(
                    Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + inntektstak.format() + " kroner.".expr(),
                    Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil si ".expr() + inntektstak.format() + " kroner.".expr(),
                    English to "You will not receive disability benefit because your income exceeds 80 percent of your income prior to disability, which is NOK ".expr() + inntektstak.format() + ".".expr()
                )
            }
        }
        // TBU064V  / brevkode not(PE_UT_04_108, PE_UT_04_109, PE_UT_06_300, PE_UT_07_200)
        showIf(harUtbetalingsgradLessThanUfoeregrad) {
            paragraph {
                textExpr(
                    Bokmal to "Du vil få tilbake ".expr() + ufoeregrad.format() + " prosent uføretrygd uten søknad, dersom du tjener mindre enn inntektsgrensen din. Hvis du allerede har fått utbetalt det du har rett til i uføretrygd for kalenderåret, vil du ikke få utbetalt uføretrygd med den opprinnelige uføregraden din før neste kalenderår.".expr(),
                    Nynorsk to "Du får tilbake ".expr() + ufoeregrad.format() + " prosent uføretrygd utan søknad dersom du tener mindre enn inntektsgrensa di. Dersom du allereie har fått utbetalt det du har rett til i uføretrygd for kalenderåret, får du ikkje utbetalt uføretrygd med den opphavlege uføregraden din før neste kalenderår.".expr(),
                    English to "You will receive NOK ".expr() + ufoeregrad.format() + " percent disability benefit without an application if your income falls below your income limit. If you already have been paid what you are entitled to in disability benefits this calendar year, you will not receive any disability benefit payments at your original degree of disability until the next calendar year.".expr()
                )
            }
        }
    }
}

