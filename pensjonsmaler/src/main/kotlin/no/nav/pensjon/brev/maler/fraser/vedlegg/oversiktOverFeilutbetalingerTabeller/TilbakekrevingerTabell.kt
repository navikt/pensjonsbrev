package no.nav.pensjon.brev.maler.fraser.vedlegg.oversiktOverFeilutbetalingerTabeller

import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.bruttobeloepTilbakekrevd
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.feilutbetaltBeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.maanedOgAar
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.nettobeloepUtenRenterTilbakekrevd
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.resultatAvVurderingen
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.skattefradragSomInnkreves
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.TilbakekrevingSelectors.ytelsenMedFeilutbetaling
import no.nav.pensjon.brev.maler.fraser.common.KonteringTypeYtelseTextMappingStorBokstav
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.maler.fraser.common.ResultatAvVurderingenTextMappingStorBokstav
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.formatMonthYear
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner


data class TilbakekrevingerTabell(
    val tilbakekreving: Expression<List<OversiktOverFeilutbetalingPEDto.Tilbakekreving>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        forEach(tilbakekreving) { tilbakekreves ->
            paragraph {
                table(
                    header = {
                        column(columnSpan = 2) {
                            textExpr(
                                Bokmal to tilbakekreves.maanedOgAar.formatMonthYear() + " - ",
                                Nynorsk to tilbakekreves.maanedOgAar.formatMonthYear() + " - ",
                                English to tilbakekreves.maanedOgAar.formatMonthYear() + " - "
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
                        column(columnSpan = 0) {}
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
                    row {
                        cell {
                            text(
                                Bokmal to "Fradrag skatt",
                                Nynorsk to "Frådrag skatt",
                                English to "Tax deduction"
                            )
                        }
                        cell {
                            includePhrase(KronerText(tilbakekreves.skattefradragSomInnkreves.ifNull(Kroner(0))))
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
                            includePhrase(KronerText(tilbakekreves.nettobeloepUtenRenterTilbakekrevd))
                        }
                    }
                }
            }
        }
    }
}

