package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.EmptyPDFVedleggData
import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata.saksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PDFDSLTest {

    @Test
    fun `tolk dsl`() {
        val vedlegg = createAttachmentPDF<LangBokmal, EmptyPDFVedleggData, EmptySaksbehandlerValg>(listOf(newText(Language.Bokmal to "Hei"))) {
            data, felles, saksbehandlerValg ->
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
        }.template(EmptyPDFVedleggData, FellesFactory.felles, saksbehandlerValg)
        assertEquals(3, vedlegg.sider.size)
        val felt = vedlegg.sider[0].felt.map { it.felt }.reduce { a, b -> a.plus(b) }
        assertEquals("1", felt["felt1"]?.get(LanguageCode.BOKMAL))
    }
}