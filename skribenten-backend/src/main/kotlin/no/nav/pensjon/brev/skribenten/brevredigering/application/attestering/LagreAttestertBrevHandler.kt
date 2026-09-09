package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

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
import no.nav.pensjon.brev.skribenten.model.SaksId

class LagreAttestertBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val nyttRedigertbrev: Edit.Letter,
        val frigiReservasjon: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        brevtilgang.forAttestering(request.brevId, request.saksId, frigiReservasjon = request.frigiReservasjon) {
            brev.oppdaterRedigertBrev(request.nyttRedigertbrev, PrincipalInContext.require().navIdent)

            val pesysdata = brevdataService.hentBrevdata(brev)
            val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)
            brev.oppdaterSakspartOgSignatur(rendretBrev.markup)

            success(brev.tilDto(rendretBrev.letterDataUsage))
        }
}
