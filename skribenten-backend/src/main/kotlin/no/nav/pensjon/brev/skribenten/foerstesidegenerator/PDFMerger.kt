package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.ByteArrayOutputStream

object PDFMerger {
    fun merge(first: ByteArray, second: ByteArray): ByteArray = merge(first, listOf { Loader.loadPDF(second) })

    fun merge(first: ByteArray, seconds: List<() -> PDDocument>): ByteArray {
        if (seconds.isEmpty()) {
            return first
        }
        PDDocument().use { target ->
            val merger = PDFMergerUtility()

            Loader.loadPDF(first).use { addPage(it, merger, target) }
            seconds.forEach { it().use { page -> addPage(page, merger, target) } }

            val outputStream = ByteArrayOutputStream()
            target.save(outputStream)
            return outputStream.toByteArray()
        }
    }

    private fun addPage(document: PDDocument, merger: PDFMergerUtility, target: PDDocument) {
        if (document.numberOfPages % 2 == 1) {
            document.addPage(PDPage())
        }
        merger.appendDocument(target, document)
    }
}