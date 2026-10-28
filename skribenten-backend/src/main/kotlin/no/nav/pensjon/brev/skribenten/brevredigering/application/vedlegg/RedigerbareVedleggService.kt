package no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg

import no.nav.pensjon.brev.skribenten.brevredigering.application.redigering.OppdaterBrevService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VedleggFinnesIkkeIMal
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.letter.updateVariables
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class RedigerbareVedleggService(
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    private val oppdaterBrevService: OppdaterBrevService,
) {

    suspend fun hentTitler(brev: BrevredigeringEntity, mergeMotMal: Boolean): List<RedigerbartVedleggInfo>? {
        if (!brevmalService.harRedigerbareVedlegg(brev.brevkode)) {
            return emptyList()
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        if (mergeMotMal) {
            val rendredeVedlegg = brevmalService.renderRedigerteVedlegg(brev, pesysdata)
            if (oppdaterBrevService.skalKunOppdatereVariabler(brev, pesysdata)) {
                brev.oppdaterVariablerIVedlegg(rendredeVedlegg)
            } else {
                brev.mergeRendredeVedlegg(rendredeVedlegg)
            }
        }

        return brevmalService.hentRedigerbareVedleggTitler(brev, pesysdata)?.vedlegg
            ?.map { vedlegg ->
                RedigerbartVedleggInfo(
                    vedleggId = vedlegg.id,
                    tittel = brev.hentRedigertVedlegg(vedlegg.id)?.title?.text?.format() ?: vedlegg.tittel,
                )
            }
    }

    suspend fun hent(
        brev: BrevredigeringEntity,
        vedleggId: VedleggId,
        mergeMotMal: Boolean,
    ): Outcome<Edit.Attachment, VedleggFinnesIkkeIMal> {
        val lagretVedlegg = brev.hentRedigertVedlegg(vedleggId)
        if (!mergeMotMal && lagretVedlegg != null) {
            return success(lagretVedlegg)
        }

        val pesysdata = brevdataService.hentBrevdata(brev)
        val malVedlegg = renderMal(brev, pesysdata, vedleggId) ?: return failure(VedleggFinnesIkkeIMal(brev.id.value, vedleggId))
        return success(lagretVedlegg?.oppdaterMotMal(brev, pesysdata, malVedlegg) ?: malVedlegg.toEdit())
    }

    suspend fun lagre(
        brev: BrevredigeringEntity,
        vedleggId: VedleggId,
        redigertVedlegg: Edit.Attachment,
        mergeMotMal: Boolean,
    ): Outcome<Edit.Attachment, VedleggFinnesIkkeIMal> {
        val pesysdata = brevdataService.hentBrevdata(brev)
        val malVedlegg = renderMal(brev, pesysdata, vedleggId) ?: return failure(VedleggFinnesIkkeIMal(brev.id.value, vedleggId))

        val nyttVedlegg = if (mergeMotMal) redigertVedlegg.oppdaterMotMal(brev, pesysdata, malVedlegg) else redigertVedlegg
        brev.settRedigertVedlegg(vedleggId, nyttVedlegg)

        return success(nyttVedlegg)
    }

    suspend fun tilbakestill(brev: BrevredigeringEntity, vedleggId: VedleggId): Outcome<Edit.Attachment, VedleggFinnesIkkeIMal> {
        val malVedlegg = renderMal(brev, brevdataService.hentBrevdata(brev), vedleggId)
            ?: return failure(VedleggFinnesIkkeIMal(brev.id.value, vedleggId))

        brev.slettRedigertVedlegg(vedleggId)

        return success(malVedlegg.toEdit())
    }

    private suspend fun Edit.Attachment.oppdaterMotMal(
        brev: BrevredigeringEntity,
        pesysdata: BrevdataResponse.Data,
        malVedlegg: LetterMarkup.Attachment,
    ): Edit.Attachment =
        if (oppdaterBrevService.skalKunOppdatereVariabler(brev, pesysdata)) {
            updateVariables(malVedlegg)
        } else {
            updateEditedAttachment(malVedlegg)
        }

    private suspend fun renderMal(brev: BrevredigeringEntity, pesysdata: BrevdataResponse.Data, vedleggId: VedleggId): LetterMarkup.Attachment? =
        brevmalService.renderRedigerbartVedlegg(brev, pesysdata, vedleggId)
}

private fun List<Edit.ParagraphContent.Text>.format(): String =
    joinToString("") { if (it is Edit.ParagraphContent.Text.Literal) it.editedText ?: it.text else it.text }

data class RedigerbartVedleggInfo(
    val vedleggId: VedleggId,
    val tittel: String,
)
