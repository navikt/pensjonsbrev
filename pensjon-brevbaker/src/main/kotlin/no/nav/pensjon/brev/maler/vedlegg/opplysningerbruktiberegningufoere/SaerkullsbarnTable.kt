import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarBrutto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepAarNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.beloepNetto
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektOverFribeloep
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text


showIf(saerkullTillegg.erRedusertMotinntekt) {
    title1 {
        text(
            Language.Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
            Language.Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
            Language.English to "Reduction of child supplement payment for children from a previous relationship before tax"
        )
    }

    table(
        header = {
            column(2) {
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
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )
            }
        }
    ) {
        showIf(
            saerkullTillegg.beloepNetto.greaterThan(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                0
            )
        ) {
            row {
                cell {
                    text(
                        Language.Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                        Language.Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                        Language.English to "Yearly child supplement before income reduction"
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(saerkullTillegg.beloepAarBrutto))
                }
            }
        }
        showIf(saerkullTillegg.erRedusertMotinntekt) {
            row {
                cell {
                    text(
                        Language.Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ",
                        Language.Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ",
                        Language.English to "Total income applied in calculation of reduction in child supplement is ",
                    )
                }

                cell {
                    includePhrase(Felles.KronerText(saerkullTillegg.inntektBruktIAvkortning))
                }
            }
        }
        showIf(
            saerkullTillegg.beloepNetto.greaterThan(0) or (saerkullTillegg.beloepNetto.lessThan(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                0
            ))
        ) {
            row {
                cell {
                    text(
                        Language.Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er",
                        Language.Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er",
                        Language.English to "Exemption amount applied in calculation of reduction in child supplement is",
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(saerkullTillegg.fribeloep))
                }
            }
        }
        showIf(
            saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                0
            ))
        ) {
            row {
                cell {
                    text(
                        Language.Bokmal to "Inntekt over fribeløpet er",
                        Language.Nynorsk to "Inntekt over fribeløpet er",
                        Language.English to "Income exceeding the exemption amount is",
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(saerkullTillegg.inntektOverFribeloep))
                }
            }
        }
        showIf(
            not(saerkullTillegg.fribeloepEllerInntektErPeriodisert)
                    and (saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(
                0
            ) and saerkullTillegg.beloepAarNetto.notEqualTo(
                0
            )))
                    and saerkullTillegg.avkortningsbeloepAar.greaterThan(0)
        ) {
            row {
                cell {
                    text( // TODO finn en fornuftig måte å vise regnestykket på
                        Language.Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                        Language.Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                        Language.English to "- 50 percent of income exceeding the allowance amount"
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(saerkullTillegg.avkortningsbeloepAar))
                }
            }
        }
        showIf(
            saerkullTillegg.fribeloepEllerInntektErPeriodisert
                    and (saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(
                0
            ) and saerkullTillegg.beloepAarNetto.notEqualTo(
                0
            )))
                    and saerkullTillegg.avkortningsbeloepAar.greaterThan(0)
        ) {
            row {
                cell {
                    text(
                        Language.Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløp)",
                        Language.Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet (oppgitt som eit årleg beløp)",
                        Language.English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(saerkullTillegg.avkortningsbeloepAar))
                }
            }
        }
        showIf(saerkullTillegg.justeringsbeloepAar.notEqualTo(0)) {
            row {
                cell {
                    text(
                        Language.Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                        Language.Nynorsk to "+ Beløp som er brukt for å justera reduksjonen av barnetillegget",
                        Language.English to "+ Amount which is used to adjust the reduction of child supplement"
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(saerkullTillegg.justeringsbeloepAar))
                }
            }
        }
        showIf(
            saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(0) and saerkullTillegg.beloepAarNetto.notEqualTo(
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
                    includePhrase(Felles.KronerText(saerkullTillegg.beloepAarNetto))
                }
            }
        }
        showIf(
            saerkullTillegg.beloepNetto.notEqualTo(0) or (saerkullTillegg.beloepNetto.equalTo(0) and saerkullTillegg.justeringsbeloepAar.notEqualTo(
                0
            ))
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
                    includePhrase(Felles.KronerText(saerkullTillegg.beloepNetto))
                }
            }
        }
        showIf(saerkullTillegg.beloepNetto.equalTo(0) and saerkullTillegg.beloepAarNetto.equalTo(0)) {
            row {
                cell {
                    text(
                        Language.Bokmal to "Grensen for å få utbetalt barnetillegg",
                        Language.Nynorsk to "Grensa for å få utbetalt barnetillegg",
                        Language.English to "The income limit for receiving child supplement"
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(saerkullTillegg.inntektstak))
                }
            }
        }
    }
}
