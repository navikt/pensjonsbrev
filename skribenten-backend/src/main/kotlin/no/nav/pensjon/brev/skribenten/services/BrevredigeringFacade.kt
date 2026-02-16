package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.*
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.sql.Connection

class BrevredigeringFacade(
    private val opprettBrev: OpprettBrevHandler,
    private val oppdaterBrev: BrevredigeringHandler<OppdaterBrevHandler.Request, Dto.Brevredigering>,
    private val hentBrev: BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering>,
    private val hentBrevAttestering: BrevredigeringHandler<HentBrevAttesteringHandler.Request, Dto.Brevredigering>,
    private val veksleKlarStatus: BrevredigeringHandler<VeksleKlarStatusHandler.Request, Dto.Brevredigering>,
    private val endreDistribusjonstype: BrevredigeringHandler<EndreDistribusjonstypeHandler.Request, Dto.Brevredigering>,
    private val endreMottaker: BrevredigeringHandler<EndreMottakerHandler.Request, Dto.Brevredigering>,
    private val reserverBrev: UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError>,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) {

    suspend fun opprettBrev(request: OpprettBrevHandlerImpl.Request): Outcome<Dto.Brevredigering, BrevredigeringError> =
        suspendTransaction {
            opprettBrev.handle(request)
        }

    suspend fun oppdaterBrev(request: OppdaterBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        oppdaterBrev.runHandler(request)

    fun hentBrevInfo(brevId: Long): Dto.BrevInfo? =
        transaction { BrevredigeringEntity.findById(brevId)?.toBrevInfo(brevreservasjonPolicy) }

    fun hentBrevForSak(saksId: SaksId): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId eq saksId }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }

    suspend fun hentBrev(request: HentBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        hentBrev.runHandler(request)

    suspend fun hentBrevAttestering(request: HentBrevAttesteringHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        hentBrevAttestering.runHandler(request)

    suspend fun veksleKlarStatus(request: VeksleKlarStatusHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        veksleKlarStatus.runHandler(request)

    suspend fun endreDistribusjonstype(request: EndreDistribusjonstypeHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        endreDistribusjonstype.runHandler(request)

    suspend fun endreMottaker(request: EndreMottakerHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        endreMottaker.runHandler(request)

    suspend fun reserverBrev(request: ReserverBrevHandler.Request): Outcome<Reservasjon, BrevredigeringError>? =
        suspendTransaction(transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ) {
            reserverBrev.handle(request)?.onError { rollback() }
        }

    private suspend fun <Request : BrevredigeringRequest, Response> BrevredigeringHandler<Request, Response>.runHandler(request: Request): Outcome<Response, BrevredigeringError>? {
        if (requiresReservasjon(request)) {
            // Forsøk å reservere brevet før vi kjører handleren, om reservasjonen feiler returner feilen eller om brevet ikke finnes returner null.
            reserverBrev(ReserverBrevHandler.Request(request.brevId))
                ?.onError { return failure(it) }
                ?: return null
        }

        return suspendTransaction {
            handle(request)?.onError { rollback() }
        }
    }
}