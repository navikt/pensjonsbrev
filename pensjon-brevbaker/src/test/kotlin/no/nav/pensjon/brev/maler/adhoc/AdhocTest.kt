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
        testAdhocPdf(GjenlevendeInfoFoer1971.template, "ADHOC_GJENLEVENDEINFOFOER1971", Bokmal)
        testAdhocPdf(GjenlevendeInfoFoer1971.template, "ADHOC_GJENLEVENDEINFOFOER1971", Nynorsk)
        testAdhocPdf(GjenlevendeInfoFoer1971.template, "ADHOC_GJENLEVENDEINFOFOER1971", English)
    }
    @Test
    fun `testGjenlevendeFoer1970 html`() {
        testHtml(GjenlevendeInfoFoer1971.template, "ADHOC_GJENLEVENDEINFOFOER1971", Bokmal)
    }

    @Test
    fun `testGjenlevendeEtter1970 pdf`() {
        testAdhocPdf(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970", Bokmal)
        testAdhocPdf(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970", Nynorsk)
        testAdhocPdf(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970", English)
    }
    @Test
    fun `testGjenlevendeEtter1970 html`() {
        testHtml(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970", Bokmal)
        testHtml(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970", Nynorsk)
        testHtml(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970", English)
    }

    @Test
    fun `testRegelendring pdf`() {
        testAdhocPdf(AdhocRegelendretGjenlevendetillegg.template, "ADHOC_AdhocRegelendretGjenlevendetillegg", Bokmal)
        testAdhocPdf(AdhocRegelendretGjenlevendetillegg.template, "ADHOC_AdhocRegelendretGjenlevendetillegg", Nynorsk)
      //  testAdhocPdf(AdhocRegelendretGjenlevendetillegg.template, "ADHOC_AdhocRegelendretGjenlevendetillegg", English)
    }
    @Test
    fun `testRegelendring html`() {
        testHtml(AdhocRegelendretGjenlevendetillegg.template, "ADHOC_AdhocRegelendretGjenlevendetillegg", Bokmal)
    }

}