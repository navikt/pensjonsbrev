package no.nav.pensjon.brev.pdfbygger.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg.Side
import no.nav.pensjon.brevbaker.api.model.VedleggType
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object P1VedleggAppender {

    internal fun lesInnP1(vedlegg: PDFVedlegg, spraak: LanguageCode): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val unwrapped = vedlegg.sider

        unwrapped.entries.forEach {
            merger.leggTilSide(target, settOppSide(vedlegg.type, it, unwrapped.size, spraak))
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

    internal fun lesInnP1Vedlegg(type: VedleggType, spraak: LanguageCode) = lesInnPDF("${type.name}-vedlegg.pdf", spraak)

    private fun lesInnPDF(vedleggType: VedleggType, filnavn: String, spraak: LanguageCode): PDDocument =
        PDDocument.load(javaClass.getResourceAsStream("/vedlegg/${vedleggType.name}/${spraak.name}/$filnavn"))

}