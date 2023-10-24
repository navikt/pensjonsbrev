package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AdhocTest {
    fun testHtml(template: LetterTemplate<*, *>, htmlName: String, vararg language: Language) {
        language.forEach {
            Letter(template, Unit, it, Fixtures.fellesAuto).renderTestHtml(htmlName + "_${it}")
        }
    }

    fun testAdhocPdf(template: LetterTemplate<*, *>, pdfName: String, vararg language: Language) {
        language.forEach {
            Letter(template, Unit, it, Fixtures.fellesAuto).renderTestPDF(pdfName + "_${it}")
        }
    }


    @Test
    fun `testAdhocGJRfoed1954Til1957 pdf`() {
        testAdhocPdf(AdhocGjenlevenderettFoed1954Til1957.template, "ADHOC_GjenlevenderettFoed1954Til1957", Bokmal)
        testAdhocPdf(AdhocGjenlevenderettFoed1954Til1957.template, "ADHOC_GjenlevenderettFoed1954Til1957", Nynorsk)
        testAdhocPdf(AdhocGjenlevenderettFoed1954Til1957.template, "ADHOC_GjenlevenderettFoed1954Til1957", English)
    }
    @Test
    fun `testAdhocGJRfoed1954Til1957 html`() {
        testHtml(AdhocGjenlevenderettFoed1954Til1957.template, "ADHOC_FeilUtsendingAvGjenlevenderett", Bokmal)
    }

}