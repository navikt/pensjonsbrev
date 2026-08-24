package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.hentSignatur
import no.nav.pensjon.brev.skribenten.brevredigering.application.redigering.oppdaterOgRender
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FerdigRedigertPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.RedigerbarSaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.NavansattService

class AttesterBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val ferdigRedigertPolicy: FerdigRedigertPolicy,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val nyeSaksbehandlerValg: RedigerbarSaksbehandlervalgMap? = null,
        val nyttRedigertbrev: Edit.Letter? = null,
        val frigiReservasjon: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        brevtilgang.forAttestering(request.brevId, request.saksId, frigiReservasjon = request.frigiReservasjon) {
            val rendretBrev = oppdaterOgRender(request.nyeSaksbehandlerValg, request.nyttRedigertbrev, brevdataService, brevmalService)

            ferdigRedigertPolicy.erFerdigRedigert(brev).onError { return@forAttestering failure(it) }

            if (!brev.laastForRedigering) {
                brev.markerSomKlar()
            }

            val principal = PrincipalInContext.require()
            val attestantSignatur = brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn
                ?: principal.hentSignatur(navansattService)
            brev.attester(principal.navIdent, attestantSignatur)

            success(brev.tilDto(rendretBrev.letterDataUsage))
        }
}
