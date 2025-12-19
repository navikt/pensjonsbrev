package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.domain.BrevedigeringError
import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
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
    data class Command(
        val brevId: Long,
        val saksbehandler: NavIdent,
        val nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        val nyttRedigertbrev: Edit.Letter? = null,
        val frigiReservasjon: Boolean = false,
    )

    suspend fun handle(cmd: Command): Result<Dto.Brevredigering, BrevedigeringError>? {
        val brev = Brevredigering.findById(cmd.brevId) ?: return null

        brev.reserver(Instant.now(), cmd.saksbehandler, brevreservasjonPolicy).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, cmd.saksbehandler).onError { return failure(it) }

        if (cmd.nyeSaksbehandlerValg != null) {
            brev.saksbehandlerValg = cmd.nyeSaksbehandlerValg
        }
        if (cmd.nyttRedigertbrev != null) {
            brev.oppdaterRedigertBev(cmd.nyttRedigertbrev, cmd.saksbehandler)
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val rendretBrev = renderService.renderMarkup(brev, pesysdata)
        brev.mergeRendretBrev(rendretBrev.markup)

        if (cmd.frigiReservasjon) {
            brev.redigeresAv = null
        }

        return success(brev.toDto(rendretBrev.letterDataUsage))
    }
}
