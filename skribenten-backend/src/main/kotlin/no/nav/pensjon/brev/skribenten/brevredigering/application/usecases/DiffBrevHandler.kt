package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.DiffSegment
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.diffBrev
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.BrevId


class DiffBrevHandler(
    private val brevdataService: BrevdataService,
    private val brevmalService: BrevmalService,
) : BrevredigeringHandler<DiffBrevHandler.Request, DiffBrevHandler.Response> {

    data class Response(val inserts: List<DiffSegment>, val deletes: List<DiffSegment>, val rendretBrev: Edit.Letter)
    data class Request(override val brevId: BrevId, val redigertBrev: Edit.Letter) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Response, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)

        return diffBrev(request.redigertBrev, rendretBrev.markup).let { (inserts, deletes) ->
            success(Response(inserts = inserts, deletes = deletes, rendretBrev = rendretBrev.markup.toEdit()))
        }
    }

    override fun requiresReservasjon(request: Request) = false
}