package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.DiffSegment
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.EditLetterWordDiff
import no.nav.pensjon.brev.skribenten.letter.UnifiedDeleteSegment
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.BrevId


class DiffBrevHandler(
    private val brevdataService: BrevdataService,
    private val brevmalService: BrevmalService,
) : BrevredigeringHandler<DiffBrevHandler.Request, DiffBrevHandler.Response> {

    sealed class Response {
        data class Unified(
            val inserts: List<DiffSegment>,
            val deletes: List<UnifiedDeleteSegment>,
        ) : Response()

        data class Split(
            val inserts: List<DiffSegment>,
            val deletes: List<DiffSegment>,
            val rendretBrev: Edit.Letter,
        ) : Response()
    }

    data class Request(
        override val brevId: BrevId,
        val redigertBrev: Edit.Letter,
        val split: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Response, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = brevmalService.renderMarkup(brev, pesysdata).markup.toEdit()

        val wordDiff = EditLetterWordDiff()
        return if (request.split) {
            wordDiff.diff(old = rendretBrev, new = request.redigertBrev).let { (inserts, deletes) ->
                success(Response.Split(inserts = inserts, deletes = deletes, rendretBrev = rendretBrev))
            }
        } else {
            wordDiff.unifiedDiff(old = rendretBrev, new = request.redigertBrev).let { (inserts, deletes) ->
                success(Response.Unified(inserts = inserts, deletes = deletes))
            }
        }
    }

    override fun requiresReservasjon(request: Request) = false
}
