package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Result.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Result.Companion.success
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import java.time.Instant

/**
 * Handler for creating a new Brevredigering, mirroring the pattern used by UpdateLetterHandler.
 */
class CreateLetterHandler(
    private val opprettBrevPolicy: OpprettBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val renderService: RenderService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
) {
    data class Request(
        val saksId: Long,
        val vedtaksId: Long?,
        val brevkode: Brevkode.Redigerbart,
        val spraak: LanguageCode,
        val avsenderEnhetsId: String?,
        val saksbehandlerValg: SaksbehandlerValg,
        val reserverForRedigering: Boolean = false,
        val mottaker: Dto.Mottaker? = null,
    )

    suspend fun handle(req: Request): Result<Dto.Brevredigering, BrevredigeringError> {
        val principal = PrincipalInContext.require()

        val parametre = when (val res = opprettBrevPolicy.kanOppretteBrev(req, principal)) {
            is Result.Failure -> return failure(res.error)
            is Result.Success -> res.value
        }

        val pesysData = brevdataService.hentBrevdata(
            saksId = req.saksId,
            vedtaksId = parametre.vedtaksId,
            brevkode = req.brevkode,
            avsenderEnhetsId = req.avsenderEnhetsId,
            mottaker = req.mottaker,
            signatur = SignerendeSaksbehandlere(saksbehandler = principalSignatur(principal)),
        )

        val rendretBrev = renderService.renderMarkup(
            brevkode = req.brevkode,
            spraak = req.spraak,
            saksbehandlerValg = req.saksbehandlerValg,
            pesysData = pesysData,
        )

        val brev = Brevredigering.opprettBrev(
            saksId = req.saksId,
            vedtaksId = parametre.vedtaksId,
            opprettetAv = principal.navIdent,
            brevkode = req.brevkode,
            spraak = req.spraak,
            avsenderEnhetId = req.avsenderEnhetsId,
            saksbehandlerValg = req.saksbehandlerValg,
            redigertBrev = rendretBrev.markup.toEdit(),
            brevtype = parametre.brevtype,
        )

        if (req.reserverForRedigering) {
            brev.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
        }
        if (req.mottaker != null) {
            brev.settMottaker(req.mottaker)
        }

        return success(brev.toDto(rendretBrev.letterDataUsage))
    }

    private suspend fun principalSignatur(principal: UserPrincipal): String =
        navansattService.hentNavansatt(principal.navIdent.id)?.let { "${it.fornavn} ${it.etternavn}" } ?: principal.fullName
}
