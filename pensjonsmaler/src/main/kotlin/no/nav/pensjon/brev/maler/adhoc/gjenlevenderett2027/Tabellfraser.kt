package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

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
                                bokmal { + "År" },
                                nynorsk { + "År" },
                                english { + "Year" }
                            )
                        }
                        column(2) {
                            text(
                                bokmal { + "Din inntekt" },
                                nynorsk { + "Di inntekt" },
                                english { + "Your income" }
                            )
                        }
                        column(2) {
                            text(
                                bokmal { + "Din inntekt i G" },
                                nynorsk { + "Di inntekt i G" },
                                english { + "Your income in G" }
                            )
                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                bokmal { + "2019" },
                                nynorsk { + "2019" },
                                english { + "2019" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + inntekt2019.format(CurrencyFormat) + " kroner" },
                                nynorsk { + inntekt2019.format(CurrencyFormat) + " kroner" },
                                english { + "NOK " + inntekt2019.format(CurrencyFormat) },

                                )
                        }
                        cell {
                            text(
                            bokmal { + inntekt2019G.format(6) + " G" },
                            nynorsk { + inntekt2019G.format(6) + " G" },
                            english { + inntekt2019G.format(6) + " G" },

                                )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "2020" },
                                nynorsk { + "2020" },
                                english { + "2020" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + inntekt2020.format(CurrencyFormat) + " kroner" },
                                nynorsk { + inntekt2020.format(CurrencyFormat) + " kroner" },
                                english { + "NOK " + inntekt2020.format(CurrencyFormat) }
                            )
                        }
                        cell {
                            text(
                            bokmal { + inntekt2020G.format(6) + " G" },
                            nynorsk { + inntekt2020G.format(6) + " G" },
                            english { + inntekt2020G.format(6) + " G" },

                                )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "2021" },
                                nynorsk { + "2021" },
                                english { + "2021" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + inntekt2021.format(CurrencyFormat) + " kroner" },
                                nynorsk { + inntekt2021.format(CurrencyFormat) + " kroner" },
                                english { + "NOK " + inntekt2021.format(CurrencyFormat) }

                            )
                        }
                        cell {
                            text(
                            bokmal { + inntekt2021G.format(6) + " G" },
                            nynorsk { + inntekt2021G.format(6) + " G" },
                            english { + inntekt2021G.format(6) + " G" },

                                )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "2022" },
                                nynorsk { + "2022" },
                                english { + "2022" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + inntekt2022.format(CurrencyFormat) + " kroner" },
                                nynorsk { + inntekt2022.format(CurrencyFormat) + " kroner" },
                                english { + "NOK " + inntekt2022.format(CurrencyFormat) }
                            )
                        }
                        cell {
                            text(
                            bokmal { + inntekt2022G.format(6) + " G" },
                            nynorsk { + inntekt2022G.format(6) + " G" },
                            english { + inntekt2022G.format(6) + " G" },

                                )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "2023" },
                                nynorsk { + "2023" },
                                english { + "2023" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + inntekt2023.format(CurrencyFormat) + " kroner" },
                                nynorsk { + inntekt2023.format(CurrencyFormat) + " kroner" },
                                english { + "NOK " + inntekt2023.format(CurrencyFormat) }
                            )
                        }
                        cell {
                            text(
                            bokmal { + inntekt2023G.format(6) + " G" },
                            nynorsk { + inntekt2023G.format(6) + " G" },
                            english { + inntekt2023G.format(6) + " G" },
                            )
                        }
                    }
                    row {

                        cell {}
                        cell {
                            text(
                                bokmal { + "Ditt gjennomsnitt: " },
                                nynorsk { + "Gjennomsnittet ditt: " },
                                english { + "Your average: " },
                                Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                            )
                        }
                        cell {
                            text(
                            bokmal { + gjennomsnittInntektG.format(6) + " G" },
                            nynorsk { + gjennomsnittInntektG.format(6) + " G" },
                            english { + gjennomsnittInntektG.format(6) + " G" },
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
                                bokmal { + "År " },
                                nynorsk { + "År " },
                                english { + "Year " },
                            )
                        }
                        column(2) {
                            text(
                                bokmal { + "Gjennomsnittlig grunnbeløp (G) ganger 3" },
                                nynorsk { + "Gjennomsnittleg grunnbeløp (G) gongar 3" },
                                english { + "Average National Insurance basic amount (G) times 3" }
                            )
                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                bokmal { + "2022" },
                                nynorsk { + "2022" },
                                english { + "2022" }

                            )
                        }
                        cell {
                            text(
                                bokmal { + g2022.format(CurrencyFormat) + " kroner" },
                                nynorsk { + g2022.format(CurrencyFormat) + " kroner" },
                                english { + "NOK " + g2022.format(CurrencyFormat) }

                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "2023" },
                                nynorsk { + "2023" },
                                english { + "2023" }

                            )
                        }
                        cell {
                            text(
                                bokmal { + g2023.format(CurrencyFormat) + " kroner" },
                                nynorsk { + g2023.format(CurrencyFormat) + " kroner" },
                                english { + "NOK " + g2023.format(CurrencyFormat) }

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
                                bokmal { +  "År " },
                                nynorsk { +  "År " },
                                english { +  "Year " },

                                )
                        }
                        column(2) {
                            text(
                                bokmal { +  "Gjennomsnittlig grunnbeløp (G) ganger 2" },
                                nynorsk { +  "Gjennomsnittleg grunnbeløp (G) gongar 2" },
                                english { +  "Average National Insurance basic amount (G) times 2" }

                            )
                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                bokmal { +  "2019" },
                                nynorsk { +  "2019" },
                                english { +  "2019" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +  g2019.format(CurrencyFormat) +" kroner" },
                                nynorsk { +  g2019.format(CurrencyFormat) +" kroner" },
                                english { +  "NOK " + g2019.format(CurrencyFormat) }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +  "2020" },
                                nynorsk { +  "2020" },
                                english { +  "2020" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +  g2020.format(CurrencyFormat) +" kroner" },
                                nynorsk { +  g2020.format(CurrencyFormat) +" kroner" },
                                english { +  "NOK " + g2020.format(CurrencyFormat) }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +  "2021" },
                                nynorsk { +  "2021" },
                                english { +  "2021" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +  g2021.format(CurrencyFormat) +" kroner" },
                                nynorsk { +  g2021.format(CurrencyFormat) +" kroner" },
                                english { +  "NOK " + g2021.format(CurrencyFormat) }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +  "2022" },
                                nynorsk { +  "2022" },
                                english { +  "2022" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +  g2022.format(CurrencyFormat) +" kroner" },
                                nynorsk { +  g2022.format(CurrencyFormat) +" kroner" },
                                english { +  "NOK " + g2022.format(CurrencyFormat) }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +  "2023" },
                                nynorsk { +  "2023" },
                                english { +  "2023" }

                            )
                        }
                        cell {
                            text(
                                bokmal { +  g2023.format(CurrencyFormat) +" kroner" },
                                nynorsk { +  g2023.format(CurrencyFormat) +" kroner" },
                                english { +  "NOK " + g2023.format(CurrencyFormat) }
                            )
                        }
                    }
                }
            }
        }
    }
}
