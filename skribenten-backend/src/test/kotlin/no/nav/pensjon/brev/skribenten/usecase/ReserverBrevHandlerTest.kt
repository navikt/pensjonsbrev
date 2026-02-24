package no.nav.pensjon.brev.skribenten.usecase

import kotlinx.coroutines.*
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.function.Predicate

class ReserverBrevHandlerTest : BrevredigeringTest() {

    suspend fun reserverBrev(
        brev: Dto.Brevredigering,
        principal: MockPrincipal = saksbehandler1Principal
    ): Outcome<Reservasjon, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.reserverBrev(ReserverBrevHandler.Request(brevId = brev.info.id))
    }

    @Test
    suspend fun `brev reservasjon kan fornyes`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        val forrigeReservasjon = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MILLIS)
        transaction { BrevredigeringEntity[brev.info.id].sistReservert = forrigeReservasjon }

        val reservasjon = reserverBrev(brev)
        assertThat(reservasjon).isSuccess {
            assertThat(it.reservertAv).isEqualTo(saksbehandler1Principal.navIdent)
            assertThat(it.vellykket).isTrue()
            assertThat(it.timestamp).isAfter(forrigeReservasjon)
                .isBetween(Instant.now().minusSeconds(1), Instant.now().plusSeconds(1))
                .isEqualTo(transaction { BrevredigeringEntity[brev.info.id].sistReservert })

        }
    }

    @Test
    suspend fun `allerede reservert brev kan ikke resereveres for redigering`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        val nyReservasjon = reserverBrev(brev = brev, principal = saksbehandler2Principal)
        assertThat(nyReservasjon).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _> {
            assertThat(it.eksisterende.reservertAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    @Test
    suspend fun `kun en som vinner reservasjon av et brev`() {
        val brev = opprettBrev().resultOrFail()

        brevbakerService.renderMarkupResultat = {
            delay(100)
            letter
        }

        val reservasjoner = coroutineScope {
            (0..10).map {
                async(Dispatchers.IO) {
                    reserverBrev(principal = MockPrincipal(NavIdent("id-$it"), "saksbehandler-id-$it"), brev = brev)
                }
            }
        }
        val awaited = reservasjoner.awaitAll().filterNotNull()
        assertThat(awaited).hasSize(reservasjoner.size)

        assertThat(awaited).areExactly(1, condition("Vellykkede hentBrev med reservasjon") { it.isSuccess })
        assertThat(awaited).areExactly(awaited.size - 1, condition("Feilende hentBrev med reservasjon") { it.isFailure })

        val faktiskReservertAv = awaited.filterIsInstance<Outcome.Success<Reservasjon>>().single().resultOrFail().reservertAv

        assertThat(awaited).allMatch {
            when (it) {
                is Outcome.Success -> it.value.reservertAv == faktiskReservertAv
                is Outcome.Failure -> it.error is BrevreservasjonPolicy.ReservertAvAnnen && it.error.eksisterende.reservertAv == faktiskReservertAv
            }
        }
    }

    private fun <T> condition(description: String, predicate: Predicate<T>): Condition<T> =
        Condition(predicate, description)
}