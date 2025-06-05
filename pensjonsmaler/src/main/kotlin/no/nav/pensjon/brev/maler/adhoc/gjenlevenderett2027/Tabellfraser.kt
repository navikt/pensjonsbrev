package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
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
    val gjennomsnitt: Expression<Int>,
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
                            Nynorsk to "Di inntekt:",
                            English to "Your income"
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
                }
                row {
                    cell {
                        text(
                            Bokmal to "Din gjennomsnitt",
                            Nynorsk to "Din gjennomsnitt",
                            English to "Your average",

                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to gjennomsnitt.format(CurrencyFormat) + " kroner",
                            Nynorsk to gjennomsnitt.format(CurrencyFormat) + " kroner",
                            English to "NOK ".expr() + gjennomsnitt.format(CurrencyFormat),
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                }
            }
        }
}
