package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SetFoerstesideHandlerTest : BrevredigeringHandlerTestBase() {

    private suspend fun setHarFoersteside(brevId: BrevId, harFoersteside: Boolean, principal: UserPrincipal = saksbehandler1Principal) =
        withPrincipal(principal) {
            brevredigeringFacade.setHarFoersteside(SetFoerstesideHandler.Request(brevId, harFoersteside))
        }

    @Test
    suspend fun `kan sette harFoersteside til true`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(setHarFoersteside(brev.info.id, true))
            .isSuccess {
                assertThat(it.harFoersteside).isTrue()
            }
    }

    @Test
    suspend fun `kan sette harFoersteside til false`() {
        val brev = opprettBrev().resultOrFail()
        setHarFoersteside(brev.info.id, true).resultOrFail()

        assertThat(setHarFoersteside(brev.info.id, false))
            .isSuccess {
                assertThat(it.harFoersteside).isFalse()
            }
    }

    @Test
    suspend fun `kan ikke sette harFoersteside på brev reservert av annen saksbehandler`() {
        val brev = opprettBrev(principal = saksbehandler1Principal, reserverForRedigering = true).resultOrFail()

        val resultat = setHarFoersteside(
            brevId = brev.info.id,
            harFoersteside = true,
            principal = saksbehandler2Principal,
        )

        assertThat(resultat).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `kan ikke sette harFoersteside på arkivert brev`() {
        val brev = opprettBrev().resultOrFail()
        arkiverBrev(brev).resultOrFail()

        assertThat(setHarFoersteside(brev.info.id, true))
            .isFailure<RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev, _, _>()
    }

    @Test
    suspend fun `beholder ikke reservasjon`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(setHarFoersteside(brev.info.id, true))
            .isSuccess {
                assertThat(it.redigeresAv).isNotEqualTo(saksbehandler1Principal.navIdent)
            }
    }
}
