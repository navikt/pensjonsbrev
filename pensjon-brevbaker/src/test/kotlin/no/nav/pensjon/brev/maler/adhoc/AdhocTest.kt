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
    fun `testGjenlevendeFoer1970 pdf`() {
        testAdhocPdf(AdhocRegelendretGjenlevendetillegg.template, "ADHOC_AdhocRegelendretGjenlevendetillegg", Bokmal)
        testAdhocPdf(AdhocRegelendretGjenlevendetillegg.template, "ADHOC_AdhocRegelendretGjenlevendetillegg", Nynorsk)
        testAdhocPdf(AdhocRegelendretGjenlevendetillegg.template, "ADHOC_AdhocRegelendretGjenlevendetillegg", English)
    }
    @Test
    fun `testGjenlevendeFoer1970 html`() {
        testHtml(AdhocRegelendretGjenlevendetillegg.template, "ADHOC_AdhocRegelendretGjenlevendetillegg", Bokmal)
    }

}