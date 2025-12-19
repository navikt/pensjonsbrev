package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.usecase.Result
import no.nav.pensjon.brev.skribenten.model.NavIdent

class RedigerBrevPolicy {
    fun kanRedigere(brev: Brevredigering, saksbehandler: NavIdent): Result<Boolean, BrevedigeringError> {
        return when {
            brev.laastForRedigering -> Result.Companion.failure(KanIkkeRedigere.LaastBrev)
            brev.journalpostId != null -> Result.Companion.failure(KanIkkeRedigere.ArkivertBrev(brev.journalpostId!!))
            brev.redigeresAv != saksbehandler -> Result.Companion.failure(KanIkkeRedigere.IkkeReservert)
            else -> Result.Companion.success(true)
        }
    }

    sealed interface KanIkkeRedigere : BrevedigeringError {
        data object LaastBrev : KanIkkeRedigere
        data class ArkivertBrev(val journalpostId: Long) : KanIkkeRedigere
        data object IkkeReservert : KanIkkeRedigere
    }
}