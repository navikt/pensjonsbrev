package no.nav.pensjon.brev.pdfbygger

import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggType
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm
import java.io.ByteArrayOutputStream

internal object PDFVedleggAppender {

    internal fun leggPaaVedlegg(pdfCompilationResponse: PDFCompilationResponse.Bytes, attachments: List<PDFVedlegg>) : PDFCompilationResponse.Bytes =
        attachments.map { leggPaaVedlegg(pdfCompilationResponse, it) }
            .firstOrNull()
            ?.let {
                val outputstream = ByteArrayOutputStream()
                it.save(outputstream)
                PDFCompilationResponse.Bytes(outputstream.toByteArray())
        } ?: pdfCompilationResponse

    private fun leggPaaVedlegg(pdfCompilationResponse: PDFCompilationResponse.Bytes, attachment: PDFVedlegg) : PDDocument =
        when (attachment.type) {
            PDFVedleggType.P1 -> leggPaaP1(pdfCompilationResponse, attachment.data)
        }

    private fun leggPaaP1(pdfCompilationResponse: PDFCompilationResponse.Bytes, data: Map<String, Any>): PDDocument {
        val somPdf = pdfCompilationResponse.bytes.let { PDDocument.load(it) }

        val pdf1 = PDDocument.load(javaClass.getResourceAsStream("/P1-side1.pdf"))

        val holderData = data["holder"] as Map<*, *>
        val holder = mapOf(
            "fornavn" to holderData["fornavn"].toString(),
            "etternavn" to holderData["etternavn"].toString(),
            "etternavn_foedsel" to holderData["etternavn_foedselsdato"]?.toString(),
            "gateadresse" to holderData["gateadresse"].toString(),
            "landkode" to holderData["landkode"].toString(),
            "postkode" to holderData["postkode"].toString(),
            "by" to holderData["by"].toString()
        )
        pdf1.documentCatalog.acroForm.setValues(
            holder
        )
        val innvilgedePensjoner = (0..25 step 5).map { it.toString() }
        val side2 = innvilgedePensjoner.map { i ->
            PDDocument.load(javaClass.getResourceAsStream("/P1-side2.pdf"))
        }

        val avslaattePensjoner = (0..15 step 5).map { it.toString() }
        val side3 = avslaattePensjoner.map { i ->
            PDDocument.load(javaClass.getResourceAsStream("/P1-side3.pdf"))
        }
        val pdf4 = PDDocument.load(javaClass.getResourceAsStream("/P1-side4.pdf"))

        val target = PDDocument()
        val merger = PDFMergerUtility()
        merger.appendDocument(target, somPdf)
        merger.appendDocument(target, pdf1)
        side2.forEach { merger.appendDocument(target, it) }
        side3.forEach { merger.appendDocument(target, it) }
        merger.appendDocument(target, pdf4)

        return target

    }

    private fun PDAcroForm.setValues(values: Map<String, String?>) = values.forEach { entry ->
        fields.firstOrNull() { it.fullyQualifiedName == entry.key }?.setValue(entry.value)
    }
}