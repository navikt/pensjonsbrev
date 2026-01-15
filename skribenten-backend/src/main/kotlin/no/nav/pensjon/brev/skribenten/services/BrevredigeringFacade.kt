package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.CreateLetterHandler
import no.nav.pensjon.brev.skribenten.usecase.Result
import no.nav.pensjon.brev.skribenten.usecase.UpdateLetterHandler
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class BrevredigeringFacade(
    brevbakerService: BrevbakerService,
    val brevdataService: BrevdataService,
    val navansattService: NavansattService,
    val renderService: RenderService,
    val redigerBrevPolicy: RedigerBrevPolicy = RedigerBrevPolicy(),
    val brevreservasjonPolicy: BrevreservasjonPolicy = BrevreservasjonPolicy(),
    val opprettBrevPolicy: OpprettBrevPolicy = OpprettBrevPolicy(brevbakerService, navansattService),
) {

    suspend fun oppdaterBrev(request: UpdateLetterHandler.Request): Result<Dto.Brevredigering, BrevredigeringError>? =
        newSuspendedTransaction {
            UpdateLetterHandler(redigerBrevPolicy, brevreservasjonPolicy, renderService, brevdataService).handle(request)
        }

    suspend fun opprettBrev(request: CreateLetterHandler.Request): Result<Dto.Brevredigering, BrevredigeringError> =
        newSuspendedTransaction {
            CreateLetterHandler(
                opprettBrevPolicy = opprettBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
                renderService = renderService,
                brevdataService = brevdataService,
                navansattService = navansattService,
            ).handle(request)
        }
}