package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.AttesterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.BrevredigeringHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.BrevredigeringRequest
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.EndreDistribusjonstypeHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.EndreMottakerHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.EndreValgteVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevAttesteringHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentEllerOpprettPdfHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OpprettBrevHandlerImpl
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.ReserverBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.SendBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.TilbakestillBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.UseCaseHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.VeksleKlarStatusHandler
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.sql.Connection

class BrevredigeringFacade(
    private val opprettBrev: OpprettBrevHandler,
    private val oppdaterBrev: BrevredigeringHandler<OppdaterBrevHandler.Request, Dto.Brevredigering>,
    private val hentBrev: BrevredigeringHandler<HentBrevHandler.Request, Dto.Brevredigering>,
    private val hentBrevAttestering: BrevredigeringHandler<HentBrevAttesteringHandler.Request, Dto.Brevredigering>,
    private val veksleKlarStatus: BrevredigeringHandler<VeksleKlarStatusHandler.Request, Dto.BrevInfo>,
    private val endreDistribusjonstype: BrevredigeringHandler<EndreDistribusjonstypeHandler.Request, Dto.BrevInfo>,
    private val endreMottaker: BrevredigeringHandler<EndreMottakerHandler.Request, Dto.BrevInfo>,
    private val reserverBrev: UseCaseHandler<ReserverBrevHandler.Request, Reservasjon, BrevredigeringError>,
    private val hentEllerOpprettPdf: BrevredigeringHandler<HentEllerOpprettPdfHandler.Request, Dto.HentDocumentResult>,
    private val attesterBrev: BrevredigeringHandler<AttesterBrevHandler.Request, Dto.Brevredigering>,
    private val tilbakestillBrev: BrevredigeringHandler<TilbakestillBrevHandler.Request, Dto.Brevredigering>,
    private val endreValgteVedlegg: BrevredigeringHandler<EndreValgteVedleggHandler.Request, Dto.Brevredigering>,
    private val sendBrev: BrevredigeringHandler<SendBrevHandler.Request, Dto.SendBrevResult>,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) {

    suspend fun opprettBrev(request: OpprettBrevHandlerImpl.Request): Outcome<Dto.Brevredigering, BrevredigeringError> =
        suspendTransaction {
            opprettBrev.handle(request)
        }

    suspend fun oppdaterBrev(request: OppdaterBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        oppdaterBrev.runHandler(request)

    fun hentBrevInfo(brevId: BrevId): Dto.BrevInfo? =
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

    suspend fun attesterBrev(request: AttesterBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        attesterBrev.runHandler(request)

    suspend fun tilbakestillBrev(request: TilbakestillBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        tilbakestillBrev.runHandler(request)

    suspend fun veksleKlarStatus(request: VeksleKlarStatusHandler.Request): Outcome<Dto.BrevInfo, BrevredigeringError>? =
        veksleKlarStatus.runHandler(request)

    suspend fun endreDistribusjonstype(request: EndreDistribusjonstypeHandler.Request): Outcome<Dto.BrevInfo, BrevredigeringError>? =
        endreDistribusjonstype.runHandler(request)

    suspend fun endreMottaker(request: EndreMottakerHandler.Request): Outcome<Dto.BrevInfo, BrevredigeringError>? =
        endreMottaker.runHandler(request)

    suspend fun endreValgteVedlegg(request: EndreValgteVedleggHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        endreValgteVedlegg.runHandler(request)

    suspend fun reserverBrev(request: ReserverBrevHandler.Request): Outcome<Reservasjon, BrevredigeringError>? =
        suspendTransaction(transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ) {
            reserverBrev.handle(request)?.onError { rollback() }
        }

    suspend fun hentPDF(request: HentEllerOpprettPdfHandler.Request): Outcome<Dto.HentDocumentResult, BrevredigeringError>? =
        hentEllerOpprettPdf.runHandler(request)

    suspend fun sendBrev(request: SendBrevHandler.Request): Outcome<Dto.SendBrevResult, BrevredigeringError>? =
        sendBrev.runHandler(request)

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