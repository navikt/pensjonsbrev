package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LagreAttestertBrevHandlerTest : BrevredigeringHandlerTestBase() {

    private fun Edit.Letter.medEndretFoersteLiteral(nyTekst: String): Edit.Letter {
        val foersteBlokk = blocks.first() as Edit.Block.Paragraph
        val foersteLiteral = foersteBlokk.content.first() as Edit.ParagraphContent.Text.Literal
        return copy(
            blocks = listOf(foersteBlokk.copy(content = listOf(foersteLiteral.copy(editedText = nyTekst)))) + blocks.drop(1)
        )
    }

    private suspend fun klartVedtaksbrev(): Dto.Brevredigering =
        opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()
            .also { veksleKlarStatus(it, klar = true).resultOrFail() }

    @Test
    suspend fun `attestant kan lagre endringer uten aa attestere`() {
        val brev = klartVedtaksbrev()
        val endret = brev.redigertBrev.medEndretFoersteLiteral("attestanten sin tekst")

        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = endret)).isSuccess {
            assertThat(it.redigertBrev.blocks).isEqualTo(endret.blocks)
            assertThat(it.info.attestertAv).isNull()
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.ATTESTERING)
        }
    }

    @Test
    suspend fun `lagring merger ikke inn endringer fra malen, men oppdaterer sakspart og malstyrt signatur`() {
        val brev = klartVedtaksbrev()
        val endret = brev.redigertBrev.medEndretFoersteLiteral("attestanten sin tekst")

        stagEndretMal()

        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = endret)).isSuccess {
            assertThat(it.redigertBrev.blocks).isEqualTo(endret.blocks)
            assertThat(it.redigertBrev.title).isEqualTo(endret.title)
            assertThat(it.redigertBrev.sakspart.gjelderNavn).isEqualTo("Nytt Navn")
            assertThat(it.redigertBrev.signatur.hilsenTekst).isEqualTo("Ny hilsen")
            assertThat(it.redigertBrev.signatur.navAvsenderEnhet).isEqualTo("Ny avsenderenhet")
            assertThat(it.redigertBrev.signatur.saksbehandlerNavn).isEqualTo(endret.signatur.saksbehandlerNavn)
        }
    }

    @Test
    suspend fun `lagringen endrer ikke saksbehandlervalg`() {
        val brev = klartVedtaksbrev()

        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev)).isSuccess {
            assertThat(it.saksbehandlerValg).isEqualTo(brev.saksbehandlerValg)
        }
    }

    @Test
    suspend fun `frigir reservasjon naar frigiReservasjon er true`() {
        val brev = klartVedtaksbrev()

        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev, frigiReservasjon = false)).isSuccess {
            assertThat(it.info.redigeresAv).isEqualTo(attestant1Principal.navIdent)
        }
        assertThat(lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev, frigiReservasjon = true)).isSuccess {
            assertThat(it.info.redigeresAv).isNull()
        }
    }

    @Test
    suspend fun `kan ikke lagre uten attestantrolle`() {
        val brev = klartVedtaksbrev()

        val resultat = lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev, attestant = saksbehandler2Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.HarIkkeAttestantrolle, _, _>()
    }

    @Test
    suspend fun `kan ikke lagre eget brev som attestant`() {
        val brev = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            principal = attestant1Principal,
        ).resultOrFail()
        veksleKlarStatus(brev, klar = true, principal = attestant1Principal).resultOrFail()

        val resultat = lagreAttestertBrev(brev, nyttRedigertbrev = brev.redigertBrev)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereEgetBrev, _, _>()
    }

    @Test
    suspend fun `returnerer null hvis brev ikke finnes`() {
        val resultat = withPrincipal(attestant1Principal) {
            lagreAttestertBrev(
                LagreAttestertBrevHandler.Request(
                    brevId = BrevId(-9999L),
                    saksId = sak1.saksId,
                    nyttRedigertbrev = letter.toEdit(),
                )
            )
        }

        assertThat(resultat).isNull()
    }
}
