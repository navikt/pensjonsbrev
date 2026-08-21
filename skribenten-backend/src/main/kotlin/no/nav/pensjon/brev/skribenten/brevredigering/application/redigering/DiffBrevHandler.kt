package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.DiffSegment
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.EditLetterWordDiff
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.BlockEdit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId

class DiffBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevdataService: BrevdataService,
    private val brevmalService: BrevmalService,
) {

    sealed class Response {
        data class Unified(
            val editedBlocks: Map<Int, BlockEdit>,
            val deletedBlocks: Map<Int, List<Edit.Block>>,
        ) : Response()

        data class Split(
            val inserts: List<DiffSegment>,
            val deletes: List<DiffSegment>,
            val rendretBrev: Edit.Letter,
        ) : Response()
    }

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val redigertBrev: Edit.Letter,
        val split: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Response, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            val pesysdata = brevdataService.hentBrevdata(brev)
            val rendretBrev = brevmalService.renderMarkup(brev, pesysdata).markup.toEdit()

            val wordDiff = EditLetterWordDiff()
            if (request.split) {
                wordDiff.diff(old = rendretBrev, new = request.redigertBrev).let { diff ->
                    success(Response.Split(inserts = diff.inserts, deletes = diff.deletes, rendretBrev = rendretBrev))
                }
            } else {
                wordDiff.unifiedDiff(old = rendretBrev, new = request.redigertBrev).let { diff ->
                    success(Response.Unified(editedBlocks = diff.editedBlocks, deletedBlocks = diff.deletedBlocks))
                }
            }
        }
}
