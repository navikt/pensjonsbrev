package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.asV1
import no.nav.pensjon.brev.skribenten.brevredigering.domain.blocks
import no.nav.pensjon.brev.skribenten.brevredigering.domain.gjelderNavn
import no.nav.pensjon.brev.skribenten.brevredigering.domain.navAvsenderEnhet
import no.nav.pensjon.brev.skribenten.brevredigering.domain.title
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class HentBrevAttesteringHandlerTest : BrevredigeringHandlerTestBase() {

    @BeforeEach
    fun stageV2() = stageMarkupV2()

    private suspend fun hentBrevAttestering(
        brevId: BrevId,
        reserverForRedigering: Boolean = false,
        principal: MockPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        withPrincipal(principal) {
            hentBrevAttestering(
                HentBrevAttesteringHandler.Request(
                    brevId = brevId,
                    saksId = sak1.saksId,
                    reserverForRedigering = reserverForRedigering
                )
            )
        }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan hente brev uten reservasjon`(versjon: MarkupVersjon) {
        val saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) }
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            saksbehandlerValg = saksbehandlerValg,
            markupVersjon = versjon,
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

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestant kan hente brev for attestering`(versjon: MarkupVersjon) {
        val saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) }
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            saksbehandlerValg = saksbehandlerValg,
            principal = saksbehandler1Principal,
            markupVersjon = versjon,
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
            assertThat(it.redigertBrev).isEqualTo(rendretVedOpprettelse(versjon).withSignaturAttestant(attestant1Principal.fullName))
            assertThat(it.info.redigeresAv).isEqualTo(attestant1Principal.navIdent)
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestant kan hente kladd for attestering`(versjon: MarkupVersjon) {
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            principal = saksbehandler1Principal,
            markupVersjon = versjon,
        ).resultOrFail()

        val hentet = hentBrevAttestering(
            brevId = opprettet.info.id,
            reserverForRedigering = true,
            principal = attestant1Principal,
        )

        assertThat(hentet).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.KLADD)
            assertThat(it.info.redigeresAv).isEqualTo(attestant1Principal.navIdent)
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke hente eget brev for attestering`(versjon: MarkupVersjon) {
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            principal = attestant1Principal,
            markupVersjon = versjon,
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

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke hente brev for attestering hvis bruker ikke har attestantrolle`(versjon: MarkupVersjon) {
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            principal = saksbehandler1Principal,
            markupVersjon = versjon,
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

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `annen attestant kan ikke hente allerede attestert brev for attestering`(versjon: MarkupVersjon) {
        val opprettet = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = false,
            principal = saksbehandler1Principal,
            markupVersjon = versjon,
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

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `hent brev for attestering merger ikke inn rendret brev, men oppdaterer sakspart og malstyrt signatur`(versjon: MarkupVersjon) {
        val opprettet = opprettBrev(
            vedtaksId = VedtaksId(1234),
            brevkode = Testbrevkoder.VEDTAKSBREV,
            reserverForRedigering = false,
            markupVersjon = versjon,
        ).resultOrFail()
        veksleKlarStatus(opprettet, klar = true, principal = saksbehandler1Principal).resultOrFail()

        stagEndretMal()

        val hentet = hentBrevAttestering(
            brevId = opprettet.info.id,
            reserverForRedigering = true,
            principal = attestant1Principal
        )

        assertThat(hentet).isSuccess {
            assertThat(it.redigertBrev.blocks()).isEqualTo(opprettet.redigertBrev.blocks())
            assertThat(it.redigertBrev.title()).isEqualTo(opprettet.redigertBrev.title())
            assertThat(it.redigertBrev.gjelderNavn()).isEqualTo("Nytt Navn")
            assertThat(it.redigertBrev.navAvsenderEnhet()).isEqualTo("Ny avsenderenhet")
            assertThat(it.redigertBrev.saksbehandlerNavn).isEqualTo(opprettet.redigertBrev.saksbehandlerNavn)
            assertThat(it.redigertBrev.attesterendeSaksbehandlerNavn).isEqualTo(attestant1Principal.fullName)
            if (versjon == MarkupVersjon.V1) {
                assertThat(it.redigertBrev.asV1().signatur.hilsenTekst).isEqualTo("Ny hilsen")
            }
        }
    }

    @Test
    suspend fun `returnerer null når brev ikke finnes`() {
        val hentet = hentBrevAttestering(brevId = BrevId(9999), reserverForRedigering = false)

        assertThat(hentet).isNull()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `allerede reservert brev kan ikke resereveres for redigering`(versjon: MarkupVersjon) {
        val brev = opprettBrev(
            reserverForRedigering = true,
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            markupVersjon = versjon,
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
