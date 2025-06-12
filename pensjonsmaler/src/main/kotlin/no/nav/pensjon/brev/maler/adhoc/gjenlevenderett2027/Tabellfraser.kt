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

object Tabeller {
    data class DineInntekterTabell(
        val inntekt2019: Expression<Int>,
        val inntekt2020: Expression<Int>,
        val inntekt2021: Expression<Int>,
        val inntekt2022: Expression<Int>,
        val inntekt2023: Expression<Int>,
        val gjennomsnittInntektG: Expression<Double>,
        val inntekt2019G: Expression<Double>,
        val inntekt2020G: Expression<Double>,
        val inntekt2021G: Expression<Double>,
        val inntekt2022G: Expression<Double>,
        val inntekt2023G: Expression<Double>,
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
                            Bokmal to inntekt2019G.format(6) + " G",
                            Nynorsk to inntekt2019G.format(6) + " G",
                            English to inntekt2019G.format(6) + " G",

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
                            Bokmal to inntekt2020G.format(6) + " G",
                            Nynorsk to inntekt2020G.format(6) + " G",
                            English to inntekt2020G.format(6) + " G",

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
                            Bokmal to inntekt2021G.format(6) + " G",
                            Nynorsk to inntekt2021G.format(6) + " G",
                            English to inntekt2021G.format(6) + " G",

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
                            Bokmal to inntekt2022G.format(6) + " G",
                            Nynorsk to inntekt2022G.format(6) + " G",
                            English to inntekt2022G.format(6) + " G",

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
                            Bokmal to inntekt2023G.format(6) + " G",
                            Nynorsk to inntekt2023G.format(6) + " G",
                            English to inntekt2023G.format(6) + " G",
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
                            Bokmal to gjennomsnittInntektG.format(6) + " G",
                            Nynorsk to gjennomsnittInntektG.format(6) + " G",
                            English to gjennomsnittInntektG.format(6) + " G",
                                Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                            )
                        }
                    }
                }
            }
    }

    object Gjennomsnittlig3GTabell : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val g2022 = 329_352.expr()
            val g2023 = 348_717.expr()

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År ",
                                Nynorsk to "År ",
                                English to "Year ",
                            )
                        }
                        column(2) {
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 3",
                                Nynorsk to "Gjennomsnittleg grunnbeløp (G) gongar 3",
                                English to "Average National Insurance basic amount (G) times 3"
                            )
                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                                Nynorsk to "2022",
                                English to "2022"

                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to g2022.format(CurrencyFormat) + " kroner",
                                Nynorsk to g2022.format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + g2022.format(CurrencyFormat)

                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                                Nynorsk to "2023",
                                English to "2023"

                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to g2023.format(CurrencyFormat) + " kroner",
                                Nynorsk to g2023.format(CurrencyFormat) + " kroner",
                                English to "NOK ".expr() + g2023.format(CurrencyFormat)

                            )
                        }
                    }
                }
            }
        }
    }

    object Gjennomsnittlig2GTabell : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val g2019 = 197_732.expr()
            val g2020 = 201_706.expr()
            val g2021 = 209_432.expr()
            val g2022 = 219_568.expr()
            val g2023 = 232_478.expr()

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År ",
                                Nynorsk to "År ",
                                English to "Year ",

                                )
                        }
                        column(2) {
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 2",
                                Nynorsk to "Gjennomsnittleg grunnbeløp (G) gongar 2",
                                English to "Average National Insurance basic amount (G) times 2"

                            )
                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2019",
                                Nynorsk to "2019",
                                English to "2019"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to g2019.format(CurrencyFormat) +" kroner",
                                Nynorsk to g2019.format(CurrencyFormat) +" kroner",
                                English to "NOK ".expr() + g2019.format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2020",
                                Nynorsk to "2020",
                                English to "2020"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to g2020.format(CurrencyFormat) +" kroner",
                                Nynorsk to g2020.format(CurrencyFormat) +" kroner",
                                English to "NOK ".expr() + g2020.format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2021",
                                Nynorsk to "2021",
                                English to "2021"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to g2021.format(CurrencyFormat) +" kroner",
                                Nynorsk to g2021.format(CurrencyFormat) +" kroner",
                                English to "NOK ".expr() + g2021.format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                                Nynorsk to "2022",
                                English to "2022"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to g2022.format(CurrencyFormat) +" kroner",
                                Nynorsk to g2022.format(CurrencyFormat) +" kroner",
                                English to "NOK ".expr() + g2022.format(CurrencyFormat)
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                                Nynorsk to "2023",
                                English to "2023"

                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to g2023.format(CurrencyFormat) +" kroner",
                                Nynorsk to g2023.format(CurrencyFormat) +" kroner",
                                English to "NOK ".expr() + g2023.format(CurrencyFormat)
                            )
                        }
                    }
                }
            }
        }
    }
}
