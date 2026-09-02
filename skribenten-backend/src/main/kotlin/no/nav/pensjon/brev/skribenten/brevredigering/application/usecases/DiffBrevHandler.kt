package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
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
import org.jetbrains.exposed.v1.jdbc.Database


class DiffBrevHandler(
    private val brevdataService: BrevdataService,
    private val brevmalService: BrevmalService,
    database: Database,
) : TransactionHandler<DiffBrevHandler.Request, DiffBrevHandler.Response, Nothing>(database) {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = Response.Unified::class, name = "UNIFIED"),
        JsonSubTypes.Type(value = Response.Split::class, name = "SPLIT"),
    )
    sealed class Response(val type: Type) {
        enum class Type {
            UNIFIED, SPLIT,
        }

        data class Unified(
            val editedBlocks: Map<Int, BlockEdit>,
            val deletedBlocks: Map<Int, List<Edit.Block>>,
        ): Response(Type.UNIFIED)

        data class Split(
            val inserts: List<DiffSegment>,
            val deletes: List<DiffSegment>,
            val rendretBrev: Edit.Letter,
        ): Response(Type.SPLIT)
    }

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val redigertBrev: Edit.Letter,
        val split: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Response, Nothing>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = brevmalService.renderMarkup(brev, pesysdata).markup.toEdit()

        val wordDiff = EditLetterWordDiff()
        return if (request.split) {
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
