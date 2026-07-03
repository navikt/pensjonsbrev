package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.hentSignatur
import no.nav.pensjon.brev.skribenten.brevredigering.application.OpprettBrevService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
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
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.time.Instant

class OpprettBrevHandler(
    private val opprettBrevPolicy: OpprettBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    private val navansattService: NavansattService,
    private val database: Database,
) : UseCaseHandler<OpprettBrevHandler.Request, Dto.Brevredigering, BrevredigeringError>, OpprettBrevService {
    data class Request(
        val saksId: SaksId,
        val vedtaksId: VedtaksId?,
        val brevkode: RedigerbarBrevkode,
        val spraak: LanguageCode,
        val avsenderEnhetsId: EnhetId,
        val saksbehandlerValg: SaksbehandlerValg,
        val reserverForRedigering: Boolean = false,
        val mottaker: Dto.Mottaker? = null,
    )

    override suspend fun invoke(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError> =
        suspendTransaction(db = database) {
            execute(request)
        }

    private suspend fun execute(request: Request): Outcome<Dto.Brevredigering, BrevredigeringError> {
        val principal = PrincipalInContext.require()

        val parametre = opprettBrevPolicy.kanOppretteBrev(request, principal).getOrElse { return failure(it) }

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
            brev.reserver(Instant.now(), principal.navIdent, brevreservasjonPolicy)
        }
        if (request.mottaker != null) {
            brev.settMottaker(request.mottaker, pesysData.felles.annenMottakerNavn)
        }

        return success(brev.toDto(brevreservasjonPolicy, rendretBrev.letterDataUsage))
    }
}
