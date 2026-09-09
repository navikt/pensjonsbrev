package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.RedigerbarSaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.mergeInn

class OppdaterBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val nyeSaksbehandlerValg: RedigerbarSaksbehandlervalgMap? = null,
        val nyttRedigertbrev: Edit.Letter? = null,
        val frigiReservasjon: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        brevtilgang.forRedigering(request.brevId, request.saksId, frigiReservasjon = request.frigiReservasjon) {
            if (request.nyeSaksbehandlerValg != null) {
                brev.saksbehandlerValg = brev.saksbehandlerValg.mergeInn(request.nyeSaksbehandlerValg)
            }
            if (request.nyttRedigertbrev != null) {
                brev.oppdaterRedigertBrev(request.nyttRedigertbrev, PrincipalInContext.require().navIdent)
            }

            val pesysdata = brevdataService.hentBrevdata(brev)
            val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)
            brev.mergeRendretBrev(rendretBrev.markup)
            brev.mergeRendredeVedlegg(brevmalService.renderRedigerteVedlegg(brev, pesysdata))

            success(brev.tilDto(rendretBrev.letterDataUsage))
        }
}
