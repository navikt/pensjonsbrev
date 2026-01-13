package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brevbaker.api.model.EmptyPDFVedleggData
import no.nav.pensjon.brev.template.FellesFactory
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PDFDSLTest {

    @Test
    fun `tolk dsl`() {
        val vedlegg = createAttachmentPDF<LangBokmal, EmptyPDFVedleggData>(title = { text(bokmal { +"Hei" }) }) {
            data, felles ->
            side("Side1.pdf") {
                felt {
                    "felt1" to 1
                    "felt2" to "a"
                    "felt3" to "b"
                }
            }
            side("Side2.pdf") {
                felt {

                }
            }
            side("Side3.pdf") {
                felt {
                    "felt1" to 3
                }
            }
        }.template(EmptyPDFVedleggData, FellesFactory.felles)
        assertEquals(3, vedlegg.sider.size)
        val felt = vedlegg.sider[0].felt.map { it.felt }.reduce { a, b -> a.plus(b) }
        assertEquals("1", felt["felt1"]?.get(LanguageCode.BOKMAL))
    }
}