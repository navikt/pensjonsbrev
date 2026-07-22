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

        return mergePDFs(pdfCompilationOutput, attachments, spraak)
    }

    private fun mergePDFs(
        first: ByteArray,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): ByteArray = PDDocument().use { target ->
        val merger = PDFMergerUtility()

        Loader.loadPDF(first).use {
            merger.appendDocument(target, it)
        }

        attachments.forEach {
            VedleggAppender.lesInnVedlegg(it, spraak).use { vedlegg ->
                if (vedlegg.pages.count % 2 == 1) {
                    target.addPage(PDPage())
                }
                merger.appendDocument(target, vedlegg)
            }
        }

        val outputStream = ByteArrayOutputStream()
        target.save(outputStream)
        return outputStream.toByteArray()
    }
}
