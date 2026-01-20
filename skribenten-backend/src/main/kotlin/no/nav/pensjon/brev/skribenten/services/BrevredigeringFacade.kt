package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.KlarTilSendingPolicy
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.EndreDistribusjonstypeHandler
import no.nav.pensjon.brev.skribenten.usecase.EndreMottakerHandler
import no.nav.pensjon.brev.skribenten.usecase.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.usecase.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.usecase.VeksleKlarStatusHandler
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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
        newSuspendedTransaction {
            OppdaterBrevHandler(redigerBrevPolicy, brevreservasjonPolicy, renderService, brevdataService).handle(request)
        }

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

    suspend fun veksleKlarStatus(request: VeksleKlarStatusHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        newSuspendedTransaction {
            VeksleKlarStatusHandler(klarTilSendingPolicy, redigerBrevPolicy, brevreservasjonPolicy).handle(request)
        }

    suspend fun endreDistribusjonstype(request: EndreDistribusjonstypeHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        newSuspendedTransaction {
            EndreDistribusjonstypeHandler(redigerBrevPolicy, brevreservasjonPolicy).handle(request)
        }

    suspend fun endreMottaker(request: EndreMottakerHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        newSuspendedTransaction {
            EndreMottakerHandler(brevreservasjonPolicy, redigerBrevPolicy, brevdataService).endreMottaker(request)
        }
}