package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.GenererFoerstesideHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentP1DataHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.P1_BREVKODE
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.P1_VEDLEGG_KEY
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.IngenFoersteside
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.Fagsak
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.PDFMerger
import no.nav.pensjon.brev.skribenten.letter.updateEditedAttachment
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto
import no.nav.pensjon.brev.skribenten.vedlegg.PDFVedleggAppender
import no.nav.pensjon.brev.skribenten.vedlegg.PDFVedleggSkribenten
import no.nav.pensjon.brev.skribenten.vedlegg.SideAppender
import no.nav.pensjon.brev.skribenten.vedlegg.vedleggsliste
import no.nav.pensjon.brevbaker.api.model.PDFVedleggTittel

class BrevPdfService(
    private val brevdataService: BrevdataService,
    private val renderService: RenderService,
    private val brevmalService: BrevmalService,
    private val hentP1DataHandler: HentP1DataHandler,
    private val genererFoerstesideHandler: GenererFoerstesideHandler,
    private val pdfVedleggAppender: PDFVedleggAppender,
) {

    suspend fun hentEllerOpprett(
        brev: BrevredigeringEntity,
        fagsak: Fagsak,
        sjekkOmRendretBrevErEndret: Boolean,
    ): Outcome<Dto.HentDocumentResult, IngenFoersteside> {
        val document = brev.document
        val pesysBrevdata = hentPesysBrevdata(brev)
        val nyBrevdataHash = Hash.read(pesysBrevdata)
        val nyVedleggHash = brev.vedleggHash

        if (document != null && document.redigertBrevHash == brev.redigertBrevHash && nyBrevdataHash == document.brevdataHash && nyVedleggHash == document.vedleggHash) {
            return success(Dto.HentDocumentResult(document = document, rendretBrevErEndret = false))
        }

        val rendretBrevErEndret = sjekkOmRendretBrevErEndret && rendretBrevErEndret(brev, pesysBrevdata)
        val pdfVedlegg = vedleggsliste[brev.brevkode.kode()] ?: emptyList()

        val pdfBytes = renderService.renderPdf(
            brev,
            pesysBrevdata,
            pdfVedlegg = pdfVedlegg.mapNotNull { v -> v.tittel[brev.spraak]?.let { PDFVedleggTittel(it) } }
        ).let {
            leggVedPDFVedlegg(pdfVedlegg, pesysBrevdata, it, brev)
        }.let { rendretBrev ->
            if (Features.foersteside.isEnabled() && brev.leggVedFoersteside == true) {
                genererFoersteside(brev, fagsak, rendretBrev) ?: return Outcome.failure(IngenFoersteside(brev.id.value))
            } else {
                rendretBrev
            }
        }

        val newDocument = Dto.Document(
            pdf = pdfBytes,
            dokumentDato = pesysBrevdata.felles.dokumentDato,
            redigertBrevHash = brev.redigertBrevHash,
            brevdataHash = nyBrevdataHash,
            vedleggHash = nyVedleggHash
        )
        brev.document = newDocument
        return success(Dto.HentDocumentResult(document = newDocument, rendretBrevErEndret = rendretBrevErEndret))
    }

    private suspend fun hentPesysBrevdata(brev: BrevredigeringEntity): BrevdataResponse.Data =
        brevdataService.hentBrevdata(brev).let { brevdata ->
            if (brev.brevkode.kode() == P1_BREVKODE) {
                hentP1DataHandler(HentP1DataHandler.Request(brevId = brev.id.value, saksId = brev.saksId))
                    ?.asSuccess()
                    ?.let { p1 -> brevdata.copy(brevdata = brevdata.medP1Data(p1.value)) }
                    ?: throw IllegalStateException("Fant ikke P1-data for brev ${brev.id.value}")
            } else {
                brevdata
            }
        }

    // Grunnen til at vi kun sjekker blocks er at det er kun om det er endringer i selve innholdet at saksbehandler trenger å ta stilling til det.
    private suspend fun rendretBrevErEndret(brev: BrevredigeringEntity, pesysBrevdata: BrevdataResponse.Data): Boolean =
        brevmalService.renderMarkup(brev, pesysBrevdata).let { rendretBrev ->
            brev.redigertBrev.updateEditedLetter(rendretBrev.markup).blocks != brev.redigertBrev.blocks
        } || rendredeVedleggErEndret(brev, pesysBrevdata)

    private suspend fun rendredeVedleggErEndret(brev: BrevredigeringEntity, pesysBrevdata: BrevdataResponse.Data): Boolean =
        brevmalService.renderRedigerteVedlegg(brev, pesysBrevdata).any { (vedleggId, rendretVedlegg) ->
            val lagret = brev.hentRedigertVedlegg(vedleggId)
            lagret != null && lagret.updateEditedAttachment(rendretVedlegg).blocks != lagret.blocks
        }

    private fun leggVedPDFVedlegg(
        pdfVedlegg: List<PDFVedleggSkribenten>,
        pesysBrevdata: BrevdataResponse.Data,
        response: LetterResponse,
        brev: BrevredigeringEntity,
    ): ByteArray = pdfVedleggAppender.leggPaaVedlegg(
        response.file,
        pdfVedlegg
            .mapNotNull { it.vedlegg(pesysBrevdata) }
            .map {
                {
                    SideAppender.lesInnPDF(
                        it.sider,
                        brev.spraak
                    ) { spraak, side -> "/vedlegg/${side.filnavn}-${spraak.name}" }
                }
            }
    )

    private suspend fun genererFoersteside(
        brev: BrevredigeringEntity,
        fagsak: Fagsak,
        rendretBrev: ByteArray,
    ): ByteArray? = genererFoerstesideHandler(
        request = GenererFoerstesideHandler.Request(
            brevId = brev.id.value,
            saksId = brev.saksId,
            pid = fagsak.pid,
            sakstype = fagsak.sakType,
            tema = fagsak.tema,
            vedlegg = brev.valgteVedlegg.map { GenererFoerstesideHandler.Tittel(it.visningstekst) }
        )
    )?.asSuccess()?.value?.let { foersteside ->
        PDFMerger.merge(rendretBrev, foersteside.foersteside)
    }

    private fun BrevdataResponse.Data.medP1Data(p1: P1RedigerbarDto): Api.GeneriskBrevdata = brevdata.apply { put(P1_VEDLEGG_KEY, p1) }
}
