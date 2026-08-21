package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.hentSignatur
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.*
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.NavansattService

class HentBrevAttesteringHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val reserverForRedigering: Boolean = false,
    )

    suspend operator fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        if (!request.reserverForRedigering) {
            brevtilgang.forLesing(request.brevId, request.saksId) { success(brev.tilDto()) }
        } else {
            brevtilgang.forAttestering(request.brevId, request.saksId) {
                val principal = PrincipalInContext.require()

                if (brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn == null) {
                    brev.oppdaterRedigertBrev(brev.redigertBrev.withSignaturAttestant(principal.hentSignatur(navansattService)), principal.navIdent)
                }

                val pesysdata = brevdataService.hentBrevdata(brev)
                val rendretBrev = brevmalService.renderMarkup(brev, pesysdata)
                brev.mergeRendretBrev(rendretBrev.markup)

                success(brev.tilDto(rendretBrev.letterDataUsage))
            }
        }
}
