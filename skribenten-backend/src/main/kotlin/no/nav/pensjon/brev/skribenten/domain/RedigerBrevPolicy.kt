package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class RedigerBrevPolicy {
    fun kanRedigere(brev: Brevredigering, principal: UserPrincipal): Outcome<Boolean, BrevredigeringError> {
        return when {
            brev.journalpostId != null -> failure(KanIkkeRedigere.ArkivertBrev(brev.journalpostId!!))
            brev.laastForRedigering && !principal.isAttestant() -> failure(KanIkkeRedigere.LaastBrev)
            brev.redigeresAv != principal.navIdent -> failure(KanIkkeRedigere.IkkeReservert)
            else -> success(true)
        }
    }

    sealed interface KanIkkeRedigere : BrevredigeringError {
        data object LaastBrev : KanIkkeRedigere
        data class ArkivertBrev(val journalpostId: Long) : KanIkkeRedigere
        data object IkkeReservert : KanIkkeRedigere
    }
}