package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.jetbrains.exposed.v1.jdbc.Database

class HentRedigerbareVedleggHandler(
    private val brevmalService: BrevmalService,
    private val brevdataService: BrevdataService,
    database: Database,
) : TransactionHandler<HentRedigerbareVedleggHandler.Request, List<RedigerbartVedleggInfo>, Nothing>(database) {

    data class Request(
        override val brevId: BrevId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<List<RedigerbartVedleggInfo>, Nothing>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null

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
}

private fun List<Edit.ParagraphContent.Text>?.format() = this?.joinToString("") { it.text }
data class RedigerbartVedleggInfo(
    val vedleggId: VedleggId,
    val tittel: String,
)
