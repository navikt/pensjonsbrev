package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.notYetStubbed
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.statements.StatementInterceptor
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class BrevredigeringFacadeTest {

    @BeforeAll
    fun startDbOnce() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    private val brevredigering = Dto.Brevredigering(
        info = Dto.BrevInfo(
            id = BrevId(1),
            saksId = SaksId(1),
            vedtaksId = null,
            opprettetAv = NavIdent("11"),
            opprettet = Instant.now(),
            sistredigertAv = NavIdent("11"),
            sistredigert = Instant.now(),
            redigeresAv = NavIdent("11"),
            sistReservert = null,
            brevkode = Testbrevkoder.INFORMASJONSBREV,
            laastForRedigering = false,
            distribusjonstype = Distribusjon.SENTRALPRINT,
            mottaker = null,
            avsenderEnhetId = EnhetId("9876"),
            spraak = LanguageCode.ENGLISH,
            journalpostId = null,
            attestertAv = null,
            status = Dto.BrevStatus.KLADD
        ),
        redigertBrev = editedLetter(),
        redigertBrevHash = Hash("abc123"),
        saksbehandlerValg = SaksbehandlerValg(),
        propertyUsage = null,
        valgteVedlegg = emptyList(),
    )

    private val reservasjon = Reservasjon(
        vellykket = true,
        reservertAv = NavIdent("Z123456"),
        timestamp = Instant.now(),
        expiresIn = 10.minutes.toJavaDuration(),
        redigertBrevHash = Hash(""),
    )
}

private class DidRollbackInterceptor : StatementInterceptor {
    var didRollback = false
        private set

    override fun afterRollback(transaction: Transaction) {
        didRollback = true
    }
}

private class ReserverBrevStub : UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError> {
    var reservertBrevId: BrevId? = null
        private set

    override suspend fun handle(request: ReserverBrevHandler.Request): Outcome<Reservasjon, BrevredigeringError> {
        reservertBrevId = request.brevId
        val reservasjon = Reservasjon(true, NavIdent("Z123456"), Instant.now(), expiresIn = 10.minutes.toJavaDuration(), Hash(""))
        return success(reservasjon)
    }
}

private fun createFacade(
    opprettBrev: OpprettBrevHandler = object : OpprettBrevHandler {
        override suspend fun handle(request: OpprettBrevHandlerImpl.Request) = notYetStubbed()
    },
    reserverBrev: UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError> = handlerStub(),
    frigiReservasjon: UseCaseHandler<FrigiReservasjonHandler.Request, Unit, BrevredigeringError> = handlerStub(),
    sendBrev: BrevredigeringHandler<SendBrevHandler.Request, Dto.SendBrevResult> = handlerStub(),
    brevreservasjonPolicy: BrevreservasjonPolicy = BrevreservasjonPolicy(),
): BrevredigeringFacade {
    return BrevredigeringFacade(
        opprettBrev = opprettBrev,
        reserverBrev = reserverBrev,
        frigiReservasjon = frigiReservasjon,
        brevreservasjonPolicy = brevreservasjonPolicy,
        sendBrev = sendBrev,
        slettBrev = handlerStub(),
    )
}

private fun <T : BrevredigeringRequest, Response> handlerStub() = object : BrevredigeringHandler<T, Response> {
    override suspend fun handle(request: T) = notYetStubbed()
    override fun requiresReservasjon(request: T) = true
}