package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import org.jetbrains.exposed.v1.jdbc.Database

class SlettBrevHandler(
    reserverBrevHandler: ReserverBrevHandler,
    slettBrevPolicy: SlettBrevPolicy,
    database: Database,
) : ReservertBrevHandler<SlettBrevHandler.Request, Unit>(database, reserverBrevHandler) {

    data class Request(override val brevId: BrevId, override val saksId: SaksId) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Unit, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        slettBrevPolicy.kanSlette(brev).onError { return failure(it) }

        brev.delete()
        return success(Unit)
    }
}

