package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.bruttoTilbakekrevdTotalbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.nettoUtenRenterTilbakekrevdTotalbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.rentetilleggSomInnkrevesTotalbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDtoSelectors.skattefradragSomInnkrevesTotalbeloep
import no.nav.pensjon.brev.maler.fraser.vedlegg.oversiktOverFeilutbetalinger.TilbakekrevingerTabell
import no.nav.pensjon.brev.maler.fraser.vedlegg.oversiktOverFeilutbetalinger.TilbakekrevingerTotalbeloepTabell
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText


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
        TilbakekrevingerTotalbeloepTabell(
            bruttoTilbakekrevdTotalbeloep = bruttoTilbakekrevdTotalbeloep,
            nettoTilbakekrevdTotalbeloep = nettoUtenRenterTilbakekrevdTotalbeloep,
            rentetilleggSomInnkrevesBeloep = rentetilleggSomInnkrevesTotalbeloep,
            skattefradragSomInnkrevesTotalbeloep = skattefradragSomInnkrevesTotalbeloep
        )
    )
    includePhrase(
        TilbakekrevingerTabell(
            tilbakekreving = tilbakekreving,
            ytelsenMedFeilutbetaling = ytelsenMedFeilutbetaling,
            resultatAvVurderingen = resultatAvVurderingen
        )
    )
}