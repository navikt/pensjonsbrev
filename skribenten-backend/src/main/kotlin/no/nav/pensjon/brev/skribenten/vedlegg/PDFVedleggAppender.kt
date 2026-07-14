package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.ByteArrayOutputStream

interface PDFVedleggAppender {
    fun leggPaaVedlegg(
        pdfCompilationOutput: ByteArray,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): ByteArray
}

class PDFVedleggAppenderImpl : PDFVedleggAppender {

    override fun leggPaaVedlegg(
        pdfCompilationOutput: ByteArray,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): ByteArray {
        /* Ikke strengt nødvendig å returnere her, det vil fungere uten, men optimalisering.
        De aller, aller fleste brevene har ikke PDF-vedlegg, så de trenger ikke gå gjennom denne løypa
         */
        if (attachments.isEmpty()) {
            return pdfCompilationOutput
        }

        PDDocument().use { target ->
            val merger = PDFMergerUtility()

            Loader.loadPDF(pdfCompilationOutput).use {
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

    private fun tilByteArray(target: PDDocument): ByteArray {
        val outputStream = ByteArrayOutputStream()
        target.save(outputStream)
        return outputStream.toByteArray()
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

internal fun PDFMergerUtility.leggTilSide(destination: PDDocument, source: PDDocument) =
    appendDocument(destination, source)

