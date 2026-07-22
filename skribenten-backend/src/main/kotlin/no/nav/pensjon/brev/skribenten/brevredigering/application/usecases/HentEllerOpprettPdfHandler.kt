package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import org.jetbrains.exposed.v1.jdbc.Database

class HentEllerOpprettPdfHandler(
    private val brevdataService: BrevdataService,
    private val renderService: RenderService,
    private val brevmalService: BrevmalService,
    private val hentP1DataHandler: HentP1DataHandler,
    database: Database,
) : TransactionHandler<HentEllerOpprettPdfHandler.Request, Dto.HentDocumentResult, Nothing>(database) {

    data class Request(
        override val brevId: BrevId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.HentDocumentResult, Nothing>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val document = brev.document

        val pesysBrevdata = brevdataService.hentBrevdata(brev).let { brevdata ->
            if (brev.brevkode.kode() == P1_BREVKODE) {
                hentP1DataHandler(HentP1DataHandler.Request(brevId = brev.id.value, saksId = brev.saksId))
                    ?.asSuccess()
                    ?.let { p1 -> brevdata.copy(brevdata = brevdata.brevdata.apply { put(P1_VEDLEGG_KEY, p1.value) }) }
                    ?: throw IllegalStateException("Fant ikke P1-data for brev ${brev.id.value}")
            } else {
                brevdata
            }
        }
        val nyBrevdataHash = Hash.read(pesysBrevdata)
        val nyVedleggHash = brev.vedleggHash

        // Sjekk om cachet pdf er oppdatert
        return if (document != null && document.redigertBrevHash == brev.redigertBrevHash && nyBrevdataHash == document.brevdataHash && nyVedleggHash == document.vedleggHash) {
            success(Dto.HentDocumentResult(document = document, rendretBrevErEndret = false))
        } else {
            // Sjekk om innholdet i brevet har endret seg. Kan skje om pesysdata har endret seg.
            // Grunnen til at vi kun sjekker blocks er at det er kun om det er endringer i selve innholdet at saksbehandler trenger å ta stilling til det.
            val rendretBrevErEndret = brevmalService.renderMarkup(brev, pesysBrevdata).let { rendretBrev ->
                brev.redigertBrev.updateEditedLetter(rendretBrev.markup).blocks != brev.redigertBrev.blocks
            }

            val pdfBytes = renderService.renderPdf(brev, pesysBrevdata)
            val newDocument = Dto.Document(
                pdf = pdfBytes,
                dokumentDato = pesysBrevdata.felles.dokumentDato,
                redigertBrevHash = brev.redigertBrevHash,
                brevdataHash = nyBrevdataHash,
                vedleggHash = nyVedleggHash
            )
            brev.document = newDocument
            success(Dto.HentDocumentResult(document = newDocument, rendretBrevErEndret = rendretBrevErEndret))
        }
    }
}

// Disse må være i sync med api-modellen
const val P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2"
const val P1_VEDLEGG_KEY = "p1Vedlegg"
