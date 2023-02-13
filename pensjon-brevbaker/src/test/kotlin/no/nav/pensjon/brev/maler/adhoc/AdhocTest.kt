package no.nav.pensjon.brev.maler.adhoc

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class AdhocTest {
    fun testAdhocPdf(template: LetterTemplate<*, *>, pdfName: String) {
        Letter(
            template,
            Any(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(pdfName, it) }

    }

    @Test
    fun testGjenlevendeFoer1970() {
        testAdhocPdf(GjenlevendeInfoFoer1970.template, "ADHOC_GJENLEVENDEINFOFOER1970")
    }

    @Test
    fun testGjenlevendeEtter1970() {
        testAdhocPdf(GjenlevendeInfoEtter1970.template, "ADHOC_GJENLEVENDEINFOETTER1970")
    }

}