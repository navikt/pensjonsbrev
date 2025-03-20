package no.nav.pensjon.brev.pdfbygger.vedlegg

import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.vedlegg.P1VedleggAppender.lesInnP1
import no.nav.pensjon.brev.pdfbygger.vedlegg.P1VedleggAppender.lesInnP1Vedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggType
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.ByteArrayOutputStream

internal object PDFVedleggAppender {

    internal fun leggPaaVedlegg(
        pdfCompilationResponse: PDFCompilationResponse.Bytes,
        attachments: List<PDFVedlegg>,
    ): PDFCompilationResponse.Bytes {
        /* Ikke strengt nødvendig å returnere her, det vil fungere uten, men optimalisering.
        De aller, aller fleste brevene har ikke PDF-vedlegg, så de trenger ikke gå gjennom denne løypa
         */
        if (attachments.isEmpty()) {
            return pdfCompilationResponse
        }

        val merger = PDFMergerUtility()
        val target = PDDocument()
        val originaltDokument = pdfCompilationResponse.bytes.let { PDDocument.load(it) }
        merger.leggTilSide(target, originaltDokument)
        leggPaaBlankPartallsside(originaltDokument, merger, target)
        attachments.map { lesInnVedlegg(it) }.forEach {
            leggPaaBlankPartallsside(it, merger, target)
            merger.leggTilSide(target, it)
        }
        return tilByteArray(target).also { target.close() }
    }

    private fun lesInnVedlegg(attachment: PDFVedlegg): PDDocument =
        when (attachment.type) {
            PDFVedleggType.P1 -> lesInnP1(attachment.data as SamletMeldingOmPensjonsvedtakDto)
            PDFVedleggType.InformasjonOmP1 -> lesInnP1Vedlegg()
        }

    private fun leggPaaBlankPartallsside(
        originaltDokument: PDDocument,
        merger: PDFMergerUtility,
        target: PDDocument,
    ) {
        if (originaltDokument.pages.count % 2 == 1) {
            PDDocument().also { it.addPage(PDPage()) }.also { merger.leggTilSide(target, it) }.also { it.close() }
        }
    }

    private fun tilByteArray(target: PDDocument): PDFCompilationResponse.Bytes {
        val outputStream = ByteArrayOutputStream()
        target.save(outputStream)
        return PDFCompilationResponse.Bytes(outputStream.toByteArray())
    }
}

internal fun PDDocument.setValues(values: Map<String, String?>) = values.forEach { entry ->
    documentCatalog?.acroForm?.fields?.firstOrNull { it.fullyQualifiedName == entry.key }?.setValue(entry.value)
}

internal fun PDFMergerUtility.leggTilSide(destionation: PDDocument, source: PDDocument) =
    appendDocument(destionation, source).also { source.close() }