package no.nav.pensjon.brev.pdfvedlegg

import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
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
    internal fun lesInnVedlegg(vedlegg: PDFVedlegg, spraak: LanguageCode): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val sider = vedlegg.sider
        val inneholderFelter = sider.any { it.felt.isNotEmpty() }

        sider.forEachIndexed { index, side ->
            lesInnPDF(side.filnavn, spraak).use { pdfSide ->
                if (inneholderFelter) {
                    addPageFieldPrefix(pdfSide, index)
                }
                merger.leggTilSide(target, pdfSide)
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

            target.documentCatalog.acroForm.needAppearances = false

            fillFields(target, feltVerdier)
        }
        return target
    }

    private fun lesInnPDF(filnavn: String, spraak: LanguageCode) =
        javaClass.getResource("/vedlegg/${filnavn}-${spraak.name}.pdf")
            ?.let { Loader.loadPDF(it.readBytes()) }
            ?: throw IllegalArgumentException("Fant ikke vedlegg $filnavn")

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

