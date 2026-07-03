package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.*
import org.jetbrains.exposed.v1.jdbc.Database

class OppdaterBrevHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<OppdaterBrevHandler.Request, Dto.Brevredigering>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        val nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        val nyttRedigertbrev: Edit.Letter? = null,
        val frigiReservasjon: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val principal = PrincipalInContext.require()

        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        if (request.nyeSaksbehandlerValg != null) {
            brev.saksbehandlerValg = request.nyeSaksbehandlerValg
        }
        if (request.nyttRedigertbrev != null) {
            brev.oppdaterRedigertBrev(request.nyttRedigertbrev, principal.navIdent)
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)
        brev.mergeRendretBrev(rendretBrev.markup)

        if (request.frigiReservasjon) {
            brev.frigiReservasjon()
        }

        return success(brev.toDto(brevreservasjonPolicy, rendretBrev.letterDataUsage))
    }
}
