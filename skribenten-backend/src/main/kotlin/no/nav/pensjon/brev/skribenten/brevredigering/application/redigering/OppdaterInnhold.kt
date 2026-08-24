package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.BrevScope
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.RedigerbarSaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.mergeInn
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

/**
 * Tar imot saksbehandlers endringer, renderer brevet på nytt med gjeldende pesysdata og fletter
 * inn resultatet. Deles av oppdatering og attestering.
 */
internal suspend fun BrevScope.oppdaterOgRender(
    nyeSaksbehandlerValg: RedigerbarSaksbehandlervalgMap?,
    nyttRedigertbrev: Edit.Letter?,
    brevdataService: BrevdataService,
    brevmalService: BrevmalService,
): LetterMarkupWithDataUsage {
    if (nyeSaksbehandlerValg != null) {
        brev.saksbehandlerValg = brev.saksbehandlerValg.mergeInn(nyeSaksbehandlerValg)
    }
    if (nyttRedigertbrev != null) {
        brev.oppdaterRedigertBrev(nyttRedigertbrev, PrincipalInContext.require().navIdent)
    }

    val pesysdata = brevdataService.hentBrevdata(brev)
    return brevmalService.renderMarkup(brev, pesysdata).also { brev.mergeRendretBrev(it.markup) }
}
