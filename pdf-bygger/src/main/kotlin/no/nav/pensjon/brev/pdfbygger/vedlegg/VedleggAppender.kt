package no.nav.pensjon.brev.pdfbygger.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg.Side
import no.nav.pensjon.brevbaker.api.model.VedleggType
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object VedleggAppender {

    internal fun lesInnVedlegg(vedlegg: PDFVedlegg, spraak: LanguageCode): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val sider = vedlegg.sider

        sider.entries.forEach {
            merger.leggTilSide(target, settOppSide(vedlegg.type, it, sider.size, spraak))
        }

        return target
    }

    private fun settOppSide(
        type: VedleggType,
        side: Map.Entry<Side, Map<String, String?>>,
        antallSider: Int,
        spraak: LanguageCode,
    ): PDDocument =
        lesInnPDF(type, "${type.name}-side${side.key.originalSide}.pdf", spraak).also { pdf ->
            pdf.setValues(side.value + ("page" to "${side.key.sidenummer}/$antallSider"))
        }

    private fun lesInnPDF(vedleggType: VedleggType, filnavn: String, spraak: LanguageCode): PDDocument =
        PDDocument.load(javaClass.getResourceAsStream("/vedlegg/${vedleggType.name}/${spraak.name}/$filnavn"))

}