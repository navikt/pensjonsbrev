package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.*
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.*
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.Instant

class BrevredigeringFacade(
    brevbakerService: BrevbakerService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
    private val renderService: RenderService,
    private val redigerBrevPolicy: RedigerBrevPolicy = RedigerBrevPolicy(),
    private val brevreservasjonPolicy: BrevreservasjonPolicy = BrevreservasjonPolicy(),
    private val opprettBrevPolicy: OpprettBrevPolicy = OpprettBrevPolicy(brevbakerService, navansattService),
    private val klarTilSendingPolicy: KlarTilSendingPolicy = KlarTilSendingPolicy(),
) {

    suspend fun oppdaterBrev(request: OppdaterBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        OppdaterBrevHandler(
            redigerBrevPolicy = redigerBrevPolicy,
            renderService = renderService,
            brevdataService = brevdataService
        ).runHandler(request)

    suspend fun opprettBrev(request: OpprettBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError> =
        newSuspendedTransaction {
            OpprettBrevHandler(
                opprettBrevPolicy = opprettBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
                renderService = renderService,
                brevdataService = brevdataService,
                navansattService = navansattService,
            ).handle(request)
        }

    suspend fun hentBrev(request: HentBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        HentBrevHandler(
            redigerBrevPolicy = redigerBrevPolicy,
            renderService = renderService,
            brevdataService = brevdataService,
        ).runHandler(request)

    suspend fun veksleKlarStatus(request: VeksleKlarStatusHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        VeksleKlarStatusHandler(
            klarTilSendingPolicy = klarTilSendingPolicy,
            redigerBrevPolicy = redigerBrevPolicy
        ).runHandler(request)

    suspend fun endreDistribusjonstype(request: EndreDistribusjonstypeHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        EndreDistribusjonstypeHandler(
            redigerBrevPolicy = redigerBrevPolicy
        ).runHandler(request)

    suspend fun endreMottaker(request: EndreMottakerHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        EndreMottakerHandler(
            redigerBrevPolicy = redigerBrevPolicy,
            brevdataService = brevdataService
        ).runHandler(request)

    private suspend fun <Request : BrevredigeringRequest> BrevredigeringHandler<Request>.runHandler(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        if (requiresReservasjon(request)) {
            val principal = PrincipalInContext.require()

            val reservasjon = transaction(Connection.TRANSACTION_REPEATABLE_READ) {
                BrevredigeringEntity.findById(request.brevId)
                    ?.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
            }
            if (reservasjon == null) {
                return null
            } else if (reservasjon is Outcome.Failure) {
                return failure(reservasjon.error)
            }
        }

        return newSuspendedTransaction {
            handle(request)
        }
    }
}