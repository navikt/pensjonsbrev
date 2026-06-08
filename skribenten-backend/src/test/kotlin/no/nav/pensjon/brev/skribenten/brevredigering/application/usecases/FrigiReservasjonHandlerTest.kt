package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FrigiReservasjonHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `eier kan frigi reservasjon`() {
        val brev = opprettBrev(principal = saksbehandler1Principal, reserverForRedigering = true).resultOrFail()

        val resultat = frigiReservasjon(
            brevId = brev.info.id,
            saksId = brev.info.saksId,
            principal = saksbehandler1Principal,
        )

        assertThat(resultat).isSuccess {
            val etterFrigi = hentBrev(brevId = brev.info.id, principal = saksbehandler1Principal).resultOrFail()
            assertThat(etterFrigi.info.redigeresAv).isNull()
        }
    }

    @Test
    suspend fun `ikke-eier frigir ikke reservasjon`() {
        val brev = opprettBrev(principal = saksbehandler1Principal, reserverForRedigering = true).resultOrFail()

        val resultat = frigiReservasjon(
            brevId = brev.info.id,
            saksId = brev.info.saksId,
            principal = saksbehandler2Principal,
        )

        assertThat(resultat).isSuccess {
            val etterFrigi = hentBrev(brevId = brev.info.id, principal = saksbehandler1Principal).resultOrFail()
            assertThat(etterFrigi.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    @Test
    suspend fun `frigi reservasjon uten aktiv reservasjon gjør ingenting`() {
        val brev = opprettBrev(principal = saksbehandler1Principal, reserverForRedigering = false).resultOrFail()

        val resultat = frigiReservasjon(
            brevId = brev.info.id,
            saksId = brev.info.saksId,
            principal = saksbehandler1Principal,
        )

        assertThat(resultat).isSuccess {
            val etterFrigi = hentBrev(brevId = brev.info.id, principal = saksbehandler1Principal).resultOrFail()
            assertThat(etterFrigi.info.redigeresAv).isNull()
        }
    }

    private suspend fun frigiReservasjon(
        brevId: BrevId,
        saksId: SaksId,
        principal: no.nav.pensjon.brev.skribenten.auth.UserPrincipal = saksbehandler1Principal,
    ): Outcome<Unit, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.frigiReservasjon(FrigiReservasjonHandler.Request(saksId = saksId, brevId = brevId))
    }
}