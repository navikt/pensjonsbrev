package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.usecase.Outcome

class RedigerBrevPolicy {
    fun kanRedigere(brev: Brevredigering, principal: UserPrincipal): Outcome<Boolean, BrevredigeringError> {
        return when {
            brev.journalpostId != null -> Outcome.failure(KanIkkeRedigere.ArkivertBrev(brev.journalpostId!!))
            brev.laastForRedigering && !principal.isAttestant() -> Outcome.failure(KanIkkeRedigere.LaastBrev)
            brev.redigeresAv != principal.navIdent -> Outcome.failure(KanIkkeRedigere.IkkeReservert)
            else -> Outcome.success(true)
        }
    }

    sealed interface KanIkkeRedigere : BrevredigeringError {
        data object LaastBrev : KanIkkeRedigere
        data class ArkivertBrev(val journalpostId: Long) : KanIkkeRedigere
        data object IkkeReservert : KanIkkeRedigere
    }
}