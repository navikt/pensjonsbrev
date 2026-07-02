package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import org.jetbrains.exposed.v1.jdbc.Database

class SlettBrevHandler(
    brevreservasjonPolicy: BrevreservasjonPolicy,
    database: Database,
) : ReservertBrevHandler<SlettBrevHandler.Request, Unit>(database, brevreservasjonPolicy) {

    data class Request(override val brevId: BrevId) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Unit, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        brev.delete()
        return success(Unit)
    }
}

