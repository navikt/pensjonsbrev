package no.nav.pensjon.brev.ufore.maler.vedlegg

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.FeilutbetalingManedSelectors.feilutbetaltBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.FeilutbetalingManedSelectors.maned
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.FeilutbetalingManedSelectors.nettobelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.FeilutbetalingManedSelectors.opprinneligBrutto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.FeilutbetalingManedSelectors.resultat
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.FeilutbetalingManedSelectors.skatt
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.FeilutbetalingPerArSelectors.ar
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.FeilutbetalingPerArSelectors.feilutbetalingManed
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.bruttoTilbakekrevdTotalbelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.feilutbetalingPerArListe
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.nettoUtenRenterTilbakekrevdTotalbelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.rentetilleggSomInnkrevesTotalbelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.skattefradragSomInnkrevesTotalbelop
import no.nav.pensjon.brev.ufore.maler.fraser.ResultatAvVurderingenTextMappingStorBokstav

@TemplateModelHelpers
val oversiktOverFeilutbetalingerPaRadform: AttachmentTemplate<LangBokmal, OversiktOverFeilutbetalingPEDto> =
    createAttachment(
        title = { text(bokmal { +"Oversikt over feilutbetalinger" }) },
        includeSakspart = true,
    ) {
        includePhrase(
            TotalbeloepTabell
                (
                bruttoTilbakekrevdTotalbelop = this.bruttoTilbakekrevdTotalbelop,
                nettoTilbakekrevdTotalbelop = nettoUtenRenterTilbakekrevdTotalbelop,
                rentetilleggSomInnkrevesBelop = rentetilleggSomInnkrevesTotalbelop,
                skattefradragSomInnkrevesTotalbelop = this.skattefradragSomInnkrevesTotalbelop
            )
        )
        title1 {
            text(
                bokmal { +"Detaljert oversikt over perioder med feilutbetalinger" },
            )
        }
        ifNotNull(feilutbetalingPerArListe) { feilutbetalingPerArListe ->

            paragraph {
                text(bokmal { +"Alle beløp er i norske kroner. Eventuelle renter kommer i tillegg. Se " })
                text(bokmal { +"rentetillegg " }, FontType.ITALIC)
                text(bokmal { +"I tabellen over" })
            }
            forEach(feilutbetalingPerArListe) { feilutbetalingPerAr ->
                title2 {
                    text(bokmal { +"Detaljert oversikt over feilutbetalinger i " + feilutbetalingPerAr.ar.format() })
                }
                paragraph {
                    table(
                        header = {
                            column {
                                text(bokmal { + "Måned" })
                            }
                            column {
                                text(bokmal { + "Resultat" })
                            }
                            column(alignment = RIGHT) {
                                text(bokmal { + "Utbetaling før skatt" })
                            }
                            column(alignment = RIGHT) {
                                text(bokmal { + "Feilutbetalt beløp" })
                            }
                            column(alignment = RIGHT) {
                                text(bokmal { + "Sum vi krever tilbake fra skatteetaten" })
                            }
                            column(alignment = RIGHT) {
                                text(bokmal { + "Sum vi krever tilbake fra deg" })
                            }
                        }
                    ) {
                        forEach(feilutbetalingPerAr.feilutbetalingManed) { feilutbetalingManed ->
                            row {
                                cell {
                                    text(bokmal { + feilutbetalingManed.maned.format() })
                                }
                                cell {
                                    ifNotNull(feilutbetalingManed.resultat) {resultat ->
                                        includePhrase(ResultatAvVurderingenTextMappingStorBokstav(resultat))
                                    }.orShow { text(bokmal { + "-" }) }

                                }
                                cell {
                                    text(bokmal { + feilutbetalingManed.opprinneligBrutto.format(CurrencyFormat)})
                                }
                                cell {
                                    text(bokmal { + feilutbetalingManed.feilutbetaltBelop.format(CurrencyFormat) })
                                }
                                cell {
                                    text(bokmal { + feilutbetalingManed.skatt.format(CurrencyFormat) + " kr" })
                                }
                                cell {
                                    text(bokmal { + feilutbetalingManed.nettobelop.format(CurrencyFormat) + " kr" }, FontType.BOLD)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


private class TotalbeloepTabell(
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
