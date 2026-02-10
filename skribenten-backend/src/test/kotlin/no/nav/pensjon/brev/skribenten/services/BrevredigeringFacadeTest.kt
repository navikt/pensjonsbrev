package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.usecase.*
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementInterceptor
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
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

    @Test
    suspend fun `reserverer brev for handler som krever det`() {
        val reserverBrev = ReserverBrevStub()
        val request = HentBrevHandler.Request(brevId = 42L)

        createFacade(
            reserverBrev = reserverBrev,
            hentBrev = object : BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering> {
                override suspend fun handle(request: HentBrevHandler.Request) = null
                override fun requiresReservasjon(request: HentBrevHandler.Request) = true
            },
        ).hentBrev(request)

        assertThat(reserverBrev.reservertBrevId).isEqualTo(request.brevId)
    }

    @Test
    suspend fun `reserverer ikke brev for handler som ikke krever det`() {
        val reserverBrev = ReserverBrevStub()
        val request = HentBrevHandler.Request(brevId = 42L)

        createFacade(
            reserverBrev = reserverBrev,
            hentBrev = object : BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering> {
                override suspend fun handle(request: HentBrevHandler.Request) = null
                override fun requiresReservasjon(request: HentBrevHandler.Request) = false
            },
        ).hentBrev(request)

        assertThat(reserverBrev.reservertBrevId).isNull()
    }

    @Test
    suspend fun `ruller tilbake transaksjon om handler feiler`() {
        val interceptor = DidRollbackInterceptor()
        val facade = createFacade(
            reserverBrev = ReserverBrevStub(), hentBrev = object : BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering> {
                override suspend fun handle(request: HentBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError> {
                    // Rollback i ytre transaksjon vil også utløse rollback i denne indre transaksjonen.
                    transaction {
                        registerInterceptor(interceptor)
                    }
                    return failure(RedigerBrevPolicy.KanIkkeRedigere.LaastBrev)
                }
            })

        facade.hentBrev(HentBrevHandler.Request(123L))
        assertThat(interceptor.didRollback).isTrue()
    }

    @Test
    suspend fun `ruller ikke tilbake transaksjon om handler er vellykket`() {
        val interceptor = DidRollbackInterceptor()
        val facade = createFacade(
            reserverBrev = ReserverBrevStub(),
            hentBrev = object : BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering> {
                override suspend fun handle(request: HentBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError> {
                    // Rollback i ytre transaksjon vil også utløse rollback i denne indre transaksjonen.
                    transaction {
                        registerInterceptor(interceptor)
                    }
                    return success(brevredigering)
                }
            }
        )

        facade.hentBrev(HentBrevHandler.Request(123L))
        assertThat(interceptor.didRollback).isFalse()
    }

    @Test
    suspend fun `ruller tilbake reservasjonstransaksjon om reservasjon feiler`() {
        val interceptor = DidRollbackInterceptor()
        val facade = createFacade(
            reserverBrev = object : UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError> {
                override suspend fun handle(request: ReserverBrevHandler.Request): Outcome<Reservasjon, BrevredigeringError> {
                    // Rollback i ytre transaksjon vil også utløse rollback i denne indre transaksjonen.
                    transaction {
                        registerInterceptor(interceptor)
                    }
                    return failure(BrevreservasjonPolicy.ReservertAvAnnen(reservasjon))
                }
            }
        )
        facade.hentBrev(HentBrevHandler.Request(123L))
        assertThat(interceptor.didRollback).isTrue()
    }

    @Test
    suspend fun `ruller ikke tilbake vellykket reservasjonstransaksjon`() {
        val interceptor = DidRollbackInterceptor()
        val facade = createFacade(
            hentBrev = object : BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering> {
                override suspend fun handle(request: HentBrevHandler.Request) = success(brevredigering)
                override fun requiresReservasjon(request: HentBrevHandler.Request) = true
            },
            reserverBrev = object : UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError> {
                override suspend fun handle(request: ReserverBrevHandler.Request): Outcome<Reservasjon, BrevredigeringError> {
                    // Rollback i ytre transaksjon vil også utløse rollback i denne indre transaksjonen.
                    transaction {
                        registerInterceptor(interceptor)
                    }
                    return success(reservasjon)
                }
            }
        )
        facade.hentBrev(HentBrevHandler.Request(123L))
        assertThat(interceptor.didRollback).isFalse()
    }

    private val brevredigering = Dto.Brevredigering(
        info = Dto.BrevInfo(
            id = 1,
            saksId = 1,
            vedtaksId = null,
            opprettetAv = NavIdent("11"),
            opprettet = Instant.now(),
            sistredigertAv = NavIdent("11"),
            sistredigert = Instant.now(),
            redigeresAv = NavIdent("11"),
            sistReservert = null,
            brevkode = Testbrevkoder.INFORMASJONSBREV,
            laastForRedigering = false,
            distribusjonstype = Distribusjonstype.SENTRALPRINT,
            mottaker = null,
            avsenderEnhetId = null,
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
    var reservertBrevId: Long? = null
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
    oppdaterBrev: BrevredigeringHandler<OppdaterBrevHandler.Request, Dto.Brevredigering> = handlerStub(),
    hentBrev: BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering> = handlerStub(),
    hentBrevAttestering: BrevredigeringHandler<HentBrevAttesteringHandler.Request, Dto.Brevredigering> = handlerStub(),
    veksleKlarStatus: BrevredigeringHandler<VeksleKlarStatusHandler.Request, Dto.Brevredigering> = handlerStub(),
    endreDistribusjonstype: BrevredigeringHandler<EndreDistribusjonstypeHandler.Request, Dto.Brevredigering> = handlerStub(),
    endreMottaker: BrevredigeringHandler<EndreMottakerHandler.Request, Dto.Brevredigering> = handlerStub(),
    reserverBrev: UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError> = handlerStub(),
): BrevredigeringFacade {
    return BrevredigeringFacade(
        opprettBrev = opprettBrev,
        oppdaterBrev = oppdaterBrev,
        hentBrev = hentBrev,
        hentBrevAttestering = hentBrevAttestering,
        veksleKlarStatus = veksleKlarStatus,
        endreDistribusjonstype = endreDistribusjonstype,
        endreMottaker = endreMottaker,
        reserverBrev = reserverBrev,
    )
}

private fun <T : BrevredigeringRequest, Response> handlerStub() = object : BrevredigeringHandler<T, Response> {
    override suspend fun handle(request: T) = notYetStubbed()
}

