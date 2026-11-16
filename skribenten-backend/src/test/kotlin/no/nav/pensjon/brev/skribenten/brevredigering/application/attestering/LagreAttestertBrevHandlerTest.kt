package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.asV1
import no.nav.pensjon.brev.skribenten.brevredigering.domain.blocks
import no.nav.pensjon.brev.skribenten.brevredigering.domain.gjelderNavn
import no.nav.pensjon.brev.skribenten.brevredigering.domain.medEndretFoersteLiteral
import no.nav.pensjon.brev.skribenten.brevredigering.domain.navAvsenderEnhet
import no.nav.pensjon.brev.skribenten.brevredigering.domain.title
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class LagreAttestertBrevHandlerTest : BrevredigeringHandlerTestBase() {

    @BeforeEach
    fun stageV2() = stageMarkupV2()

    private suspend fun klartVedtaksbrev(versjon: MarkupVersjon): Dto.Brevredigering =
        opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234), markupVersjon = versjon).resultOrFail()
            .also { veksleKlarStatus(it, klar = true).resultOrFail() }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestant kan lagre endringer uten aa attestere`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrev(versjon)
        val endret = brev.redigertBrev.medEndretFoersteLiteral("attestanten sin tekst")

        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = endret)).isSuccess {
            assertThat(it.redigertBrev.markupVersjon).isEqualTo(versjon)
            assertThat(it.redigertBrev.blocks()).isEqualTo(endret.blocks())
            assertThat(it.info.attestertAv).isNull()
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.ATTESTERING)
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `lagring merger ikke inn endringer fra malen, men oppdaterer sakspart og malstyrt signatur`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrev(versjon)
        val endret = brev.redigertBrev.medEndretFoersteLiteral("attestanten sin tekst")

        stagEndretMal()

        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = endret)).isSuccess {
            assertThat(it.redigertBrev.blocks()).isEqualTo(endret.blocks())
            assertThat(it.redigertBrev.title()).isEqualTo(endret.title())
            assertThat(it.redigertBrev.gjelderNavn()).isEqualTo("Nytt Navn")
            assertThat(it.redigertBrev.navAvsenderEnhet()).isEqualTo("Ny avsenderenhet")
            assertThat(it.redigertBrev.saksbehandlerNavn).isEqualTo(endret.saksbehandlerNavn)
            if (versjon == MarkupVersjon.V1) {
                assertThat(it.redigertBrev.asV1().signatur.hilsenTekst).isEqualTo("Ny hilsen")
            }
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `lagringen endrer ikke saksbehandlervalg`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrev(versjon)

        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev)).isSuccess {
            assertThat(it.saksbehandlerValg).isEqualTo(brev.saksbehandlerValg)
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `frigir reservasjon naar frigiReservasjon er true`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrev(versjon)

        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev, frigiReservasjon = false)).isSuccess {
            assertThat(it.info.redigeresAv).isEqualTo(attestant1Principal.navIdent)
        }
        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev, frigiReservasjon = true)).isSuccess {
            assertThat(it.info.redigeresAv).isNull()
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke lagre uten attestantrolle`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrev(versjon)

        val resultat = lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev, attestant = saksbehandler2Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.HarIkkeAttestantrolle, _, _>()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke lagre eget brev som attestant`(versjon: MarkupVersjon) {
        val brev = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            principal = attestant1Principal,
            markupVersjon = versjon,
        ).resultOrFail()
        veksleKlarStatus(brev, klar = true, principal = attestant1Principal).resultOrFail()

        val resultat = lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereEgetBrev, _, _>()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `returnerer null hvis brev ikke finnes`(versjon: MarkupVersjon) {
        val resultat = withPrincipal(attestant1Principal) {
            lagreAttestertBrev(
                LagreAttestertBrevHandler.Request(
                    brevId = BrevId(-9999L),
                    saksId = sak1.saksId,
                    nyttRedigertbrev = rendretVedOpprettelse(versjon),
                )
            )
        }

        assertThat(resultat).isNull()
    }
}
