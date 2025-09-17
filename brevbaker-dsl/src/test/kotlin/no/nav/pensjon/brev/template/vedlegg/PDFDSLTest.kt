package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PDFDSLTest {

    private object EmptyData : PDFVedleggData {
        override val tittel = mapOf(LanguageCode.BOKMAL to "Hei")

    }

    @Test
    fun `tolk dsl`() {
        val vedlegg = createAttachmentPDF<LangBokmal, EmptyData> {
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
        }.template(EmptyData)
        assertEquals(3, vedlegg.sider.size)
        val felt = vedlegg.sider[0].felt.map { it.felt }.reduce { a, b -> a.plus(b) }
        assertEquals("1", felt["felt1"]?.get(LanguageCode.BOKMAL))
    }
}