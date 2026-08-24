package no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

class HentRedigerbareVedleggHandler(
    private val brevtilgang: Brevtilgang,
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
    )

    suspend operator fun invoke(request: Request): Outcome<List<RedigerbartVedleggInfo>, Nothing>? =
        brevtilgang.forLesing(request.brevId, request.saksId) {
            // Trenger ikke å gå videre med tyngre kall om det ikke er noe redigerbare vedlegg på malen.
            if (!brevmalService.harRedigerbareVedlegg(brev.brevkode)) {
                return@forLesing success(emptyList())
            }

            val pesysdata = brevdataService.hentBrevdata(brev)
            val vedlegg = brevmalService.hentRedigerbareVedleggTitler(brev, pesysdata)?.vedlegg
                ?.map { vedlegg ->
                    // Bruk den redigerte tittelen dersom saksbehandler har overstyrt vedlegget, ellers maltittelen.
                    val redigertTittel = brev.hentRedigertVedlegg(vedlegg.id)?.title?.text
                    RedigerbartVedleggInfo(
                        vedleggId = vedlegg.id,
                        tittel = redigertTittel?.format() ?: vedlegg.tittel,
                    )
                } ?: return@forLesing null

            success(vedlegg)
        }
}

private fun List<Edit.ParagraphContent.Text>?.format() = this?.joinToString("") {
    if (it is Edit.ParagraphContent.Text.Literal) it.editedText ?: it.text else it.text
}

data class RedigerbartVedleggInfo(
    val vedleggId: VedleggId,
    val tittel: String,
)
