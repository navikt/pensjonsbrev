package no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase.Fixtures.sak1
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase.Fixtures.saksbehandler1Principal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.isSuccess
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.statements.StatementInterceptor
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test

class BrevtilgangTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `forRedigering reserverer brevet`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.info.redigeresAv).isNull()

        val result = withPrincipal(saksbehandler1Principal) {
            brevtilgang.forRedigering(brev.info.id, sak1.saksId, frigiReservasjon = false) { success(this.brev.tilDto(null)) }
        }

        assertThat(result).isSuccess { assertThat(it.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent) }
    }

    @Test
    suspend fun `forRedigering med frigiReservasjon frigir reservasjonen foer blokken bygger dto`() {
        val brev = opprettBrev().resultOrFail()

        val result = withPrincipal(saksbehandler1Principal) {
            brevtilgang.forRedigering(brev.info.id, sak1.saksId, frigiReservasjon = true) { success(this.brev.tilDto(null)) }
        }

        assertThat(result).isSuccess { assertThat(it.info.redigeresAv).isNull() }
    }

    @Test
    suspend fun `forLesing reserverer ikke brevet`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.info.redigeresAv).isNull()

        val result = withPrincipal(saksbehandler1Principal) {
            brevtilgang.forLesing(brev.info.id, sak1.saksId) { success(this.brev.tilDto(null)) }
        }

        assertThat(result).isSuccess { assertThat(it.info.redigeresAv).isNull() }
    }

    @Test
    suspend fun `ruller tilbake transaksjon om blokken feiler`() {
        val interceptor = DidRollbackInterceptor()
        val brev = opprettBrev().resultOrFail()

        withPrincipal(saksbehandler1Principal) {
            brevtilgang.forRedigering(brev.info.id, sak1.saksId, frigiReservasjon = false) {
                // Rollback i ytre transaksjon vil også utløse rollback i denne indre transaksjonen.
                transaction(SharedPostgres.database) {
                    registerInterceptor(interceptor)
                }
                failure(RedigerBrevPolicy.KanIkkeRedigere.LaastBrev)
            }
        }

        assertThat(interceptor.didRollback).isTrue()
    }

    @Test
    suspend fun `ruller ikke tilbake transaksjon om blokken er vellykket`() {
        val interceptor = DidRollbackInterceptor()
        val brev = opprettBrev().resultOrFail()

        withPrincipal(saksbehandler1Principal) {
            brevtilgang.forRedigering(brev.info.id, sak1.saksId, frigiReservasjon = false) {
                // Rollback i ytre transaksjon vil også utløse rollback i denne indre transaksjonen.
                transaction {
                    registerInterceptor(interceptor)
                }
                success(this.brev.tilDto(emptySet()))
            }
        }

        assertThat(interceptor.didRollback).isFalse()
    }
}

private class DidRollbackInterceptor : StatementInterceptor {
    var didRollback = false
        private set

    override fun afterRollback(transaction: Transaction) {
        didRollback = true
    }
}
