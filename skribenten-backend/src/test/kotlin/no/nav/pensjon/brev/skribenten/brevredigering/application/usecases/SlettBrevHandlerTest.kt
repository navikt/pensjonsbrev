package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SlettBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SlettBrevHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `kan slette brev`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(slettBrev(brev)).isSuccess()

        // Verify the brev is actually deleted
        assertThat(hentBrev(brev.info.id)).isNull()
    }

    @Test
    suspend fun `kan ikke slette brev som ikke eksisterer`() {
        val result = slettBrev(BrevId(-9999))

        assertThat(result).isNull()
    }

    @Test
    suspend fun `kan ikke slette arkivert brev`() {
        val brev = opprettBrev().resultOrFail()

        arkiverBrev(brev)

        val result = slettBrev(brev)
        assertThat(result).isFailure<SlettBrevPolicy.KanIkkeSlette.ArkivertBrev, Unit, BrevredigeringError> {
            assertThat(it.journalpostId).isEqualTo(penService.sendBrevResponse?.journalpostId!!)
        }

        // Verify the brev still exists
        assertThat(hentBrev(brev.info.id)).isNotNull()
    }

    @Test
    suspend fun `sletting krever reservasjon`() {
        // Create and reserve the brev for saksbehandler1
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        // Try to delete with a different principal (saksbehandler2 doesn't have the reservation)
        val result = slettBrev(brev, principal = saksbehandler2Principal)

        // Should fail because saksbehandler2 doesn't have the reservation
        assertThat(result).isFailure()

        // Verify the brev still exists
        assertThat(hentBrev(brev.info.id)).isNotNull()
    }
}




