package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SlettBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId

class SlettBrevHandler(
    private val slettBrevPolicy: SlettBrevPolicy,
) : BrevredigeringHandler<SlettBrevHandler.Request, Unit> {

    data class Request(override val brevId: BrevId) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Unit, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        slettBrevPolicy.kanSlette(brev).onError { return failure(it) }

        brev.delete()
        return success(Unit)
    }

    override fun requiresReservasjon(request: Request): Boolean = true
}

