package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EndreValgteVedleggHandlerTest : BrevredigeringHandlerTestBase() {
    suspend fun endreVedlegg(brev: Dto.Brevredigering, vedlegg: List<AlltidValgbartVedleggBrevkode>, principal: UserPrincipal = saksbehandler1Principal): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        withPrincipal(principal) {
            brevredigeringFacade.endreValgteVedlegg(EndreValgteVedleggHandler.Request(brev.info.id, vedlegg))
        }

    @Test
    suspend fun `kan endre valgte vedlegg`() {
        val brev = opprettBrev().resultOrFail()

        val vedlegg = listOf(AlltidValgbartVedleggBrevkode("kode1", "Visningstekst 1", setOf()), AlltidValgbartVedleggBrevkode("kode2", "Visningstekst 2", setOf()))
        assertThat(endreVedlegg(brev, vedlegg)).isSuccess {
            assertThat(it.valgteVedlegg).isEqualTo(vedlegg)
        }
    }

    @Test
    suspend fun `kan fjerne valgte vedlegg`() {
        val brev = opprettBrev().resultOrFail()

        val vedlegg = listOf(AlltidValgbartVedleggBrevkode("kode1", "Visningstekst 1", setOf()), AlltidValgbartVedleggBrevkode("kode2", "Visningstekst 2", setOf()))
        assertThat(endreVedlegg(brev, vedlegg)).isSuccess {
            assertThat(it.valgteVedlegg).isEqualTo(vedlegg)
        }

        assertThat(endreVedlegg(brev, emptyList())).isSuccess {
            assertThat(it.valgteVedlegg).isEmpty()
        }
    }

    @Test
    suspend fun `endring av valgte vedlegg foerer til ny rendring ved neste pdf-henting`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        val rendringerFoer = brevbakerService.renderPdfKall.size

        val vedlegg = listOf(AlltidValgbartVedleggBrevkode("kode1", "Visningstekst 1", setOf()))
        assertThat(endreVedlegg(brev, vedlegg)).isSuccess()

        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(brevbakerService.renderPdfKall.size).isGreaterThan(rendringerFoer)
    }

    @Test
    suspend fun `uendrede valgte vedlegg gjenbruker dokumentet uten ny rendring`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg = listOf(AlltidValgbartVedleggBrevkode("kode1", "Visningstekst 1", setOf()))
        assertThat(endreVedlegg(brev, vedlegg)).isSuccess()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        val rendringerFoer = brevbakerService.renderPdfKall.size

        assertThat(endreVedlegg(brev, vedlegg)).isSuccess()

        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(brevbakerService.renderPdfKall.size).isEqualTo(rendringerFoer)
    }

    @Test
    suspend fun `kan ikke endre valgte vedlegg for brev som redigeres av andre`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        val vedlegg = listOf(AlltidValgbartVedleggBrevkode("kode1", "Visningstekst 1", setOf()))
        assertThat(endreVedlegg(brev, vedlegg, saksbehandler2Principal))
            .isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

}