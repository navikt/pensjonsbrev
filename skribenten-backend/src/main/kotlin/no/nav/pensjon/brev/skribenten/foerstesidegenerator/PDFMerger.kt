package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.ByteArrayOutputStream

object PDFMerger {
    fun merge(first: ByteArray, second: ByteArray): ByteArray = mergePDFs(first, { listOf(Loader.loadPDF(second)) })

    fun mergePDFs(
        first: ByteArray,
        seconds: () -> List<PDDocument>,
    ): ByteArray = PDDocument().use { target ->
        val merger = PDFMergerUtility()

        Loader.loadPDF(first).use {
            if (it.numberOfPages % 2 == 1) {
                it.addPage(PDPage())
            }
            merger.appendDocument(target, it)
        }

        seconds().forEach {
            it.use { vedlegg ->
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