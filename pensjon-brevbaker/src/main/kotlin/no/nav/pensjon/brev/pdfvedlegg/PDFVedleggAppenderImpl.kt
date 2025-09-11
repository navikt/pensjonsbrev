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

        PDDocument().use { target ->
            val merger = PDFMergerUtility()

            Loader.loadPDF(pdfCompilationOutput.bytes).use {
                leggTilBlankPartallsideOgSaaLeggTilSide(it, target, merger)
            }

            attachments.forEach {
                VedleggAppender.lesInnVedlegg(it.tilPDFVedlegg(), spraak).use { vedlegg ->
                    leggTilBlankPartallsideOgSaaLeggTilSide(vedlegg, target, merger)
                }
            }
            return tilByteArray(target)
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

private fun leggTilBlankPartallsideOgSaaLeggTilSide(source: PDDocument, target: PDDocument, merger: PDFMergerUtility) {
    leggPaaBlankPartallsside(source, target)
    merger.leggTilSide(target, source)
}

private fun leggPaaBlankPartallsside(
    originaltDokument: PDDocument,
    target: PDDocument,
) {
    if (originaltDokument.pages.count % 2 == 1) {
        target.addPage(PDPage())
    }
}

internal fun PDFMergerUtility.leggTilSide(destionation: PDDocument, source: PDDocument) =
    appendDocument(destionation, source).also { source.close() }