package no.nav.pensjon.brev.pdfbygger.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg.Side
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object P1VedleggAppender {

    internal fun lesInnP1(vedlegg: PDFVedlegg, spraak: LanguageCode): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val unwrapped = vedlegg.sider

        merger.leggTilSide(target, settOppSide1(unwrapped, spraak))
        settOppSide2(unwrapped, merger, target, spraak)
        settOppSide3(unwrapped, merger, target, spraak)
        settOppSide4(unwrapped, merger, target, spraak)

        return target
    }

    private fun settOppSide1(unwrapped: Map<Side, Map<String, String?>>, spraak: LanguageCode): PDDocument =
        lesInnPDF("P1-side1.pdf", spraak).also {
            it.setValues(unwrapped[Side(1)]!!.plus("page" to "1/${unwrapped.keys.size}"))
        }

    private fun settOppSide2(
        unwrapped: Map<Side, Map<String, String?>>,
        merger: PDFMergerUtility,
        target: PDDocument,
        spraak: LanguageCode,
    ) {
        val antallSide2 = unwrapped.keys.count { it.originalSide == 3 }
        (0..<antallSide2).map { index -> unwrapped[Side(index)]!!
            .let { flettefelt ->
                lesInnPDF("P1-side2.pdf", spraak).also {
                    it.setValues(
                        flettefelt.plus("page" to "${1 + index + 1}/${unwrapped.keys.size}")
                    )
                }
            }
        }.forEach { merger.leggTilSide(target, it) }
    }

    private fun settOppSide3(
        unwrapped: Map<Side, Map<String, String?>>,
        merger: PDFMergerUtility,
        target: PDDocument,
        spraak: LanguageCode,
    ) {
        val startSide3 = 1 + unwrapped.keys.count { it.originalSide == 2 }
        val antallSide3 = unwrapped.keys.count { it.originalSide == 3 }
        (0..<antallSide3).map { index -> unwrapped[Side(index)]!!
            .let { flettefelt ->
                lesInnPDF("P1-side3.pdf", spraak).also {
                    it.setValues(flettefelt.plus("page" to "${startSide3 + index + 1}/${unwrapped.keys.size}"))
                }
            }
        }.forEach { merger.leggTilSide(target, it) }
    }

    private fun settOppSide4(
        unwrapped: Map<Side, Map<String, String?>>,
        merger: PDFMergerUtility,
        target: PDDocument,
        spraak: LanguageCode,
    ) =
        lesInnPDF("P1-side4.pdf", spraak)
            .also {
                it.setValues(
                    unwrapped[Side(unwrapped.size-1)]!! +
                            ("page" to "${unwrapped.size}/${unwrapped.size}")
                    )
            }.also { merger.leggTilSide(target, it) }

    internal fun lesInnP1Vedlegg(spraak: LanguageCode) = lesInnPDF("P1-vedlegg.pdf", spraak)

    private fun lesInnPDF(filnavn: String, spraak: LanguageCode): PDDocument =
        PDDocument.load(javaClass.getResourceAsStream("/vedlegg/P1/${spraak.name}/$filnavn"))

}