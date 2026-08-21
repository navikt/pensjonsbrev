package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.hentSignatur
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.common.getOrElse
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.LanguageCode

class OpprettBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val opprettBrevPolicy: OpprettBrevPolicy,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
) : OpprettBrevService {

    data class Request(
        val saksId: SaksId,
        val vedtaksId: VedtaksId?,
        val brevkode: RedigerbarBrevkode,
        val spraak: LanguageCode,
        val avsenderEnhetsId: EnhetId,
        val saksbehandlerValg: SaksbehandlervalgMap,
        val reserverForRedigering: Boolean = false,
        val mottaker: Dto.Mottaker? = null,
    )

    override suspend fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError> =
        brevtilgang.iTransaksjon {
            val principal = PrincipalInContext.require()

            val parametre = opprettBrevPolicy.kanOppretteBrev(request, principal).getOrElse { return@iTransaksjon failure(it) }

            val pesysData = brevdataService.hentBrevdata(
                saksId = request.saksId,
                vedtaksId = parametre.vedtaksId,
                brevkode = request.brevkode,
                avsenderEnhetsId = request.avsenderEnhetsId,
                mottaker = request.mottaker,
                signatur = SignerendeSaksbehandlere(saksbehandler = principal.hentSignatur(navansattService)),
            )

            val rendretBrev = brevmalService.renderMarkup(
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
                brev.reserverFor(principal.navIdent)
            }
            if (request.mottaker != null) {
                brev.settMottaker(request.mottaker, pesysData.felles.annenMottakerNavn)
            }

            success(brev.tilDto(rendretBrev.letterDataUsage))
        }
}
