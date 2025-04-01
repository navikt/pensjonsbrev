package no.nav.pensjon.brev.maler.fraser.vedlegg.oversiktOverFeilutbetalinger

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
@TemplateModelHelpers

data class TabellFeilutbetalingerTotalbeloep(
    val bruttoTilbakekrevdTotalBeloep: Expression<Kroner>,
    val nettoTilbakekrevdTotalBeloep: Expression<Kroner>,
    val rentetilleggSomInnkrevesBeloep: Expression<Kroner>,
    val skattefradragSomInnkrevesBeloep: Expression<Kroner>
): OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        paragraph {
            table(
            header = {
                column(columnSpan = 2) {
                    text(
                        Bokmal to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
                        Nynorsk to "Beløp som skal krevjast tilbake i heile feilutbetalingsperioden",
                        English to "Reimbursement amount for entire error period",
                    )
                }
                column(columnSpan = 0) {}
            }
            ) {
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
                            Bokmal to bruttoTilbakekrevdTotalBeloep.format() + " Kr",
                            Nynorsk to bruttoTilbakekrevdTotalBeloep.format() + " Kr",
                            English to bruttoTilbakekrevdTotalBeloep.format() + " NOK"
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
                            Bokmal to "- ".expr() + skattefradragSomInnkrevesBeloep.format() + " Kr",
                            Nynorsk to "- ".expr() + skattefradragSomInnkrevesBeloep.format() + " Kr",
                            English to "- ".expr() + skattefradragSomInnkrevesBeloep.format() + " NOK"
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Netto tilbakekreving",
                            Nynorsk to "Netto tilbakekrevjing",
                            English to "Net amount",
                            fontType = FontType.BOLD,
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to nettoTilbakekrevdTotalBeloep.format() + " Kr",
                            Nynorsk to nettoTilbakekrevdTotalBeloep.format() + " Kr",
                            English to nettoTilbakekrevdTotalBeloep.format() + " NOK",
                            fontType = FontType.BOLD,
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
                            Bokmal to rentetilleggSomInnkrevesBeloep.format() + " Kr",
                            Nynorsk to rentetilleggSomInnkrevesBeloep.format() + " Kr",
                            English to rentetilleggSomInnkrevesBeloep.format() + " NOK"
                        )
                    }
                }
            }
        }
    }
}

