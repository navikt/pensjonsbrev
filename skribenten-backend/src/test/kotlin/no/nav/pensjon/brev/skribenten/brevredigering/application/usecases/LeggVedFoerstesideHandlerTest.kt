package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LeggVedFoerstesideHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `kan sette leggVedFoersteside til true`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(leggVedFoersteside(brev.info.id, leggVedFoersteside = true)).isSuccess {
            assertThat(it.leggVedFoersteside).isTrue()
        }
    }

    @Test
    suspend fun `kan sette leggVedFoersteside til false`() {
        val brev = opprettBrev().resultOrFail()
        leggVedFoersteside(brev.info.id, leggVedFoersteside = true).resultOrFail()

        assertThat(leggVedFoersteside(brev.info.id, leggVedFoersteside = false)).isSuccess {
            assertThat(it.leggVedFoersteside).isFalse()
        }
    }

    @Test
    suspend fun `returnerer null for brev som ikke finnes`() {
        assertThat(leggVedFoersteside(BrevId(-1), leggVedFoersteside = true)).isNull()
    }

    @Test
    suspend fun `beholder ikke reservasjon etter endring`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(leggVedFoersteside(brev.info.id, leggVedFoersteside = true)).isSuccess {
            assertThat(it.redigeresAv).isNotEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    @Test
    suspend fun `kan ikke sette foersteside for brev reservert av annen saksbehandler`() {
        val brev = opprettBrev(principal = saksbehandler1Principal, reserverForRedigering = true).resultOrFail()

        assertThat(leggVedFoersteside(brev.info.id, leggVedFoersteside = true, principal = saksbehandler2Principal))
            .isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `kan ikke sette foersteside for arkivert brev`() {
        val brev = opprettBrev().resultOrFail()
        arkiverBrev(brev).resultOrFail()

        assertThat(leggVedFoersteside(brev.info.id, leggVedFoersteside = true))
            .isFailure<RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev, _, _>()
    }
}
