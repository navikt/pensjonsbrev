package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.sql.Connection

class BrevredigeringFacade(
    private val opprettBrev: OpprettBrevHandler,
    private val reserverBrev: UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError>,
    private val frigiReservasjon: UseCaseHandler<FrigiReservasjonHandler.Request, Unit, BrevredigeringError>,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : HentBrevService, OpprettBrevService {

    override suspend fun opprettBrev(request: OpprettBrevHandlerImpl.Request): Outcome<Dto.Brevredigering, BrevredigeringError> =
        suspendTransaction {
            opprettBrev.handle(request)
        }

    override fun hentBrevInfo(brevId: BrevId): Dto.BrevInfo? =
        transaction { BrevredigeringEntity.findById(brevId)?.toBrevInfo(brevreservasjonPolicy) }

    fun hentBrevForSak(saksId: SaksId): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId eq saksId }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }

    override fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId inList saksIder }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }

    suspend fun reserverBrev(request: ReserverBrevHandler.Request): Outcome<Reservasjon, BrevredigeringError>? =
        suspendTransaction(transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ) {
            reserverBrev.handle(request)?.onError { rollback() }
        }

    suspend fun frigiReservasjon(request: FrigiReservasjonHandler.Request): Outcome<Unit, BrevredigeringError>? =
        suspendTransaction {
            frigiReservasjon.handle(request)?.onError { rollback() }
        }
}