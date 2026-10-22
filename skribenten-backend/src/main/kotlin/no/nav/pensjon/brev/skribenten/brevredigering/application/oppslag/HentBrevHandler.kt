package no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.BrevScope
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.BrevmalendringType
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.letter.variablesValueMap
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class HentBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val reserverForRedigering: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        if (!request.reserverForRedigering) {
            brevtilgang.forLesing(request.brevId, request.saksId) { success(brev.tilDto()) }
        } else {
            brevtilgang.forRedigering(request.brevId, request.saksId, frigiReservasjon = false) {
                val pesysdata = brevdataService.hentBrevdata(brev)
                val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)

                val brevmalendringType = oppdaterBrevmal(rendretBrev, pesysdata)

                success(brev.tilDto(rendretBrev.letterDataUsage, brevmalEndret = brevmalendringType))
            }
        }

    private suspend fun BrevScope.oppdaterBrevmal(
        rendretBrev: LetterMarkupWithDataUsage,
        pesysdata: BrevdataResponse.Data,
    ): BrevmalendringType {
        val originalBrevmalHash = brev.brevmalHash
        val endretHash = rendretBrev.stableHashCode != originalBrevmalHash

        if (originalBrevmalHash == null || !endretHash || !Features.isEnabled(Features.sjekkFoerOppdaterKladd)) {
            // TODO: Fjern brevmalHash-null-sjekken i if-klausulen når vi har definert det på alle kladder
            brev.mergeRendretBrev(rendretBrev.markup)
            brev.mergeRendredeVedlegg(brevmalService.renderRedigerteVedlegg(brev, pesysdata))
            rendretBrev.stableHashCode?.let { brev.settBrevmalHash(it) }
        } else {
            brev.oppdaterVariabler(rendretBrev.markup)
            brev.oppdaterVariablerIVedlegg(brevmalService.renderRedigerteVedlegg(brev, pesysdata))
        }

        val eksisterendeVariabler = brev.rendretBrev?.variablesValueMap()?.keys?.sorted() ?: emptyList()
        val nyeVariabler = rendretBrev.finnVariablerBrukt()

        val brevmalendringType = when {
            endretHash -> BrevmalendringType.MARKUP_ENDRET
            eksisterendeVariabler != nyeVariabler -> BrevmalendringType.KUNNE_IKKE_OPPDATERE_ALLE_VARIABLER
            else -> BrevmalendringType.INGEN_ENDRING
        }
        return brevmalendringType
    }

    private fun LetterMarkupWithDataUsage.finnVariablerBrukt(): List<Int> = markup.blocks
        .asSequence()
        .filterIsInstance<LetterMarkup.Block.Paragraph>()
        .flatMap { it.content }
        .filterIsInstance<LetterMarkup.ParagraphContent.Text.Variable>()
        .map { it.id }
        .sorted()
        .toList()
}
