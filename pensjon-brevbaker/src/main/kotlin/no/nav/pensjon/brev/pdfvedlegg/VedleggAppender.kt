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
            merger.leggTilSide(target, settOppSide( it, sider.size, spraak))
        }

        return target
    }

    private fun settOppSide(side: Side, antallSider: Int, spraak: LanguageCode): PDDocument =
        lesInnPDF(side.filnavn, spraak).also { it.setValues(side.felt + ("page" to "${side.sidenummer}/$antallSider")) }

    private fun lesInnPDF(filnavn: String, spraak: LanguageCode): PDDocument =
        javaClass.getResource("/vedlegg/${spraak.name}/${filnavn}-${spraak.name}.pdf")?.let {
            Loader.loadPDF(it.readBytes())
        } ?: throw IllegalArgumentException("Fant ikke vedlegg $filnavn")
}