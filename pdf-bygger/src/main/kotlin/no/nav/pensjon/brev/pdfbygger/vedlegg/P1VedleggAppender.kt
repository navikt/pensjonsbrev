package no.nav.pensjon.brev.pdfbygger.vedlegg

import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object P1VedleggAppender {

    internal fun leggPaaP1(data: Map<String, Any>): PDDocument {
        val unwrapped = data["data"] as Map<*, *>

        val innvilgedePensjoner = unwrapped["innvilgedePensjoner"] as List<*>
        val side2 = (0..innvilgedePensjoner.size step 5).map {
            PDDocument.load(javaClass.getResourceAsStream("/P1-side2.pdf"))
        }

        val avslaattePensjoner = unwrapped["avslaattePensjoner"] as List<*>
        val side3 = (0..avslaattePensjoner.size step 5).map {
            PDDocument.load(javaClass.getResourceAsStream("/P1-side3.pdf"))
        }

        val target = PDDocument()
        val merger = PDFMergerUtility()
        merger.appendDocument(target, settOppSide1(unwrapped))
        side2.forEach { merger.appendDocument(target, it) }
        side3.forEach { merger.appendDocument(target, it) }
        merger.appendDocument(target, PDDocument.load(javaClass.getResourceAsStream("/P1-side4.pdf")))

        return target
    }

    private fun settOppSide1(unwrapped: Map<*, *>): PDDocument {
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

    internal fun leggPaaP1Vedlegg() = PDDocument.load(javaClass.getResourceAsStream("/P1-vedlegg.pdf"))
}