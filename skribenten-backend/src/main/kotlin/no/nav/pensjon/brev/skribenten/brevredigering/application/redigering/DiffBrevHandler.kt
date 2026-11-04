package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FeilMarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertBrev
import no.nav.pensjon.brev.skribenten.brevredigering.domain.sjekkMarkupVersjon
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.DiffSegment
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.EditLetterWordDiff
import no.nav.pensjon.brev.skribenten.letter.UnifiedDiff.BlockEdit
import no.nav.pensjon.brev.skribenten.letter.v2.DiffSegmentV2
import no.nav.pensjon.brev.skribenten.letter.v2.EditLetterWordDiffV2
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2
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

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = ResponseV2.Unified::class, name = "UNIFIED"),
        JsonSubTypes.Type(value = ResponseV2.Split::class, name = "SPLIT"),
    )
    sealed class ResponseV2(val type: Response.Type) {
        data class Unified(
            val editedBlocks: Map<Int, UnifiedDiffV2.BlockEdit>,
            val deletedBlocks: Map<Int, List<EditV2.Block>>,
        ): ResponseV2(Response.Type.UNIFIED)

        data class Split(
            val inserts: List<DiffSegmentV2>,
            val deletes: List<DiffSegmentV2>,
            val rendretBrev: EditV2.Letter,
        ): ResponseV2(Response.Type.SPLIT)
    }

    sealed interface Resultat {
        data class V1(val response: Response) : Resultat
        data class V2(val response: ResponseV2) : Resultat
    }

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val redigertBrev: VersjonertBrev,
        val split: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Resultat, FeilMarkupVersjon>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            brev.sjekkMarkupVersjon(request.redigertBrev.markupVersjon)?.let { return@forLesing failure(it) }
            val rendretBrev = brev.rendretBrev ?: renderGjeldendeMal(brev)

            when (val redigertBrev = request.redigertBrev) {
                is VersjonertBrev.V1 -> success(Resultat.V1(diffV1((rendretBrev as VersjonertBrev.V1).brev, redigertBrev.brev, request.split)))
                is VersjonertBrev.V2 -> success(Resultat.V2(diffV2((rendretBrev as VersjonertBrev.V2).brev, redigertBrev.brev, request.split)))
            }
        }

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

    private fun diffV2(rendretBrev: EditV2.Letter, redigertBrev: EditV2.Letter, split: Boolean): ResponseV2 {
        val wordDiff = EditLetterWordDiffV2()
        return if (split) {
            wordDiff.diff(old = rendretBrev, new = redigertBrev).let { diff ->
                ResponseV2.Split(inserts = diff.inserts, deletes = diff.deletes, rendretBrev = rendretBrev)
            }
        } else {
            wordDiff.unifiedDiff(old = rendretBrev, new = redigertBrev).let { diff ->
                ResponseV2.Unified(editedBlocks = diff.editedBlocks, deletedBlocks = diff.deletedBlocks)
            }
        }
    }

    // Brev opprettet før rendretBrev ble lagret mangler utgangspunkt og faller tilbake til gjeldende mal
    private suspend fun renderGjeldendeMal(brev: Brevredigering): VersjonertBrev =
        brevmalService.renderMarkup(brev, brevdataService.hentBrevdata(brev)).markup.toEdit()
}
