package no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VedleggFinnesIkkeIMal
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertVedleggMarkup
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertVedlegg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

class RedigerbareVedleggService(
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) {

    suspend fun hentTitler(brev: BrevredigeringEntity, mergeMotMal: Boolean): List<RedigerbartVedleggInfo>? {
        if (!brevmalService.harRedigerbareVedlegg(brev.brevkode)) {
            return emptyList()
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val kanIkkeFlettes = if (mergeMotMal) {
            val rendredeVedlegg = brevmalService.renderRedigerteVedlegg(brev, pesysdata)
            if (Features.forhindreOppdateringAvKladd.isEnabled()) {
                val endredeMaler = rendredeVedlegg
                    .filter { (vedleggId, rendret) -> !brev.harUendretVedleggmal(vedleggId, rendret.stableHashCode) }
                    .keys
                brev.mergeRendredeVedleggMedUendretHash(rendredeVedlegg)
                endredeMaler
            } else {
                brev.mergeRendredeVedlegg(rendredeVedlegg)
                emptySet()
            }
        } else {
            emptySet()
        }

        return brevmalService.hentRedigerbareVedleggTitler(brev, pesysdata)?.vedlegg
            ?.map { vedlegg ->
                RedigerbartVedleggInfo(
                    vedleggId = vedlegg.id,
                    tittel = brev.hentRedigertVedlegg(vedlegg.id)?.tittel ?: vedlegg.tittel,
                    kanFletteEndringer = vedlegg.id !in kanIkkeFlettes,
                )
            }
    }

    suspend fun hent(
        brev: BrevredigeringEntity,
        vedleggId: VedleggId,
        mergeMotMal: Boolean,
    ): Outcome<VersjonertVedlegg, VedleggFinnesIkkeIMal> {
        val lagretVedlegg = brev.hentRedigertVedlegg(vedleggId)
        if (!mergeMotMal && lagretVedlegg != null) {
            return success(lagretVedlegg)
        }

        val malVedlegg = renderMal(brev, vedleggId) ?: return failure(VedleggFinnesIkkeIMal(brev.id.value, vedleggId))
        return when {
            lagretVedlegg == null -> success(malVedlegg.toEdit())
            skalFlette(brev, vedleggId, malVedlegg) -> success(lagretVedlegg.merge(malVedlegg))
            else -> success(lagretVedlegg)
        }
    }

    suspend fun lagre(
        brev: BrevredigeringEntity,
        vedleggId: VedleggId,
        redigertVedlegg: VersjonertVedlegg,
        mergeMotMal: Boolean,
    ): Outcome<VersjonertVedlegg, VedleggFinnesIkkeIMal> {
        val malVedlegg = renderMal(brev, vedleggId) ?: return failure(VedleggFinnesIkkeIMal(brev.id.value, vedleggId))

        val nyttVedlegg = if (mergeMotMal && skalFlette(brev, vedleggId, malVedlegg)) {
            redigertVedlegg.merge(malVedlegg)
        } else {
            redigertVedlegg
        }
        val foersteLagring = brev.hentRedigertVedlegg(vedleggId) == null
        brev.settRedigertVedlegg(vedleggId, nyttVedlegg)
        if (foersteLagring) {
            brev.settBrevmalForRedigertVedlegg(vedleggId, malVedlegg)
        }

        return success(nyttVedlegg)
    }

    suspend fun tilbakestill(brev: BrevredigeringEntity, vedleggId: VedleggId): Outcome<VersjonertVedlegg, VedleggFinnesIkkeIMal> {
        val malVedlegg = renderMal(brev, vedleggId) ?: return failure(VedleggFinnesIkkeIMal(brev.id.value, vedleggId))

        brev.slettRedigertVedlegg(vedleggId)

        return success(malVedlegg.toEdit())
    }

    private suspend fun skalFlette(brev: BrevredigeringEntity, vedleggId: VedleggId, malVedlegg: LetterMarkup.Attachment): Boolean =
        !Features.forhindreOppdateringAvKladd.isEnabled() || brev.harUendretVedleggmal(vedleggId, malVedlegg.stableHashCode)

    private suspend fun renderMal(brev: BrevredigeringEntity, vedleggId: VedleggId): VersjonertVedleggMarkup? =
        brevmalService.renderRedigerbartVedlegg(brev, brevdataService.hentBrevdata(brev), vedleggId)
}

data class RedigerbartVedleggInfo(
    val vedleggId: VedleggId,
    val tittel: String,
    val kanFletteEndringer: Boolean,
)
