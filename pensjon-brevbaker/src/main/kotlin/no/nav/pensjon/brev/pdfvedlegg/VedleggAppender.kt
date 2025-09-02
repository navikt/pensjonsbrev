package no.nav.pensjon.brev.pdfvedlegg

import no.nav.brev.brevbaker.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.Side
import no.nav.pensjon.brevbaker.api.model.VedleggType
import org.apache.pdfbox.Loader
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object VedleggAppender {

    internal fun lesInnVedlegg(vedlegg: PDFVedlegg, spraak: LanguageCode): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val sider = vedlegg.sider

        sider.forEach {
            merger.leggTilSide(target, settOppSide(vedlegg.type, it, sider.size, spraak))
        }

        return target
    }

    private fun settOppSide(
        type: VedleggType,
        side: Side,
        antallSider: Int,
        spraak: LanguageCode,
    ): PDDocument =
        lesInnPDF(type, "${type.name}-side${side.originalSide}.pdf", spraak).also { pdf ->
            pdf.setValues(side.felt + ("page" to "${side.sidenummer}/$antallSider"))
        }

    private fun lesInnPDF(vedleggType: VedleggType, filnavn: String, spraak: LanguageCode): PDDocument =
        javaClass.getResource("/vedlegg/${vedleggType.name}/${spraak.name}/$filnavn")?.let {
            Loader.loadPDF(it.readBytes())
        } ?: throw IllegalArgumentException("Fant ikke vedlegg $filnavn for type ${vedleggType.name}")
}