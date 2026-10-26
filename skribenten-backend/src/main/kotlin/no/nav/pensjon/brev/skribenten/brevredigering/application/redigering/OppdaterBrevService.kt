package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.BrevmalendringType
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.letter.variablesMissingIn
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage

class OppdaterBrevService(private val brevmalService: BrevmalService) {
    suspend fun oppdaterBrevmal(
        brev: Brevredigering,
        rendretBrev: LetterMarkupWithDataUsage,
        pesysdata: BrevdataResponse.Data,
    ): BrevmalendringType {
        val originalBrevmalHash = brev.brevmalHash
        val rendretBrevmalHash = rendretBrev.stableHashCode
        // Mangler en av hashene vet vi ikke om malen er endret, og behandler det som uendret.
        // TODO: Fjern null-håndteringen for originalBrevmalHash når vi har definert det på alle kladder
        val endretHash = originalBrevmalHash != null && rendretBrevmalHash != null && rendretBrevmalHash != originalBrevmalHash
        val harVariablerSomIkkeKanOppdateres = brev.rendretBrev?.variablesMissingIn(rendretBrev.markup)?.isNotEmpty() ?: false

        if (!endretHash || !Features.isEnabled(Features.sjekkFoerOppdaterKladd)) {
            brev.mergeRendretBrev(rendretBrev.markup)
            brev.mergeRendredeVedlegg(brevmalService.renderRedigerteVedlegg(brev, pesysdata))
            rendretBrevmalHash?.let { brev.settBrevmalHash(it) }
        } else {
            brev.oppdaterVariabler(rendretBrev.markup)
            brev.oppdaterVariablerIVedlegg(brevmalService.renderRedigerteVedlegg(brev, pesysdata))
        }

        return when {
            endretHash -> BrevmalendringType.MARKUP_ENDRET
            harVariablerSomIkkeKanOppdateres -> BrevmalendringType.KUNNE_IKKE_OPPDATERE_ALLE_VARIABLER
            else -> BrevmalendringType.INGEN_ENDRING
        }
    }
}