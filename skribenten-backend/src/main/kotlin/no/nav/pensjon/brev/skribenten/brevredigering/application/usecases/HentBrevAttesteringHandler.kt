package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.*
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.NavansattService
import org.jetbrains.exposed.v1.jdbc.Database

class HentBrevAttesteringHandler(
    private val attesterBrevPolicy: AttesterBrevPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<HentBrevAttesteringHandler.Request, Dto.Brevredigering>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        val reserverForRedigering: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

        if (!request.reserverForRedigering) {
            return success(brev.toDto(brevreservasjonPolicy, null))
        }

        val principal = PrincipalInContext.require()
        attesterBrevPolicy.kanAttestere(brev, principal).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        if (brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn == null) {
            brev.oppdaterRedigertBrev(brev.redigertBrev.withSignaturAttestant(principal.hentSignatur(navansattService)), principal.navIdent)
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)
        brev.mergeRendretBrev(rendretBrev.markup)

        return success(brev.toDto(brevreservasjonPolicy, rendretBrev.letterDataUsage))
    }

    override fun requiresReservasjon(request: Request): Boolean =
        request.reserverForRedigering
}
