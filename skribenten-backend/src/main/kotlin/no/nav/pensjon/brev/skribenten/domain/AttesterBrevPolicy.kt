package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class AttesterBrevPolicy {

    fun kanAttestere(brev: Brevredigering, principal: UserPrincipal): Outcome<Boolean, KanIkkeAttestere> {
        return when {
            !principal.isAttestant() -> failure(KanIkkeAttestere.HarIkkeAttestantrolle(principal.navIdent))
            brev.opprettetAv == principal.navIdent -> failure(KanIkkeAttestere.KanIkkeAttestereEgetBrev(principal.navIdent, BrevId(brev.id.value)))
            brev.attestertAvNavIdent != null && brev.attestertAvNavIdent != principal.navIdent -> failure(
                KanIkkeAttestere.AlleredeAttestertAvAnnen(BrevId(brev.id.value), brev.attestertAvNavIdent!!)
            )
            else -> success(true)
        }
    }

    sealed interface KanIkkeAttestere : BrevredigeringError {
        data class HarIkkeAttestantrolle(val navIdent: NavIdent) : KanIkkeAttestere
        data class KanIkkeAttestereEgetBrev(val navIdent: NavIdent, val brevId: BrevId) : KanIkkeAttestere
        data class AlleredeAttestertAvAnnen(val brevId: BrevId, val attestertAv: NavIdent) : KanIkkeAttestere
    }
}
