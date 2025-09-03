package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.LangNynorsk
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PDFDSLTest {

    @Test
    fun `tolk dsl`() {
        val vedlegg = PDFVedlegg.create<LangNynorsk>(mapOf(Language.Bokmal to "Vedlegg1", Language.English to "Attachment1")) {
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
        }
        assertEquals(3, vedlegg.sider.size)
        val felt = vedlegg.sider[0].felt.map { it.felt }.reduce { a, b -> a.plus(b) }
        assertEquals("1", felt["felt1"]?.get(Language.Bokmal))
    }
}