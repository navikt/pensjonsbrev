package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.bruttobeloepTilbakekrevd
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.feilutbetaltBeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.maanedOgAar
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.nettobeloepUtenRenterTilbakekrevd
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.resultatAvVurderingen
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.skattefradragSomInnkreves
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.ytelsenMedFeilutbetaling
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.bruttoTilbakekrevdTotalbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.nettoUtenRenterTilbakekrevdTotalbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.rentetilleggSomInnkrevesTotalbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.skattefradragSomInnkrevesTotalbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.tilbakekrevingPerMaaned
import no.nav.pensjon.brev.maler.fraser.common.KonteringTypeYtelseTextMappingStorBokstav
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.maler.fraser.common.ResultatAvVurderingenTextMappingStorBokstav
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner


@TemplateModelHelpers
val oversiktOverFeilutbetalingerPE = createAttachment<LangBokmalNynorskEnglish, OversiktOverFeilutbetalingPEDto>(
    title = newText(
        Bokmal to "Oversikt over feilutbetalinger",
        Nynorsk to "Oversikt over feilutbetalingar",
        English to "Overview of incorrect payments"
    ),
    includeSakspart = true,
) {
    includePhrase(
        TilbakekrevingerTotalbeloepTabell
            (
            bruttoTilbakekrevdTotalbeloep = bruttoTilbakekrevdTotalbeloep,
            nettoTilbakekrevdTotalbeloep = nettoUtenRenterTilbakekrevdTotalbeloep,
            rentetilleggSomInnkrevesBeloep = rentetilleggSomInnkrevesTotalbeloep,
            skattefradragSomInnkrevesTotalbeloep = skattefradragSomInnkrevesTotalbeloep
        )
    )
    title1 {
        text(
            Bokmal to "Detaljert oversikt over perioder med feilutbetalinger",
            Nynorsk to "Detaljert oversikt over periodar feilutbetalingar",
            English to "Detailed overview of periods with incorrect payments",
        )
    }
    includePhrase(TilbakekrevingerTabell(tilbakekreving = tilbakekrevingPerMaaned))
}

private data class TilbakekrevingerTotalbeloepTabell(
    val bruttoTilbakekrevdTotalbeloep: Expression<Kroner>,
    val nettoTilbakekrevdTotalbeloep: Expression<Kroner>,
    val rentetilleggSomInnkrevesBeloep: Expression<Kroner>,
    val skattefradragSomInnkrevesTotalbeloep: Expression<Kroner?>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(columnSpan = 3) {
                        text(
                            Bokmal to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
                            Nynorsk to "Beløp som skal krevjast tilbake i heile feilutbetalingsperioden",
                            English to "Reimbursement amount for entire error period",
                        )
                    }
                    column(columnSpan = 1, alignment = RIGHT) {}
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
                        includePhrase(KronerText(bruttoTilbakekrevdTotalbeloep.ifNull(Kroner(0))))
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
                        includePhrase(KronerText(nettoTilbakekrevdTotalbeloep.ifNull(Kroner(0)), FontType.BOLD))
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

private data class TilbakekrevingerTabell(
    val tilbakekreving: Expression<List<OversiktOverFeilutbetalingPEDto.Tilbakekreving>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        forEach(tilbakekreving) { tilbakekreves ->
            paragraph {
                table(
                    header = {
                        column(columnSpan = 3) {
                            textExpr(
                                Bokmal to tilbakekreves.maanedOgAar.formatYearMonth() + " - ",
                                Nynorsk to tilbakekreves.maanedOgAar.formatYearMonth() + " - ",
                                English to tilbakekreves.maanedOgAar.formatYearMonth() + " - "
                            )
                            includePhrase(
                                KonteringTypeYtelseTextMappingStorBokstav(
                                    ytelsenMedFeilutbetaling = tilbakekreves.ytelsenMedFeilutbetaling
                                )
                            )
                            text(
                                Bokmal to " - ",
                                Nynorsk to " - ",
                                English to " - "
                            )
                            includePhrase(
                                ResultatAvVurderingenTextMappingStorBokstav(
                                    resultatAvVurderingen = tilbakekreves.resultatAvVurderingen
                                )
                            )

                        }
                        column(columnSpan = 1, RIGHT) {}
                    }
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "Feilutbetalt beløp",
                                Nynorsk to "Feilutbetalt beløp",
                                English to "Incorrect payment"
                            )
                        }
                        cell {
                            includePhrase(KronerText(tilbakekreves.feilutbetaltBeloep))
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Brutto tilbakekreving",
                                Nynorsk to "Brutto tilbakekrevjing",
                                English to "Gross repayment amount"
                            )
                        }
                        cell {
                            includePhrase(KronerText(tilbakekreves.bruttobeloepTilbakekrevd.ifNull(Kroner(0))))
                        }
                    }
                    showIf(tilbakekreves.skattefradragSomInnkreves.notEqualTo(Kroner(0))) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Fradrag skatt",
                                    Nynorsk to "Frådrag skatt",
                                    English to "Tax deduction"
                                )
                            }
                            cell {
                                includePhrase(KronerText(tilbakekreves.skattefradragSomInnkreves))
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Netto tilbakekreving",
                                Nynorsk to "Netto tilbakekrevjing",
                                English to "Net repayment amount"
                            )
                        }
                        cell {
                            includePhrase(KronerText(tilbakekreves.nettobeloepUtenRenterTilbakekrevd.ifNull(Kroner(0))))
                        }
                    }
                }
            }
        }
    }
}
