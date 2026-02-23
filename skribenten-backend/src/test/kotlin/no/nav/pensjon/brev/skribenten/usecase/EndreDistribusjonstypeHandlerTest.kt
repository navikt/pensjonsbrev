package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EndreDistribusjonstypeHandlerTest : BrevredigeringTest() {

    suspend fun endreDistribusjonstype(
        brevId: BrevId,
        nyDistribusjonstype: Distribusjonstype,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.BrevInfo, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.endreDistribusjonstype(
            EndreDistribusjonstypeHandler.Request(
                brevId = brevId,
                type = nyDistribusjonstype,
            )
        )
    }

    @Test
    suspend fun `kan endre distribusjonstype`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(endreDistribusjonstype(brev.info.id, Distribusjonstype.LOKALPRINT))
            .isSuccess {
                assertThat(it.distribusjonstype).isEqualTo(Distribusjonstype.LOKALPRINT)
            }
    }

    @Test
    suspend fun `kan ikke endre distribusjonstype på brev reservert av annen saksbehandler`() {
        val brev = opprettBrev(principal = saksbehandler1Principal, reserverForRedigering = true).resultOrFail()

        val resultat = endreDistribusjonstype(
            brevId = brev.info.id,
            nyDistribusjonstype = Distribusjonstype.LOKALPRINT,
            principal = saksbehandler2Principal,
        )

        assertThat(resultat).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `kan endre distribusjonstype på klart brev`() {
        val brev = opprettBrev().resultOrFail()
        veksleKlarStatus(brev, klar = true).resultOrFail()

        assertThat(endreDistribusjonstype(brev.info.id, Distribusjonstype.LOKALPRINT))
            .isSuccess {
                assertThat(it.distribusjonstype).isEqualTo(Distribusjonstype.LOKALPRINT)
            }
    }

    @Test
    suspend fun `kan ikke endre distribusjonstype på arkivert brev`() {
        val brev = opprettBrev().resultOrFail()
        arkiverBrev(brev).resultOrFail()

        assertThat(endreDistribusjonstype(brev.info.id, Distribusjonstype.LOKALPRINT))
            .isFailure<RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev, _, _>()
    }

    @Test
    suspend fun `beholder ikke reservasjon`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(endreDistribusjonstype(brev.info.id, Distribusjonstype.LOKALPRINT))
            .isSuccess {
                assertThat(it.redigeresAv).isNotEqualTo(saksbehandler1Principal.navIdent)
            }
    }
}