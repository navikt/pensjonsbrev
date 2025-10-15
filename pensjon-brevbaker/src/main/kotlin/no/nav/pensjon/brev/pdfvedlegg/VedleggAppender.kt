package no.nav.pensjon.brev.pdfvedlegg

import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox
import org.apache.pdfbox.pdmodel.interactive.form.PDField
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField

internal object VedleggAppender {

    internal fun lesInnVedlegg(vedlegg: PDFVedlegg, spraak: LanguageCode): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val sider = vedlegg.sider

        sider.forEachIndexed { index, side ->
            lesInnPDF(side.filnavn, spraak).use { pdfSide ->
                side.felt{ "Sidetall" to "${index + 1}/${sider.size}" }
                val map: Map<String, String?> = side.felt
                    .flatMap { it.felt.entries }
                    .associate { it.key to it.value?.get(spraak) }

                fillFields(pdfSide, map)
                merger.leggTilSide(target, pdfSide)
            }
        }

        return target
    }

    private fun lesInnPDF(filnavn: String, spraak: LanguageCode) =
        javaClass.getResource("/vedlegg/${filnavn}-${spraak.name}.pdf")
            ?.let { Loader.loadPDF(it.readBytes()) }
            ?: throw IllegalArgumentException("Fant ikke vedlegg $filnavn")


    private fun fillFields(document: PDDocument, feltVerdier: Map<String, String?>) {
        document.documentCatalog?.acroForm?.fieldIterator?.forEach { field ->
            fillFields(field, feltVerdier)
        }
    }

    private fun fillFields(field: PDField, feltVerdier: Map<String, String?>) {
        if(feltVerdier.containsKey(field.partialName)){
            val feltVerdi = feltVerdier[field.partialName]
            when(field) {
                is PDTextField -> {
                    field.value = feltVerdi
                }

                is PDComboBox -> {
                    field.setValue(feltVerdi)
                }
            }

        }
    }
}

