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

class LagreAttestertBrevHandler(
    private val attesterBrevPolicy: AttesterBrevPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<LagreAttestertBrevHandler.Request, Dto.Brevredigering>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val nyttRedigertbrev: Edit.Letter,
        val frigiReservasjon: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null
        val principal = PrincipalInContext.require()

        attesterBrevPolicy.kanAttestere(brev, principal).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        brev.oppdaterRedigertBrev(request.nyttRedigertbrev, principal.navIdent)

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)
        brev.oppdaterSakspartOgSignatur(rendretBrev.markup)

        if (request.frigiReservasjon) {
            brev.frigiReservasjon()
        }

        return success(brev.toDto(brevreservasjonPolicy, rendretBrev.letterDataUsage))
    }
}
