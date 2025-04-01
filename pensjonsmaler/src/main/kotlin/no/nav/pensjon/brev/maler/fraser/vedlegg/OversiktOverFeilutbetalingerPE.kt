package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner


@TemplateModelHelpers
val oversiktOverFeilutbetalingerPE =
    createAttachment(
        title = newText(
            Bokmal to "Oversikt over feilutbetalinger",
            Nynorsk to "Oversikt over feilutbetalingar",
            English to "Overview of incorrect payments"
        ),
        includeSakspart = true,
    ) {
        title2 {
            text(
                Bokmal to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
                Nynorsk to "Beløp som skal krevjast tilbake i heile feilutbetalingsperioden",
                English to "Total amount to be repaid",

                )
        }

        paragraph {
            table(header = {
                column(columnSpan = 8, alignment = RIGHT) {
                    text(
                        Bokmal to "Opplysning",
                        Nynorsk to "Opplysning",
                        English to "Information",
                    )
                }
                column(columnSpan = 4, alignment = RIGHT) {
                    text(
                        Bokmal to "Beløp",
                        Nynorsk to "Beløp",
                        English to "Amount",
                    )
                }
            }) {
                row {
                    cell {
                        text(
                            Bokmal to "Brutto tilbakekreving",
                            Nynorsk to "Brutto tilbakekrevjing",
                            English to "Gross amount to be repaid"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to OversiktOverFeilutbetalingPEDto.bruttoTilbakekrevdTotalBeloep.format() + " Kr".expr(),
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "- Fradrag skatt",
                            Nynorsk to "- Frådrag skatt",
                            English to "- Tax deduction",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to skattefradragSomInnkrevesTotalBeloep.format() + " Kr".expr(),
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Netto tilbakekreving",
                            Nynorsk to "Netto tilbakekrevjing",
                            English to "Net amount to be repaid"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to nettoUtenRenterTilbakekrevdTotalBeloep.format() + " Kr".expr(),
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "+ Rentetillegg",
                            Nynorsk to "+ Rentetillegg",
                            English to "+ Interest surcharge"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to rentetilleggSomInnkrevesTotalBeloep.format() + " Kr".expr(),
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
            }
        }
    }