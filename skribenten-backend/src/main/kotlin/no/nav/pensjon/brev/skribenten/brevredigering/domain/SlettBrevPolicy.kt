package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brevbaker.api.model.BrevbakerType

class SlettBrevPolicy(private val pdlService: PdlService) {
    suspend fun kanSlette(
        brev: Brevredigering,
        pid: BrevbakerType.Pid,
        behandlingsnumre: List<Behandlingsnummer>,
    ): Outcome<Unit, BrevredigeringError> {
        return when {
            brev.journalpostId != null && pdlService.hentBrukerContext(
                pid,
                behandlingsnumre
            )?.doedsdato == null -> failure(KanIkkeSlette.ArkivertBrev(brev.journalpostId!!))
            else -> success(Unit)
        }
    }

    sealed interface KanIkkeSlette : BrevredigeringError {
        data class ArkivertBrev(val journalpostId: JournalpostId) : KanIkkeSlette
    }
}

