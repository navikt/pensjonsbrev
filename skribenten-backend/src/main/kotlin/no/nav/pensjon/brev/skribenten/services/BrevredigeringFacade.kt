package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.domain.*
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.usecase.*
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.sql.Connection
import java.time.Instant

class BrevredigeringFacade(
    private val brevreservasjonPolicy: BrevreservasjonPolicy = BrevreservasjonPolicy(),
    private val opprettBrev: OpprettBrevHandler,
    private val oppdaterBrev: BrevredigeringHandler<OppdaterBrevHandler.Request>,
    private val hentBrev: BrevredigeringHandler<HentBrevHandler.Request>,
    private val hentBrevAttestering: BrevredigeringHandler<HentBrevAttesteringHandler.Request>,
    private val veksleKlarStatus: BrevredigeringHandler<VeksleKlarStatusHandler.Request>,
    private val endreDistribusjonstype: BrevredigeringHandler<EndreDistribusjonstypeHandler.Request>,
    private val endreMottaker: BrevredigeringHandler<EndreMottakerHandler.Request>,
) {

    suspend fun opprettBrev(request: OpprettBrevHandlerImpl.Request): Outcome<Dto.Brevredigering, BrevredigeringError> =
        suspendTransaction {
            opprettBrev.handle(request)
        }

    suspend fun oppdaterBrev(request: OppdaterBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        oppdaterBrev.runHandler(request)

    fun hentBrevInfo(brevId: Long): Dto.BrevInfo? =
        transaction { BrevredigeringEntity.findById(brevId)?.toBrevInfo() }

    fun hentBrevForSak(saksId: Long): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId eq saksId }
                .map { it.toBrevInfo() }
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

    private suspend fun <Request : BrevredigeringRequest> BrevredigeringHandler<Request>.runHandler(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        if (requiresReservasjon(request)) {
            val principal = PrincipalInContext.require()

            val reservasjon = transaction(transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ) {
                BrevredigeringEntity.findById(request.brevId)
                    ?.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
            }
            if (reservasjon == null) {
                return null
            } else if (reservasjon is Outcome.Failure) {
                return failure(reservasjon.error)
            }
        }

        return suspendTransaction {
            handle(request)
        }
    }
}