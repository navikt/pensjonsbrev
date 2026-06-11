package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.ByteArrayOutputStream

object PDFMerger {
    fun merge(first: ByteArray, second: ByteArray): ByteArray {
        PDDocument().use { target ->
            val merger = PDFMergerUtility()
            val foerstesidePdf = Loader.loadPDF(second)
            val originalPdf = Loader.loadPDF(first)
            merger.appendDocument(target, originalPdf).also { originalPdf.close() }
            merger.appendDocument(target, foerstesidePdf).also { foerstesidePdf.close() }
            val outputStream = ByteArrayOutputStream()
            target.save(outputStream)
            return outputStream.toByteArray()
        }
    }
}