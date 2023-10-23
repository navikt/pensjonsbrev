package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.bruttoTilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.fradragSkatt
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.nettoTilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.renteTillegg
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.summer

@TemplateModelHelpers
val tilbakekrevingVedlegg = createAttachment<LangBokmalNynorskEnglish, TilbakekrevingInnholdDTO>(
    title = newText(
            Bokmal to "Oversikt over feiltutbetalinger",
            Nynorsk to "Oversikt over feiltutbetalinger",
            English to "Oversikt over feiltutbetalinger",
    ),
    includeSakspart = false
) {
    title2 {
        text(
            Bokmal to "Oversikt over feiltutbetalinger",
            Nynorsk to "Oversikt over feiltutbetalinger",
            English to "Oversikt over feiltutbetalinger",
        )
    }
    paragraph {
        table(
            header = {
                column(1) {
                    text(
                        Bokmal to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
                        Nynorsk to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
                        English to "Beløp som skal kreves tilbake i hele feilutbetalingsperioden",
                    )
                }
                column(2) {}
            }
        ) {
            row {
                cell {
                    text(
                        Bokmal to "Brutto tilbakekreving",
                        Nynorsk to "Brutto tilbakekreving",
                        English to "Brutto tilbakekreving",
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(summer.bruttoTilbakekreving))
                }
            }
            row {
                cell {
                    text(
                        Bokmal to "- fradrag skatt",
                        Nynorsk to "- fradrag skatt",
                        English to "- fradrag skatt"
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(summer.fradragSkatt))
                }
            }
            row {
                cell {
                    text(
                        Bokmal to "Netto tilbakekreving",
                        Nynorsk to "Netto tilbakekreving",
                        English to "Netto tilbakekreving",
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(summer.nettoTilbakekreving))
                }
            }
            row {
                cell {
                    text(
                        Bokmal to "- Rentetillegg",
                        Nynorsk to "- Rentetillegg",
                        English to "- Rentetillegg"
                    )
                }
                cell {
                    includePhrase(Felles.KronerText(summer.renteTillegg))
                }
            }
        }
    }
    paragraph {
        // TODO EY-2806 - tabell med alle peridoer/måneder
    }
}
