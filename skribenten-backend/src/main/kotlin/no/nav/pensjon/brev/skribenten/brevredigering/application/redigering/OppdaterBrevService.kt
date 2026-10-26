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
        val endretHash = rendretBrev.stableHashCode != originalBrevmalHash
        val harVariablerSomIkkeKanOppdateres = brev.rendretBrev?.variablesMissingIn(rendretBrev.markup)?.isNotEmpty() ?: false

        if (originalBrevmalHash == null || !endretHash || !Features.isEnabled(Features.sjekkFoerOppdaterKladd)) {
            // TODO: Fjern brevmalHash-null-sjekken i if-klausulen når vi har definert det på alle kladder
            brev.mergeRendretBrev(rendretBrev.markup)
            brev.mergeRendredeVedlegg(brevmalService.renderRedigerteVedlegg(brev, pesysdata))
            rendretBrev.stableHashCode?.let { brev.settBrevmalHash(it) }
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