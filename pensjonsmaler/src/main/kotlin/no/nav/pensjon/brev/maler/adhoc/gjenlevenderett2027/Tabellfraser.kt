package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class DineInntekterTabell(
    val inntekt2019: Expression<Int>,
    val inntekt2020: Expression<Int>,
    val inntekt2021: Expression<Int>,
    val inntekt2022: Expression<Int>,
    val inntekt2023: Expression<Int>,
    val gInntekt2023: Expression<Double>,
    val gInntekt2022: Expression<Double>,
    val gInntekt2021: Expression<Double>,
    val gInntekt2020: Expression<Double>,
    val gInntekt2019: Expression<Double>,
    val gjennomsnitt: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            table(
                header = {
                    column(1) {
                        text(
                            Bokmal to "År",
                            Nynorsk to "År",
                            English to "Year"
                        )
                    }
                    column(2) {
                        text(
                            Bokmal to "Din inntekt",
                            Nynorsk to "Di inntekt",
                            English to "Your income"
                        )
                    }
                    column(2) {
                        text(
                            Bokmal to "Din inntekt i G",
                            Nynorsk to "Di inntekt i G",
                            English to "Your income in G"
                        )
                    }
                },
            ) {
                row {
                    cell {
                        text(
                            Bokmal to "2019",
                            Nynorsk to "2019",
                            English to "2019",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to inntekt2019.format(CurrencyFormat) + " kroner",
                            Nynorsk to inntekt2019.format(CurrencyFormat) + " kroner",
                            English to "NOK ".expr() + inntekt2019.format(CurrencyFormat),

                            )
                    }
                    cell {
                        textExpr(
                            Bokmal to gInntekt2019.format(6) + " G",
                            Nynorsk to gInntekt2019.format(6) + " G",
                            English to gInntekt2019.format(6) + " G",

                            )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "2020",
                            Nynorsk to "2020",
                            English to "2020",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to inntekt2020.format(CurrencyFormat) + " kroner",
                            Nynorsk to inntekt2020.format(CurrencyFormat) + " kroner",
                            English to "NOK ".expr() + inntekt2020.format(CurrencyFormat)
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to gInntekt2020.format(6) + " G",
                            Nynorsk to gInntekt2020.format(6) + " G",
                            English to gInntekt2020.format(6) + " G",

                            )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "2021",
                            Nynorsk to "2021",
                            English to "2021",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to inntekt2021.format(CurrencyFormat) + " kroner",
                            Nynorsk to inntekt2021.format(CurrencyFormat) + " kroner",
                            English to "NOK ".expr() + inntekt2021.format(CurrencyFormat)

                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to gInntekt2021.format(6) + " G",
                            Nynorsk to gInntekt2021.format(6) + " G",
                            English to gInntekt2021.format(6) + " G",

                            )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "2022",
                            Nynorsk to "2022",
                            English to "2022",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to inntekt2022.format(CurrencyFormat) + " kroner",
                            Nynorsk to inntekt2022.format(CurrencyFormat) + " kroner",
                            English to "NOK ".expr() + inntekt2022.format(CurrencyFormat)
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to gInntekt2022.format(6) + " G",
                            Nynorsk to gInntekt2022.format(6) + " G",
                            English to gInntekt2022.format(6) + " G",

                            )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "2023",
                            Nynorsk to "2023",
                            English to "2023",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to inntekt2023.format(CurrencyFormat) + " kroner",
                            Nynorsk to inntekt2023.format(CurrencyFormat) + " kroner",
                            English to "NOK ".expr() + inntekt2023.format(CurrencyFormat)
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to gInntekt2023.format(6) + " G",
                            Nynorsk to gInntekt2023.format(6) + " G",
                            English to gInntekt2023.format(6) + " G",
                        )
                    }
                }
                row {

                    cell {}
                    cell {
                        text(
                            Bokmal to "Ditt gjennomsnitt: ",
                            Nynorsk to "Gjennomsnittet ditt: ",
                            English to "Your average: ",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to gjennomsnitt.format(6) + " G",
                            Nynorsk to gjennomsnitt.format(6) + " G",
                            English to gjennomsnitt.format(6) + " G",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                }
            }
        }
}
