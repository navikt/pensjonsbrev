package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

class HentRedigerbareVedleggHandler(
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
) : BrevredigeringHandler<HentRedigerbareVedleggHandler.Request, List<RedigerbartVedleggInfo>> {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<List<RedigerbartVedleggInfo>, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        // Trenger ikke å gå videre med tyngre kall om det ikke er noe redigerbare vedlegg på malen.
        if (!brevmalService.harRedigerbareVedlegg(brev.brevkode)) {
            return success(emptyList())
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
            }?: return null

        return success(vedlegg)
    }

    override fun requiresReservasjon(request: Request) = false
}

private fun List<Edit.ParagraphContent.Text>?.format() = this?.joinToString("") { it.text }
data class RedigerbartVedleggInfo(
    val vedleggId: VedleggId,
    val tittel: String,
)
