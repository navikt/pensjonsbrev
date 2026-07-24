package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.ByteArrayOutputStream

object PDFMerger {
    fun merge(first: ByteArray, second: ByteArray): ByteArray {
        PDDocument().use { target ->
            val merger = PDFMergerUtility()
            Loader.loadPDF(first).use { merger.appendDocument(target, it) }
            Loader.loadPDF(second).use { merger.appendDocument(target, it) }
            val outputStream = ByteArrayOutputStream()
            target.save(outputStream)
            return outputStream.toByteArray()
        }
    }
}