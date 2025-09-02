package no.nav.pensjon.brev.pdfvedlegg

import no.nav.brev.brevbaker.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object VedleggAppender {

    internal fun lesInnVedlegg(vedlegg: PDFVedlegg, spraak: LanguageCode): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val sider = vedlegg.sider

        sider.forEach {
            val side = lesInnPDF(it.filnavn, spraak).also { side ->
                side.setValues(it.felt + ("page" to "${it.sidenummer}/${sider.size}"))
            }
            merger.leggTilSide(target, side)
        }

        return target
    }

    private fun lesInnPDF(filnavn: String, spraak: LanguageCode) =
        javaClass.getResource("/vedlegg/${filnavn}-${spraak.name}.pdf")
            ?.let { Loader.loadPDF(it.readBytes()) }
            ?: throw IllegalArgumentException("Fant ikke vedlegg $filnavn")
}