package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class OpplysningerOmBarnetilleggTabell(
    val avkortningsbeloepAar: Expression<Kroner>,
    val beloepAarBrutto: Expression<Kroner>,
    val beloepAarNetto: Expression<Kroner>,
    val erRedusertMotinntekt: Expression<Boolean>,
    val fribeloep: Expression<Kroner>,
    val inntektBruktIAvkortning: Expression<Kroner>,
    val inntektOverFribeloep: Expression<Kroner>,
    val inntektstak: Expression<Kroner>,
    val justeringsbeloepAar: Expression<Kroner>,
    val beloepNetto: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Language.Bokmal to "Reduksjon av barnetillegg for fellesbarn før skatt",
                Language.Nynorsk to "Reduksjon av barnetillegg for fellesbarn før skatt",
                Language.English to "Reduction of child supplement payment for joint children before tax"
            )
        }
        val faarUtbetaltTillegg = beloepNetto.greaterThan(0)
        val harJusteringsbeloep = justeringsbeloepAar.notEqualTo(0)
        val harNettoBeloep = beloepNetto.notEqualTo(0)
        table(
            header = {
                column(columnSpan = 2) {
                    text(
                        Language.Bokmal to "Beskrivelse",
                        Language.Nynorsk to "Beskrivelse",
                        Language.English to "Description",
                        Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                    )
                }
                column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(
                        Language.Bokmal to "Beløp",
                        Language.Nynorsk to "Beløp",
                        Language.English to "Amount",
                        Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                    )
                }
            }
        ) {
            showIf(faarUtbetaltTillegg and harJusteringsbeloep) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                            Language.Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                            Language.English to "Yearly child supplement before income reduction"
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(beloepAarBrutto))
                    }
                }
            }
            showIf(erRedusertMotinntekt) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                            Language.Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                            Language.English to "Total income applied in calculation of reduction in child supplement is "
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(inntektBruktIAvkortning))
                    }
                }
            }
            showIf(faarUtbetaltTillegg and harJusteringsbeloep) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                            Language.Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                            Language.English to "Exemption amount applied in calculation of reduction in child supplement is",
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(fribeloep))
                    }
                }
            }
            showIf(harNettoBeloep or (not(harNettoBeloep) and harJusteringsbeloep)) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "Inntekt over fribeløpet er",
                            Language.Nynorsk to "Inntekt over fribeløpet er",
                            Language.English to "Income exceeding the exemption amount is",
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(inntektOverFribeloep))
                    }
                }
            }
            showIf(
                harNettoBeloep
                        and harJusteringsbeloep
                        and avkortningsbeloepAar.greaterThan(0)
            ) {
                row {
                    cell {
                        text( // TODO finn en fornuftig måte å vise regnestykket på
                            Language.Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløep)",
                            Language.Nynorsk to "- 50 prosent av inntekta som overstig fribeløpet (oppgitt som eit årleg beløp)",
                            Language.English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(avkortningsbeloepAar))
                    }
                }
            }
            showIf(harJusteringsbeloep) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                            Language.Nynorsk to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                            Language.English to "+ Amount which is used to adjust the reduction of child supplement"
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(justeringsbeloepAar))
                    }
                }
            }
            showIf(
                harNettoBeloep or (beloepNetto.equalTo(0) and beloepAarNetto.notEqualTo(
                    0
                ))
            ) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                            Language.Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                            Language.English to "= Yearly child supplement after income reduction",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(beloepAarNetto))
                    }
                }
            }
            showIf(
                faarUtbetaltTillegg or beloepNetto.equalTo(0)
            ) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "Utbetaling av barnetillegg per måned",
                            Language.Nynorsk to "Utbetaling av barnetillegg per månad",
                            Language.English to "Child supplement payment for the remaining months of the year"
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(beloepNetto))
                    }
                }
            }
            showIf(
                beloepNetto.equalTo(0) and justeringsbeloepAar.equalTo(0)
            ) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "Grensen for å få utbetalt barnetillegg",
                            Language.Nynorsk to "Grensa for å få utbetalt barnetillegg",
                            Language.English to "The income limit for receiving child supplement"
                        )
                    }
                    cell {
                        includePhrase(Felles.KronerText(inntektstak))
                    }
                }
            }
        }
    }
}