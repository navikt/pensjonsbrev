package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import java.time.Instant

class OpprettBrevHandler(
    private val opprettBrevPolicy: OpprettBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val renderService: RenderService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
) {
    data class Request(
        val saksId: SaksId,
        val vedtaksId: VedtaksId?,
        val brevkode: Brevkode.Redigerbart,
        val spraak: LanguageCode,
        val avsenderEnhetsId: EnhetId,
        val saksbehandlerValg: SaksbehandlerValg,
        val reserverForRedigering: Boolean = false,
        val mottaker: Dto.Mottaker? = null,
    )

    suspend fun handle(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError> {
        val principal = PrincipalInContext.require()

        val parametre = when (val res = opprettBrevPolicy.kanOppretteBrev(request, principal)) {
            is Outcome.Failure -> return failure(res.error)
            is Outcome.Success -> res.value
        }

        val pesysData = brevdataService.hentBrevdata(
            saksId = request.saksId,
            vedtaksId = parametre.vedtaksId,
            brevkode = request.brevkode,
            avsenderEnhetsId = request.avsenderEnhetsId,
            mottaker = request.mottaker,
            signatur = SignerendeSaksbehandlere(saksbehandler = principalSignatur(principal)),
        )

        val rendretBrev = renderService.renderMarkup(
            brevkode = request.brevkode,
            spraak = request.spraak,
            saksbehandlerValg = request.saksbehandlerValg,
            pesysData = pesysData,
        )

        val brev = BrevredigeringEntity.opprettBrev(
            saksId = request.saksId,
            vedtaksId = parametre.vedtaksId,
            opprettetAv = principal.navIdent,
            brevkode = request.brevkode,
            spraak = request.spraak,
            avsenderEnhetId = request.avsenderEnhetsId,
            saksbehandlerValg = request.saksbehandlerValg,
            redigertBrev = rendretBrev.markup.toEdit(),
            brevtype = parametre.brevtype,
        )

        if (request.reserverForRedigering) {
            brev.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
        }
        if (request.mottaker != null) {
            brev.settMottaker(request.mottaker, pesysData.felles.annenMottakerNavn)
        }

        return success(brev.toDto(rendretBrev.letterDataUsage))
    }

    private suspend fun principalSignatur(principal: UserPrincipal): String =
        navansattService.hentNavansatt(principal.navIdent.id)?.fulltNavn ?: principal.fullName
}
