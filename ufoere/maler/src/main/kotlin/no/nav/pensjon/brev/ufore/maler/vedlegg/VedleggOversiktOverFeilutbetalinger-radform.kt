package no.nav.pensjon.brev.ufore.maler.vedlegg

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
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
val oversiktOverFeilutbetalingerPaRadform: AttachmentTemplate<LangBokmalNynorsk, OversiktOverFeilutbetalingPEDto> =
    createAttachment(
        title = {
            text(bokmal { +"Oversikt over feilutbetalinger" },
                nynorsk { +"Oversikt over feilutbetalingar" }) },
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
                nynorsk { +"Detaljert oversikt over perioder med feilutbetalingar" },
            )
        }
        ifNotNull(feilutbetalingPerArListe) { feilutbetalingPerArListe ->

            paragraph {
                text(
                    bokmal { +"Alle beløp er i norske kroner. Eventuelle renter kommer i tillegg. Se " },
                    nynorsk { +"Alle beløp er i norske kroner. Eventuelle renter kjem i tillegg. Sjå " })
                text(bokmal { +"rentetillegg " }, nynorsk { +"rentetillegg " }, fontType = FontType.ITALIC)
                text(bokmal { +"I tabellen over" }, nynorsk { +"I tabellen over" })
            }
            forEach(feilutbetalingPerArListe) { feilutbetalingPerAr ->
                title2 {
                    text(
                        bokmal { +"Detaljert oversikt over feilutbetalinger i " + feilutbetalingPerAr.ar.format() },
                        nynorsk { +"Detaljert oversikt over feilutbetalingar i " + feilutbetalingPerAr.ar.format() }
                    )
                }
                paragraph {
                    table(
                        header = {
                            column {
                                text(bokmal { + "Måned" }, nynorsk { + "Månad" })
                            }
                            column {
                                text(bokmal { + "Resultat" }, nynorsk { + "Resultat" })
                            }
                            column(alignment = RIGHT) {
                                text(bokmal { + "Utbetaling før skatt" }, nynorsk { + "Utbetaling før skatt" })
                            }
                            column(alignment = RIGHT) {
                                text(bokmal { + "Feilutbetalt beløp" }, nynorsk { + "Feilutbetalt beløp" })
                            }
                            column(alignment = RIGHT) {
                                text(bokmal { + "Sum vi krever tilbake fra skatteetaten" }, nynorsk { + "Sum vi krev tilbake frå skatteetaten" })
                            }
                            column(alignment = RIGHT) {
                                text(bokmal { + "Sum vi krever tilbake fra deg" }, nynorsk { + "Sum vi krev tilbake frå deg" })
                            }
                        }
                    ) {
                        forEach(feilutbetalingPerAr.feilutbetalingManed) { feilutbetalingManed ->
                            row {
                                cell {
                                    text(bokmal { + feilutbetalingManed.maned.format() }, nynorsk { + feilutbetalingManed.maned.format() })
                                }
                                cell {
                                    ifNotNull(feilutbetalingManed.resultat) {resultat ->
                                        includePhrase(ResultatAvVurderingenTextMappingStorBokstav(resultat))
                                    }.orShow { text(bokmal { + "-" }, nynorsk { + "-" }) }

                                }
                                cell {
                                    text(bokmal { + feilutbetalingManed.opprinneligBrutto.format(CurrencyFormat) }, nynorsk { + feilutbetalingManed.opprinneligBrutto.format(CurrencyFormat) })
                                }
                                cell {
                                    text(bokmal { + feilutbetalingManed.feilutbetaltBelop.format(CurrencyFormat) }, nynorsk { + feilutbetalingManed.feilutbetaltBelop.format(CurrencyFormat) })
                                }
                                cell {
                                    text(bokmal { + feilutbetalingManed.skatt.format(CurrencyFormat) + " kr" }, nynorsk { + feilutbetalingManed.skatt.format(CurrencyFormat) + " kr" })
                                }
                                cell {
                                    text(bokmal { + feilutbetalingManed.nettobelop.format(CurrencyFormat) + " kr" }, nynorsk { + feilutbetalingManed.nettobelop.format(CurrencyFormat) + " kr" }, fontType = FontType.BOLD)
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
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(columnSpan = 3) {
                        text(
                            bokmal { + "Beløp som skal kreves tilbake i hele feilutbetalingsperioden" },
                            nynorsk { + "Beløp som skal krevast tilbake i heile feilutbetalingsperioden" }
                        )
                    }
                    column(columnSpan = 1, alignment = RIGHT) {}
                }
            ) {
                row {
                    cell {
                        text(bokmal { + "Brutto tilbakekreving" }, nynorsk { + "Brutto tilbakekreving" })
                    }
                    cell {
                        text(bokmal { +bruttoTilbakekrevdTotalbelop.format(CurrencyFormat) + " kr " }, nynorsk { +bruttoTilbakekrevdTotalbelop.format(CurrencyFormat) + " kr " })
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { + "- Fradrag skatt" }, nynorsk { + "- Fradrag skatt" },
                        )
                    }
                    cell {
                        text(bokmal { +skattefradragSomInnkrevesTotalbelop.format(CurrencyFormat) + " kr " }, nynorsk { +skattefradragSomInnkrevesTotalbelop.format(CurrencyFormat) + " kr " })
                    }
                }

                row {
                    cell {
                        text(
                            bokmal { + "Netto tilbakekreving" },
                            nynorsk { + "Netto tilbakekreving" },
                            fontType = FontType.BOLD,
                        )
                    }
                    cell {
                        text(bokmal { +nettoTilbakekrevdTotalbelop.format(CurrencyFormat) + " kr " }, nynorsk { +nettoTilbakekrevdTotalbelop.format(CurrencyFormat) + " kr " })
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { + "+ Rentetillegg" }, nynorsk { + "+ Rentetillegg" },
                        )
                    }
                    cell {
                        text(bokmal { + rentetilleggSomInnkrevesBelop.format(CurrencyFormat) + " kr " }, nynorsk { + rentetilleggSomInnkrevesBelop.format(CurrencyFormat) + " kr " })
                    }
                }
            }
        }
    }
}
