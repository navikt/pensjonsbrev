package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HentBrevAttesteringHandlerTest : BrevredigeringTest() {

    private suspend fun hentBrevAttestering(
        brevId: Long,
        reserverForRedigering: Boolean = false,
        principal: MockPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        withPrincipal(principal) {
            brevredigeringFacade.hentBrevAttestering(
                HentBrevAttesteringHandler.Request(
                    brevId = brevId,
                    reserverForRedigering = reserverForRedigering
                )
            )
        }

    @Test
    suspend fun `kan hente brev uten reservasjon`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            saksbehandlerValg = saksbehandlerValg
        ).resultOrFail()

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrevAttestering(brevId = opprettet.info.id, reserverForRedigering = false)

        assertThat(brevbakerService.renderMarkupKall).isEmpty()
        assertThat(hentet).isSuccess {
            assertThat(it.info.id).isEqualTo(opprettet.info.id)
            assertThat(it.saksbehandlerValg).isEqualTo(saksbehandlerValg)
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev)
        }
    }

    @Test
    suspend fun `attestant kan hente brev for attestering`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            saksbehandlerValg = saksbehandlerValg,
            principal = saksbehandler1Principal
        ).resultOrFail()
        veksleKlarStatus(opprettet, klar = true, principal = saksbehandler1Principal).resultOrFail()

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrevAttestering(
            brevId = opprettet.info.id,
            reserverForRedigering = true,
            principal = attestant1Principal
        )

        assertThat(brevbakerService.renderMarkupKall.first()).isEqualTo(Testbrevkoder.VEDTAKSBREV to LanguageCode.ENGLISH)
        assertThat(brevbakerService.renderMarkupKall.size).isEqualTo(1)

        assertThat(hentet).isSuccess {
            assertThat(it.info.id).isEqualTo(opprettet.info.id)
            assertThat(it.saksbehandlerValg).isEqualTo(saksbehandlerValg)
            assertThat(it.redigertBrev).isEqualTo(letter.toEdit().withSignatur(attestant = attestant1Principal.fullName))
            assertThat(it.info.redigeresAv).isEqualTo(attestant1Principal.navIdent)
        }
    }

    @Test
    suspend fun `kan ikke hente eget brev for attestering`() {
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            principal = attestant1Principal
        ).resultOrFail()
        veksleKlarStatus(opprettet, klar = true, principal = attestant1Principal).resultOrFail()

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrevAttestering(
            brevId = opprettet.info.id,
            reserverForRedigering = true,
            principal = attestant1Principal
        )

        assertThat(brevbakerService.renderMarkupKall).hasSize(0)
        assertThat(hentet).isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereEgetBrev, _, _>()
    }

    @Test
    suspend fun `kan ikke hente brev for attestering hvis bruker ikke har attestantrolle`() {
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            principal = saksbehandler1Principal
        ).resultOrFail()
        veksleKlarStatus(opprettet, klar = true, principal = saksbehandler1Principal).resultOrFail()

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrevAttestering(
            brevId = opprettet.info.id,
            reserverForRedigering = true,
            principal = saksbehandler2Principal
        )

        assertThat(brevbakerService.renderMarkupKall).hasSize(0)
        assertThat(hentet).isFailure<AttesterBrevPolicy.KanIkkeAttestere.HarIkkeAttestantrolle, _, _>()
    }

    @Test
    suspend fun `annen attestant kan ikke hente allerede attestert brev for attestering`() {
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            principal = saksbehandler1Principal
        ).resultOrFail()
        veksleKlarStatus(opprettet, klar = true, principal = saksbehandler1Principal).resultOrFail()
        attester(opprettet, attestant = attestant1Principal, frigiReservasjon = true)

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrevAttestering(
            brevId = opprettet.info.id,
            reserverForRedigering = true,
            principal = attestant2Principal
        )

        assertThat(brevbakerService.renderMarkupKall).hasSize(0)
        assertThat(hentet).isFailure<AttesterBrevPolicy.KanIkkeAttestere.AlleredeAttestertAvAnnen, _, _>()
    }

    @Test
    suspend fun `hent brev for attestering med reservasjon merger inn rendret brev`() {
        val opprettet = opprettBrev(
            vedtaksId = VedtaksId(1234),
            brevkode = Testbrevkoder.VEDTAKSBREV,
            reserverForRedigering = false
        ).resultOrFail()
        veksleKlarStatus(opprettet, klar = true, principal = saksbehandler1Principal).resultOrFail()

        val freshRender = letter.copy(
            blocks = letter.blocks + ParagraphImpl(2, true, listOf(VariableImpl(21, "ny paragraph")))
        )
        brevbakerService.renderMarkupResultat = { freshRender }

        val hentet = hentBrevAttestering(
            brevId = opprettet.info.id,
            reserverForRedigering = true,
            principal = attestant1Principal
        )

        assertThat(hentet).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev.updateEditedLetter(freshRender))
        }
    }

    @Test
    suspend fun `returnerer null når brev ikke finnes`() {
        val hentet = hentBrevAttestering(brevId = 9999, reserverForRedigering = false)

        assertThat(hentet).isNull()
    }

    @Test
    suspend fun `allerede reservert brev kan ikke resereveres for redigering`() {
        val brev = opprettBrev(
            reserverForRedigering = true,
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
        ).resultOrFail()

        val hentet = hentBrevAttestering(
            brevId = brev.info.id,
            reserverForRedigering = true,
            principal = attestant1Principal,
        )
        assertThat(hentet).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _> {
            assertThat(it.eksisterende.reservertAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

}
