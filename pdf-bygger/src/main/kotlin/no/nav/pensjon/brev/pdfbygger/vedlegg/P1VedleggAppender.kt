package no.nav.pensjon.brev.pdfbygger.vedlegg

import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.pdfbygger.setValues
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object P1VedleggAppender {

    internal fun leggPaaP1(pdfCompilationResponse: PDFCompilationResponse.Bytes, data: Map<String, Any>): PDDocument {
        val unwrapped = data["data"] as Map<*, *>
        val pdf1 = settOppSide1(unwrapped)
        val innvilgedePensjoner = unwrapped["innvilgedePensjoner"] as List<*>
        val side2 = (0..innvilgedePensjoner.size % 5).map {
            PDDocument.load(javaClass.getResourceAsStream("/P1-side2.pdf"))
        }

        val avslaattePensjoner = unwrapped["avslaattePensjoner"] as List<*>
        val side3 = (0..avslaattePensjoner.size % 5).map {
            PDDocument.load(javaClass.getResourceAsStream("/P1-side3.pdf"))
        }
        val pdf4 = PDDocument.load(javaClass.getResourceAsStream("/P1-side4.pdf"))

        val target = PDDocument()
        val merger = PDFMergerUtility()
        merger.appendDocument(target, pdfCompilationResponse.bytes.let { PDDocument.load(it) })
        // TODO her skal det vera ei blank side. Sjekk etter generell løysing
        merger.appendDocument(target, pdf1)
        side2.forEach { merger.appendDocument(target, it) }
        side3.forEach { merger.appendDocument(target, it) }
        merger.appendDocument(target, pdf4)

        return target

    }

    private fun settOppSide1(unwrapped: Map<*, *>): PDDocument? {
        val pdf1 = PDDocument.load(javaClass.getResourceAsStream("/P1-side1.pdf"))
        val holderData = unwrapped["holder"] as Map<*, *>
        val holder = mapOf(
            "fornavn" to holderData["fornavn"].toString(),
            "etternavn" to holderData["etternavn"].toString(),
            "etternavn_foedsel" to holderData["etternavnVedFoedsel"]?.toString(),
            "gateadresse" to holderData["adresselinje"].toString(),
            "landkode" to holderData["landkode"].toString(),
            "postkode" to holderData["postnummer"].toString(),
            "by" to holderData["poststed"].toString()
        )
        pdf1.documentCatalog.acroForm.setValues(
            holder
        )
        return pdf1
    }
}