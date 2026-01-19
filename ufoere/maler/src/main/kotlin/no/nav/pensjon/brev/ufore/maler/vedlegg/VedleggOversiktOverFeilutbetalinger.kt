package no.nav.pensjon.brev.ufore.maler.vedlegg

import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.formatMonthYear
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.bruttobelopTilbakekrevd
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.feilutbetaltBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.manedOgAr
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.nettobelopUtenRenterTilbakekrevd
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.resultatAvVurderingen
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.skattefradragSomInnkreves
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.ytelsenMedFeilutbetaling
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.bruttoTilbakekrevdTotalbelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.nettoUtenRenterTilbakekrevdTotalbelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.rentetilleggSomInnkrevesTotalbelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.skattefradragSomInnkrevesTotalbelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.tilbakekrevingPerManed
import no.nav.pensjon.brev.ufore.maler.fraser.KonteringTypeYtelseTextMappingStorBokstav
import no.nav.pensjon.brev.ufore.maler.fraser.ResultatAvVurderingenTextMappingStorBokstav

@TemplateModelHelpers
val oversiktOverFeilutbetalinger =
    createAttachment(
        title = { text(bokmal { +"Oversikt over feilutbetalinger" }) },
        includeSakspart = true,
    ) {
        includePhrase(
            TilbakekrevingerTotalbeloepTabell
                (
                bruttoTilbakekrevdTotalbelop = this.bruttoTilbakekrevdTotalbelop,
                nettoTilbakekrevdTotalbelop = nettoUtenRenterTilbakekrevdTotalbelop,
                rentetilleggSomInnkrevesBelop = rentetilleggSomInnkrevesTotalbelop,
                skattefradragSomInnkrevesTotalbelop = this.skattefradragSomInnkrevesTotalbelop
            )
        )
        title1 {
            text(bokmal { +"Detaljert oversikt over perioder med feilutbetalinger" },
            )
        }
        includePhrase(TilbakekrevingerTabell(tilbakekreving = tilbakekrevingPerManed))
    }

private data class TilbakekrevingerTotalbeloepTabell(
    val bruttoTilbakekrevdTotalbelop: Expression<Int>,
    val nettoTilbakekrevdTotalbelop: Expression<Int>,
    val rentetilleggSomInnkrevesBelop: Expression<Int>,
    val skattefradragSomInnkrevesTotalbelop: Expression<Int>
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(columnSpan = 3) {
                        text(bokmal { + "Beløp som skal kreves tilbake i hele feilutbetalingsperioden" })
                    }
                    column(columnSpan = 1, alignment = RIGHT) {}
                }
            ) {
                row {
                    cell {
                        text(bokmal { + "Brutto tilbakekreving" })
                    }
                    cell {
                        text(bokmal { +bruttoTilbakekrevdTotalbelop.format(CurrencyFormat) + " kr " })
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { + "- Fradrag skatt" },
                        )
                    }
                    cell {
                        text(bokmal { +skattefradragSomInnkrevesTotalbelop.format(CurrencyFormat) + " kr " })
                    }
                }

                row {
                    cell {
                        text(
                            bokmal { + "Netto tilbakekreving" },
                            fontType = FontType.BOLD,
                        )
                    }
                    cell {
                        text(bokmal { +nettoTilbakekrevdTotalbelop.format(CurrencyFormat) + " kr " })
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { + "+ Rentetillegg" },
                        )
                    }
                    cell {
                        text(bokmal { +rentetilleggSomInnkrevesBelop.format(CurrencyFormat) + " kr " })
                    }
                }
            }
        }
    }
}

private data class TilbakekrevingerTabell(
    val tilbakekreving: Expression<List<OversiktOverFeilutbetalingPEDto.Tilbakekreving>>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        forEach(tilbakekreving) { tilbakekreves ->
            paragraph {
                table(
                    header = {
                        column(columnSpan = 3) {
                            text(
                                bokmal { + tilbakekreves.manedOgAr.formatMonthYear() + " - " },
                            )
                            includePhrase(
                                KonteringTypeYtelseTextMappingStorBokstav(
                                    ytelsenMedFeilutbetaling = tilbakekreves.ytelsenMedFeilutbetaling
                                )
                            )
                            text(
                                bokmal { + " - " },
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
                                bokmal { + "Feilutbetalt beløp" },
                            )
                        }
                        cell {
                            text(bokmal { +tilbakekreves.feilutbetaltBelop.format(CurrencyFormat) + " kr " })
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Brutto tilbakekreving" },
                            )
                        }
                        cell {
                            text(bokmal { +tilbakekreves.bruttobelopTilbakekrevd.format(CurrencyFormat) + " kr " })
                        }
                    }
                    showIf(tilbakekreves.skattefradragSomInnkreves.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { + "Fradrag skatt" },
                                )
                            }
                            cell {
                                text(bokmal { +tilbakekreves.skattefradragSomInnkreves.format(CurrencyFormat) + " kr " })
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Netto tilbakekreving" },
                            )
                        }
                        cell {
                            text(bokmal { +tilbakekreves.nettobelopUtenRenterTilbakekrevd.format(CurrencyFormat) + " kr " })
                        }
                    }
                }
            }
        }
    }
}
