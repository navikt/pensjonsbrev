package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.hentSignatur
import no.nav.pensjon.brev.skribenten.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import java.time.Instant
import java.time.temporal.ChronoUnit

class AttesterBrevHandler(
    private val attesterBrevPolicy: AttesterBrevPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val renderService: RenderService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
) : BrevredigeringHandler<AttesterBrevHandler.Request, Dto.Brevredigering> {

    data class Request(
        override val brevId: BrevId,
        val nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        val nyttRedigertbrev: Edit.Letter? = null,
        val frigiReservasjon: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val principal = PrincipalInContext.require()

        attesterBrevPolicy.kanAttestere(brev, principal).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        val signaturAttestant = brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn
            ?: principal.hentSignatur(navansattService)

        val pesysdata = brevdataService.hentBrevdata(
            brev = brev,
            signatur = SignerendeSaksbehandlere(
                saksbehandler = brev.redigertBrev.signatur.saksbehandlerNavn!!,
                attesterendeSaksbehandler = signaturAttestant
            )
        )

        if (request.nyeSaksbehandlerValg != null) {
            brev.saksbehandlerValg = request.nyeSaksbehandlerValg
        }
        if (request.nyttRedigertbrev != null) {
            brev.oppdaterRedigertBev(request.nyttRedigertbrev, principal.navIdent)
        }

        val rendretBrev = renderService.renderMarkup(brev, pesysdata)
        brev.mergeRendretBrev(rendretBrev.markup)

        brev.attester(principal.navIdent, signaturAttestant)
        brev.sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
        brev.sistRedigertAv = principal.navIdent

        if (request.frigiReservasjon) {
            brev.redigeresAv = null
        }

        return success(brev.toDto(brevreservasjonPolicy, rendretBrev.letterDataUsage))
    }

    override fun requiresReservasjon(request: Request) = true
}

