package no.nav.pensjon.brev.pdfvedlegg

import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PDFVedleggAppender
import no.nav.pensjon.brev.maler.vedlegg.pdf.tilPDFVedlegg
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.ByteArrayOutputStream
import kotlin.collections.map

internal object PDFVedleggAppenderImpl : PDFVedleggAppender {

    override fun leggPaaVedlegg(
        pdfCompilationOutput: PDFCompilationOutput,
        attachments: List<PDFVedleggData>,
        spraak: Language,
    ): PDFCompilationOutput {
        /* Ikke strengt nødvendig å returnere her, det vil fungere uten, men optimalisering.
        De aller, aller fleste brevene har ikke PDF-vedlegg, så de trenger ikke gå gjennom denne løypa
         */
        if (attachments.isEmpty()) {
            return pdfCompilationOutput
        }

        val merger = PDFMergerUtility()
        val target = PDDocument()
        val originaltDokument = pdfCompilationOutput.bytes.let { Loader.loadPDF(it) }
        merger.leggTilSide(target, originaltDokument)
        leggPaaBlankPartallsside(originaltDokument, merger, target)

        attachments.map { VedleggAppender.lesInnVedlegg(it.tilPDFVedlegg(), spraak) }.forEach {
            leggPaaBlankPartallsside(it, merger, target)
            merger.leggTilSide(target, it)
        }
        return tilByteArray(target).also { target.close() }
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

    private fun tilByteArray(target: PDDocument): PDFCompilationOutput {
        val outputStream = ByteArrayOutputStream()
        target.save(outputStream)
        return PDFCompilationOutput(outputStream.toByteArray())
    }
}

internal fun PDDocument.setValues(values: Map<String, String?>) = values.forEach { entry ->
    documentCatalog?.acroForm?.fields?.firstOrNull { it.fullyQualifiedName == entry.key }?.setValue(entry.value)
}

internal fun PDFMergerUtility.leggTilSide(destionation: PDDocument, source: PDDocument) =
    appendDocument(destionation, source).also { source.close() }