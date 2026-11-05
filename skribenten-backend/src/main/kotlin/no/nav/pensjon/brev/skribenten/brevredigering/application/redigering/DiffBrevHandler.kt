package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FeilMarkupVersjonException
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertBrev
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.DiffSegment
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.EditLetterWordDiff
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.BlockEdit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId

class DiffBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevdataService: BrevdataService,
    private val brevmalService: BrevmalService,
) {

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

    sealed interface Resultat {
        data class V1(val response: Response) : Resultat
    }

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val redigertBrev: VersjonertBrev,
        val split: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Resultat, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            val rendretBrev = brev.rendretBrev ?: renderGjeldendeMal(brev)

            when (val redigertBrev = request.redigertBrev) {
                is VersjonertBrev.V1 -> success(Resultat.V1(diffV1(rendretBrev.asV1(), redigertBrev.brev, request.split)))
                is VersjonertBrev.V2 -> throw FeilMarkupVersjonException(MarkupVersjon.V1, MarkupVersjon.V2)
            }
        }

    private fun VersjonertBrev.asV1(): Edit.Letter =
        (this as? VersjonertBrev.V1)?.brev ?: throw FeilMarkupVersjonException(markupVersjon, MarkupVersjon.V1)

    private fun diffV1(rendretBrev: Edit.Letter, redigertBrev: Edit.Letter, split: Boolean): Response {
        val wordDiff = EditLetterWordDiff()
        return if (split) {
            wordDiff.diff(old = rendretBrev, new = redigertBrev).let { diff ->
                Response.Split(inserts = diff.inserts, deletes = diff.deletes, rendretBrev = rendretBrev)
            }
        } else {
            wordDiff.unifiedDiff(old = rendretBrev, new = redigertBrev).let { diff ->
                Response.Unified(editedBlocks = diff.editedBlocks, deletedBlocks = diff.deletedBlocks)
            }
        }
    }

    // Brev opprettet før rendretBrev ble lagret mangler utgangspunkt og faller tilbake til gjeldende mal
    private suspend fun renderGjeldendeMal(brev: Brevredigering): VersjonertBrev =
        brevmalService.renderMarkup(brev, brevdataService.hentBrevdata(brev)).markup.toEdit()
}
