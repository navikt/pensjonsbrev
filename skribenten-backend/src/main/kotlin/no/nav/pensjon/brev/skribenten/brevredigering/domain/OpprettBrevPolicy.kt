package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.api.model.TemplateDescription.Redigerbar
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.OpprettBrevPolicy.KanIkkeOppretteBrev.IkkeTilgangTilEnhet
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

class OpprettBrevPolicy(
    private val brevmalService: BrevmalService,
    private val navansattService: NavansattService,
) {

    suspend fun kanOppretteBrev(
        brevkode: RedigerbarBrevkode,
        vedtaksId: VedtaksId?,
        avsenderEnhetsId: EnhetId,
        principal: UserPrincipal,
    ): Outcome<Parametre, BrevredigeringError> {
        if (!navansattService.harTilgangTilEnhet(principal.navIdent, avsenderEnhetsId)) {
            return failure(IkkeTilgangTilEnhet(enhetsId = avsenderEnhetsId))
        }

        val template = brevmalService.getRedigerbarTemplate(brevkode)
        if (template == null) {
            return failure(BrevmalFinnesIkke(brevkode))
        } else if (template.isVedtakKontekst && vedtaksId == null) {
            return failure(KanIkkeOppretteBrev.BrevmalKreverVedtaksId(brevkode))
        }

        return success(
            Parametre(
                vedtaksId = vedtaksId?.takeIf { template.isVedtakKontekst },
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
        data class BrevmalKreverVedtaksId(val brevkode: Brevkode.Redigerbart) : KanIkkeOppretteBrev
    }
}

private val TemplateDescription.Redigerbar.isVedtakKontekst: Boolean
    get() = brevkontekst == TemplateDescription.Brevkontekst.VEDTAK