package no.nav.pensjon.brev.pdfvedlegg

import no.nav.brev.brevbaker.PDFVedlegg
import no.nav.brev.brevbaker.Side
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
            merger.leggTilSide(target, settOppSide(vedlegg.filnavn, it, sider.size, spraak))
        }

        return target
    }

    private fun settOppSide(
        name: String,
        side: Side,
        antallSider: Int,
        spraak: LanguageCode,
    ): PDDocument =
        lesInnPDF(name, "$name-side${side.originalSide}.pdf", spraak).also { pdf ->
            pdf.setValues(side.felt + ("page" to "${side.sidenummer}/$antallSider"))
        }

    private fun lesInnPDF(name: String, filnavn: String, spraak: LanguageCode): PDDocument =
        javaClass.getResource("/vedlegg/$name/${spraak.name}/$filnavn")?.let {
            Loader.loadPDF(it.readBytes())
        } ?: throw IllegalArgumentException("Fant ikke vedlegg $filnavn for type $name")
}