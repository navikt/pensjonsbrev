package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.services.PdlServiceStub
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SlettBrevPolicyTest {

    private data class BrevStub(override val journalpostId: JournalpostId?) : BrevredigeringStub()

    private class PdlServiceFake(private val personContext: Pdl.PersonContext?) : PdlServiceStub() {
        override suspend fun hentBrukerContext(ident: BrevbakerType.Pid, behandlingsnumre: List<Behandlingsnummer>): Pdl.PersonContext? =
            personContext
    }

    private val pid = BrevbakerType.Pid("12345678910")
    private val behandlingsnumre = listOf(Behandlingsnummer("ABC123"))

    private fun policyMedPersonContext(personContext: Pdl.PersonContext?) = SlettBrevPolicy(PdlServiceFake(personContext))

    @Test
    suspend fun `uarkivert brev kan slettes selv uten doedsdato`() {
        val brev = BrevStub(journalpostId = null)
        val policy = policyMedPersonContext(personContext = Pdl.PersonContext(adressebeskyttelse = false, doedsdato = null))

        assertThat(policy.kanSlette(brev, pid, behandlingsnumre)).isSuccess()
    }

    @Test
    suspend fun `arkivert brev uten doedsdato kan ikke slettes`() {
        val journalpostId = JournalpostId(1)
        val brev = BrevStub(journalpostId = journalpostId)
        val policy = policyMedPersonContext(personContext = Pdl.PersonContext(adressebeskyttelse = false, doedsdato = null))

        assertThat(policy.kanSlette(brev, pid, behandlingsnumre))
            .isFailure<SlettBrevPolicy.KanIkkeSlette.ArkivertBrev, Unit, BrevredigeringError> {
                assertThat(it.journalpostId).isEqualTo(journalpostId)
            }
    }

    @Test
    suspend fun `arkivert brev uten brukerkontekst i pdl kan ikke slettes`() {
        val journalpostId = JournalpostId(2)
        val brev = BrevStub(journalpostId = journalpostId)
        val policy = policyMedPersonContext(personContext = null)

        assertThat(policy.kanSlette(brev, pid, behandlingsnumre))
            .isFailure<SlettBrevPolicy.KanIkkeSlette.ArkivertBrev, Unit, BrevredigeringError> {
                assertThat(it.journalpostId).isEqualTo(journalpostId)
            }
    }

    @Test
    suspend fun `arkivert brev med doedsdato kan slettes`() {
        val brev = BrevStub(journalpostId = JournalpostId(3))
        val policy = policyMedPersonContext(
            personContext = Pdl.PersonContext(adressebeskyttelse = false, doedsdato = LocalDate.of(2024, 1, 1))
        )

        assertThat(policy.kanSlette(brev, pid, behandlingsnumre)).isSuccess()
    }
}
