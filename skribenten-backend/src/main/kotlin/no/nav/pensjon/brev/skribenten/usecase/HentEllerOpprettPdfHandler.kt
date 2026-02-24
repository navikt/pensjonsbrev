package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.BrevdataResponse
import no.nav.pensjon.brev.skribenten.services.P1Service
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success

class HentEllerOpprettPdfHandler(
    private val brevdataService: BrevdataService,
    private val renderService: RenderService,
    private val p1Service: P1Service,
) : BrevredigeringHandler<HentEllerOpprettPdfHandler.Request, Dto.HentDocumentResult> {

    data class Request(
        override val brevId: BrevId,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.HentDocumentResult, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val document = brev.document

        val pesysBrevdata = brevdataService.hentBrevdata(brev).withP1DataIfP1(brev)
        val nyBrevdataHash = Hash.read(pesysBrevdata)

        // Sjekk om cachet pdf er oppdatert
        return if (document != null && document.redigertBrevHash == brev.redigertBrevHash && nyBrevdataHash == document.brevdataHash) {
            success(Dto.HentDocumentResult(document = document, rendretBrevErEndret = false))
        } else {
            // Sjekk om innholdet i brevet har endret seg. Kan skje om pesysdata har endret seg.
            // Grunnen til at vi kun sjekker blocks er at det er kun om det er endringer i selve innholdet at saksbehandler trenger å ta stilling til det.
            val rendretBrevErEndret = renderService.renderMarkup(brev, pesysBrevdata).let { rendretBrev ->
                brev.redigertBrev.updateEditedLetter(rendretBrev.markup).blocks != brev.redigertBrev.blocks
            }

            val pdfBytes = renderService.renderPdf(brev, pesysBrevdata)
            val newDocument = Dto.Document(
                pdf = pdfBytes,
                dokumentDato = pesysBrevdata.felles.dokumentDato,
                redigertBrevHash = brev.redigertBrevHash,
                brevdataHash = nyBrevdataHash
            )
            brev.document = newDocument
            success(Dto.HentDocumentResult(document = newDocument, rendretBrevErEndret = rendretBrevErEndret))
        }
    }

    override fun requiresReservasjon(request: Request): Boolean = false

    private suspend fun BrevdataResponse.Data.withP1DataIfP1(brev: Brevredigering): BrevdataResponse.Data =
        p1Service.patchMedP1DataOmP1(
            brevdataResponse = this,
            brevkode = brev.brevkode,
            brevId = brev.id.value,
            saksId = brev.saksId
        )
}


