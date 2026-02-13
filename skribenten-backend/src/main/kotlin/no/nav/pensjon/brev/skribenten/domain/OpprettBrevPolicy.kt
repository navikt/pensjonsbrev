package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy.KanIkkeOppretteBrev.IkkeTilgangTilEnhet
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.usecase.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

class OpprettBrevPolicy(
    private val brevbakerService: BrevbakerService,
    private val navansattService: NavansattService,
) {

    suspend fun kanOppretteBrev(
        request: OpprettBrevHandler.Request,
        principal: UserPrincipal
    ): Outcome<Parametre, BrevredigeringError> {
        if (!navansattService.harTilgangTilEnhet(principal.navIdent.id, request.avsenderEnhetsId)) {
            return failure(IkkeTilgangTilEnhet(enhetsId = request.avsenderEnhetsId))
        }

        val template = brevbakerService.getRedigerbarTemplate(request.brevkode)
        if (template == null) {
            return failure(KanIkkeOppretteBrev.BrevmalFinnesIkke(request.brevkode))
        } else if (template.isVedtakKontekst && request.vedtaksId == null) {
            return failure(KanIkkeOppretteBrev.BrevmalKreverVedtaksId(request.brevkode))
        }

        return success(
            Parametre(
                vedtaksId = request.vedtaksId?.takeIf { template.isVedtakKontekst },
                brevtype = template.metadata.brevtype,
            )
        )
    }

    data class Parametre(
        val vedtaksId: VedtaksId?,
        val brevtype: LetterMetadata.Brevtype,
    )

    sealed interface KanIkkeOppretteBrev : BrevredigeringError {
        data class IkkeTilgangTilEnhet(val enhetsId: EnhetId) : KanIkkeOppretteBrev
        data class BrevmalFinnesIkke(val brevkode: Brevkode.Redigerbart) : KanIkkeOppretteBrev
        data class BrevmalKreverVedtaksId(val brevkode: Brevkode.Redigerbart) : KanIkkeOppretteBrev
    }
}

private val TemplateDescription.Redigerbar.isVedtakKontekst: Boolean
    get() = brevkontekst == TemplateDescription.Brevkontekst.VEDTAK