package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EndreValgteVedleggHandlerTest : BrevredigeringTest() {
    suspend fun endreVedlegg(brev: Dto.Brevredigering, vedlegg: List<AlltidValgbartVedleggKode>, principal: UserPrincipal = saksbehandler1Principal): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        withPrincipal(principal) {
            brevredigeringFacade.endreValgteVedlegg(EndreValgteVedleggHandler.Request(brev.info.id, vedlegg))
        }

    @Test
    suspend fun `kan endre valgte vedlegg`() {
        val brev = opprettBrev().resultOrFail()

        val vedlegg = listOf(AlltidValgbartVedleggBrevkode("kode1", "Visningstekst 1"), AlltidValgbartVedleggBrevkode("kode2", "Visningstekst 2"))
        assertThat(endreVedlegg(brev, vedlegg)).isSuccess {
            assertThat(it.valgteVedlegg).isEqualTo(vedlegg)
        }
    }

    @Test
    suspend fun `kan fjerne valgte vedlegg`() {
        val brev = opprettBrev().resultOrFail()

        val vedlegg = listOf(AlltidValgbartVedleggBrevkode("kode1", "Visningstekst 1"), AlltidValgbartVedleggBrevkode("kode2", "Visningstekst 2"))
        assertThat(endreVedlegg(brev, vedlegg)).isSuccess {
            assertThat(it.valgteVedlegg).isEqualTo(vedlegg)
        }

        assertThat(endreVedlegg(brev, emptyList())).isSuccess {
            assertThat(it.valgteVedlegg).isEmpty()
        }
    }

    @Test
    suspend fun `kan ikke endre vedlegg for brev som redigeres av andre`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        val vedlegg = listOf(AlltidValgbartVedleggBrevkode("kode1", "Visningstekst 1"), AlltidValgbartVedleggBrevkode("kode2", "Visningstekst 2"))
        assertThat(endreVedlegg(brev, vedlegg, saksbehandler2Principal)).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

}