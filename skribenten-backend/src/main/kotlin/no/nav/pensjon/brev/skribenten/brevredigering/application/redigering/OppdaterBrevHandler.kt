package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertBrev
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Companion.fase
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability.Fase

class OppdaterBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val nyeSaksbehandlerValg: SaksbehandlervalgMap? = null,
        val nyttRedigertbrev: VersjonertBrev? = null,
        val frigiReservasjon: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        brevtilgang.forRedigering(request.brevId, request.saksId, frigiReservasjon = request.frigiReservasjon) {
            fase(Fase.VERDIER) {
                if (request.nyeSaksbehandlerValg != null) {
                    brev.saksbehandlerValg = request.nyeSaksbehandlerValg
                }
                if (request.nyttRedigertbrev != null) {
                    brev.oppdaterRedigertBrev(request.nyttRedigertbrev, PrincipalInContext.require().navIdent)
                }
            }

            val pesysdata = fase(Fase.BREVDATA) { brevdataService.hentBrevdata(brev) }
            val rendretBrev = fase(Fase.RENDER_BREV) { brevmalService.renderMarkup(brev, pesysdata) }
            fase(Fase.FLETT_BREV) { brev.mergeRendretBrev(rendretBrev.markup) }
            val vedlegg = fase(Fase.RENDER_VEDLEGG) { brevmalService.renderRedigerteVedlegg(brev, pesysdata) }
            fase(Fase.FLETT_VEDLEGG) { brev.mergeRendredeVedlegg(vedlegg) }

            success(fase(Fase.DTO) { brev.tilDto(rendretBrev.letterDataUsage) })
        }
}
