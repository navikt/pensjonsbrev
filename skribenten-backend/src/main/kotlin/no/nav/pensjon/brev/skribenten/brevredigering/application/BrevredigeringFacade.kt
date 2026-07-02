package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.letter.Edit
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
    private val hentEllerOpprettPdf: BrevredigeringHandler<HentEllerOpprettPdfHandler.Request, Dto.HentDocumentResult>,
    private val hentRedigertVedlegg: BrevredigeringHandler<HentRedigertVedleggHandler.Request, Edit.Attachment>,
    private val hentRedigerbareVedlegg: BrevredigeringHandler<HentRedigerbareVedleggHandler.Request, List<RedigerbartVedleggInfo>>,
    private val sendBrev: BrevredigeringHandler<SendBrevHandler.Request, Dto.SendBrevResult>,
    private val slettBrev: BrevredigeringHandler<SlettBrevHandler.Request, Unit>,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val diffBrev: BrevredigeringHandler<DiffBrevHandler.Request, DiffBrevHandler.Response>,
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

    suspend fun hentRedigertVedlegg(request: HentRedigertVedleggHandler.Request): Outcome<Edit.Attachment, BrevredigeringError>? =
        hentRedigertVedlegg.runHandler(request)

    suspend fun hentRedigerbareVedlegg(request: HentRedigerbareVedleggHandler.Request): Outcome<List<RedigerbartVedleggInfo>, BrevredigeringError>? =
        hentRedigerbareVedlegg.runHandler(request)

    suspend fun reserverBrev(request: ReserverBrevHandler.Request): Outcome<Reservasjon, BrevredigeringError>? =
        suspendTransaction(transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ) {
            reserverBrev.handle(request)?.onError { rollback() }
        }

    suspend fun frigiReservasjon(request: FrigiReservasjonHandler.Request): Outcome<Unit, BrevredigeringError>? =
        suspendTransaction {
            frigiReservasjon.handle(request)?.onError { rollback() }
        }

    suspend fun hentPDF(request: HentEllerOpprettPdfHandler.Request): Outcome<Dto.HentDocumentResult, BrevredigeringError>? =
        hentEllerOpprettPdf.runHandler(request)

    suspend fun sendBrev(request: SendBrevHandler.Request): Outcome<Dto.SendBrevResult, BrevredigeringError>? =
        sendBrev.runHandler(request)

    suspend fun slettBrev(request: SlettBrevHandler.Request): Outcome<Unit, BrevredigeringError>? =
        slettBrev.runHandler(request)

    suspend fun diffBrev(request: DiffBrevHandler.Request): Outcome<DiffBrevHandler.Response, BrevredigeringError>? =
        diffBrev.runHandler(request)

    private suspend fun <Request : BrevredigeringRequest, Response> BrevredigeringHandler<Request, Response>.runHandler(request: Request): Outcome<Response, BrevredigeringError>? {
        if (requiresReservasjon(request)) {
            // Forsøk å reservere brevet før vi kjører handleren, om reservasjonen feiler returner feilen eller om brevet ikke finnes returner null.
            reserverBrev(ReserverBrevHandler.Request(request.brevId))
                ?.onError { return failure(it) }
                ?: return null
        }

        val isolation = transactionIsolation()
        return if (isolation != null) {
            suspendTransaction(transactionIsolation = isolation) {
                handle(request)?.onError { rollback() }
            }
        } else {
            suspendTransaction {
                handle(request)?.onError { rollback() }
            }
        }
    }
}