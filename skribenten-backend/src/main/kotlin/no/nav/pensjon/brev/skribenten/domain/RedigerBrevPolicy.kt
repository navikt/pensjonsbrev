package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.usecase.Result

class RedigerBrevPolicy {
    fun kanRedigere(brev: Brevredigering, principal: UserPrincipal): Result<Boolean, BrevedigeringError> {
        return when {
            brev.journalpostId != null -> Result.failure(KanIkkeRedigere.ArkivertBrev(brev.journalpostId!!))
            brev.laastForRedigering && !principal.isAttestant() -> Result.failure(KanIkkeRedigere.LaastBrev)
            brev.redigeresAv != principal.navIdent -> Result.failure(KanIkkeRedigere.IkkeReservert)
            else -> Result.success(true)
        }
    }

    sealed interface KanIkkeRedigere : BrevedigeringError {
        data object LaastBrev : KanIkkeRedigere
        data class ArkivertBrev(val journalpostId: Long) : KanIkkeRedigere
        data object IkkeReservert : KanIkkeRedigere
    }
}