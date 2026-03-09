package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.JournalpostId

class SlettBrevPolicy {
    fun kanSlette(brev: Brevredigering): Outcome<Unit, BrevredigeringError> {
        return when {
            brev.journalpostId != null -> failure(KanIkkeSlette.ArkivertBrev(brev.journalpostId!!))
            else -> success(Unit)
        }
    }

    sealed interface KanIkkeSlette : BrevredigeringError {
        data class ArkivertBrev(val journalpostId: JournalpostId) : KanIkkeSlette
    }
}

