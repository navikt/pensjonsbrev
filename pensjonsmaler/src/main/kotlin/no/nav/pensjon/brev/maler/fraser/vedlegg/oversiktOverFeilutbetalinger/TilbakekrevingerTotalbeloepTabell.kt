package no.nav.pensjon.brev.maler.fraser.vedlegg.oversiktOverFeilutbetalinger

import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner

data class TilbakekrevingerTotalbeloepTabell(
    val bruttoTilbakekrevdTotalbeloep: Expression<Kroner>,
    val nettoTilbakekrevdTotalbeloep: Expression<Kroner>,
    val rentetilleggSomInnkrevesBeloep: Expression<Kroner?>,
    val skattefradragSomInnkrevesTotalbeloep: Expression<Kroner?>
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
                        includePhrase(KronerText(bruttoTilbakekrevdTotalbeloep))
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
                            includePhrase(KronerText(skattefradragSomInnkrevesTotalbeloep.ifNull(Kroner(0))))
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
                        includePhrase(KronerText(nettoTilbakekrevdTotalbeloep))
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
                        includePhrase(KronerText(rentetilleggSomInnkrevesBeloep.ifNull(Kroner(0))))
                    }
                }
            }
        }
    }
}

