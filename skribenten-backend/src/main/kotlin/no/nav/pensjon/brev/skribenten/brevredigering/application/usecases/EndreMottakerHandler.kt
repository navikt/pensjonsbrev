package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.model.*
import org.jetbrains.exposed.v1.jdbc.Database

class EndreMottakerHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevdataService: BrevdataService,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<EndreMottakerHandler.Request, Dto.BrevInfo>(database, reserverBrevHandler) {

    data class Request(override val brevId: BrevId, val mottaker: Dto.Mottaker?) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        val principal = PrincipalInContext.require()
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        brev.settMottaker(request.mottaker, request.mottaker?.hentAnnenMottakerNavn())
        brev.frigiReservasjon()

        return success(brev.toBrevInfo(brevreservasjonPolicy))
    }

    private suspend fun Dto.Mottaker.hentAnnenMottakerNavn(): String? =
        brevdataService.hentAnnenMottakerNavn(this)

}