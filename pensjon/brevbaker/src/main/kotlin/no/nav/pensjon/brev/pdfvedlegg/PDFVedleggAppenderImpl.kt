package no.nav.pensjon.brev.pdfvedlegg

import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PDFVedleggAppender
import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.ByteArrayOutputStream

internal object PDFVedleggAppenderImpl : PDFVedleggAppender {

    override fun leggPaaVedlegg(
        pdfCompilationOutput: PDFCompilationOutput,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
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
                merger.leggTilSide(target, it)
            }

            attachments.forEach {
                VedleggAppender.lesInnVedlegg(it, spraak).use { vedlegg ->
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

