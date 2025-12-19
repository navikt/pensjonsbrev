package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.domain.BrevedigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Result
import no.nav.pensjon.brev.skribenten.usecase.UpdateLetterHandler
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class BrevredigeringFacade(
    val renderService: RenderService,
    val brevdataService: BrevdataService,
    val redigerBrevPolicy: RedigerBrevPolicy = RedigerBrevPolicy(),
    val brevreservasjonPolicy: BrevreservasjonPolicy = BrevreservasjonPolicy(),
) {

    suspend fun oppdaterBrev(cmd: UpdateLetterHandler.Request): Result<Dto.Brevredigering, BrevedigeringError>? =
        newSuspendedTransaction {
            UpdateLetterHandler(redigerBrevPolicy, brevreservasjonPolicy, renderService, brevdataService).handle(cmd)
        }

}