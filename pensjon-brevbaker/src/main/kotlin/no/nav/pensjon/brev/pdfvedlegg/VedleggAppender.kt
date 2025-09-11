package no.nav.pensjon.brev.pdfvedlegg

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object VedleggAppender {

    internal fun <Lang : LanguageSupport> lesInnVedlegg(vedlegg: PDFVedlegg<Lang>, spraak: Language): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val sider = vedlegg.sider

        sider.forEachIndexed { index, side ->
            lesInnPDF(side.filnavn, spraak).use { pdfSide ->
                val map: Map<String, String?> = side.felt
                    .flatMap { it.felt.entries }
                    .associate { it.key to it.value?.get(spraak) }

                pdfSide.setValues(map + ("page" to "${index + 1}/${sider.size}"))
                merger.leggTilSide(target, pdfSide)
            }
        }

        return target
    }

    private fun lesInnPDF(filnavn: String, spraak: Language) =
        javaClass.getResource("/vedlegg/${filnavn}-${spraak.name}.pdf")
            ?.let { Loader.loadPDF(it.readBytes()) }
            ?: throw IllegalArgumentException("Fant ikke vedlegg $filnavn")
}