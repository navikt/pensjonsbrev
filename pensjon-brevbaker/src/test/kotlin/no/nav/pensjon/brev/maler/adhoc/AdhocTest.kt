package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class AdhocTest {
    fun testHtml(template: LetterTemplate<*, *>, htmlName: String) {
        Letter(template, Unit, Language.Bokmal, Fixtures.fellesAuto).renderTestHtml(htmlName)
    }

    fun testAdhocPdf(template: LetterTemplate<*, *>, pdfName: String) {
        Letter(template, Unit, Language.Bokmal, Fixtures.fellesAuto).renderTestPDF(pdfName)
    }

    @Test
    fun `testGjenlevendeFoer1970 pdf`() {
        testAdhocPdf(GjenlevendeInfoFoer1971.template, "ADHOC_GJENLEVENDEINFOFOER1971")
    }
    @Test
    fun `testGjenlevendeFoer1970 html`() {
        testHtml(GjenlevendeInfoFoer1971.template, "ADHOC_GJENLEVENDEINFOFOER1971")
    }

    @Test
    fun `testGjenlevendeEtter1970 pdf`() {
        testAdhocPdf(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970")
    }

    @Test
    fun `testGjenlevendeEtter1970 html`() {
        testHtml(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970")
    }
}