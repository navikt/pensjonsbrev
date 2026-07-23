package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.Side
import org.apache.pdfbox.Loader
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox
import org.apache.pdfbox.pdmodel.interactive.form.PDField
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField

internal object VedleggAppender {
    fun lesInnPDF(sider: List<Side>, spraak: LanguageCode, filnavn: (LanguageCode, Side) -> String): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val inneholderFelter = sider.any { it.felt.isNotEmpty() }

        sider.forEachIndexed { index, side ->
            lesInnPDF("${filnavn(spraak, side)}.pdf").use { pdfSide ->
                if (inneholderFelter) {
                    addPageFieldPrefix(pdfSide, index)
                }
                merger.appendDocument(target, pdfSide)
            }
        }

        if (inneholderFelter) {
            // For å støtte alle felt-verdier, så må hele fonten bygges inn, og ikke bare de tegnene som er i bruk fra før
            val font = PDType0Font.load(target, javaClass.getResource("/fonts/SourceSans3-Regular.ttf")!!.openStream(), false)
            val acroForm = target.documentCatalog.acroForm

            acroForm?.defaultResources = PDResources().apply { put(COSName.getPDFName("SourceSans3Embedded"), font) }
            val feltVerdier: Map<String, String?> = sider.flatMapIndexed { index, side ->
                val pagePrefix = pagePrefix(index)
                side.felt { "Sidetall" to "${index + 1}/${sider.size}" }
                side.felt
                    .flatMap { it.felt.entries }
                    .map { pagePrefix + it.key to it.value?.get(spraak) }
            }.associate { it.first to it.second }

            target.documentCatalog.acroForm?.needAppearances = false

            fillFields(target, feltVerdier)
        }
        return target
    }

    private fun lesInnPDF(filsti: String) =
        javaClass.getResource(filsti)
            ?.let { Loader.loadPDF(it.readBytes()) }
            ?: throw IllegalArgumentException("Fant ikke pdf $filsti")

    private fun addPageFieldPrefix(document: PDDocument, pageNumber: Int) {
        document.documentCatalog?.acroForm
            ?.fieldIterator
            ?.forEach {
                if (it is PDTextField || it is PDComboBox) {
                    it.partialName = pagePrefix(pageNumber) + it.partialName
                }
            }
    }

    private fun pagePrefix(pageNumber: Int): String = "page_${pageNumber}_"

    private fun fillFields(document: PDDocument, feltVerdier: Map<String, String?>) =
        document.documentCatalog?.acroForm?.fieldIterator
            ?.forEach { fillFields(it, feltVerdier) }

    private fun fillFields(field: PDField, feltVerdier: Map<String, String?>) {
        when (field) {
            is PDTextField -> {
                field.defaultAppearance = field.defaultAppearance.replaceFirst("SourceSans3-Regular", "SourceSans3Embedded")
            }

            is PDComboBox -> {
                field.defaultAppearance = field.defaultAppearance.replaceFirst("SourceSans3-Regular", "SourceSans3Embedded")
            }
        }

        if (feltVerdier.containsKey(field.partialName)) {
            val feltVerdi = feltVerdier[field.partialName]
            if (feltVerdi != null) {
                when (field) {
                    is PDTextField -> field.value = feltVerdi
                    is PDComboBox -> field.setValue(feltVerdi)
                }
            }

        }
    }
}

