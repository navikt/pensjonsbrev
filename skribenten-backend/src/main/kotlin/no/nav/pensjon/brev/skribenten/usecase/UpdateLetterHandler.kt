package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.domain.BrevedigeringError
import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.services.toDto
import no.nav.pensjon.brev.skribenten.usecase.Result.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Result.Companion.success
import java.time.Instant

class UpdateLetterHandler(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val renderService: RenderService,
    private val brevdataService: BrevdataService,
) {
    data class Request(
        val brevId: Long,
        val nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        val nyttRedigertbrev: Edit.Letter? = null,
        val frigiReservasjon: Boolean = false,
    )

    suspend fun handle(cmd: Request): Result<Dto.Brevredigering, BrevedigeringError>? = with(cmd) {
        val brev = Brevredigering.findById(brevId) ?: return null
        val principal = PrincipalInContext.require()

        brev.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        if (nyeSaksbehandlerValg != null) {
            brev.saksbehandlerValg = nyeSaksbehandlerValg
        }
        if (nyttRedigertbrev != null) {
            brev.oppdaterRedigertBev(nyttRedigertbrev, principal.navIdent)
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = renderService.renderMarkup(brev, pesysdata)
        brev.mergeRendretBrev(rendretBrev.markup)

        if (frigiReservasjon) {
            brev.redigeresAv = null
        }

        return success(brev.toDto(rendretBrev.letterDataUsage))
    }
}
