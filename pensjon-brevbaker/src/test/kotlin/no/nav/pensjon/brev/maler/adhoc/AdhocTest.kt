package no.nav.pensjon.brev.maler.adhoc

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class AdhocTest {
    fun testHtml(template: LetterTemplate<*, *>, htmlName: String) {
        Letter(template, Unit, Language.Bokmal, Fixtures.fellesAuto.copy(signerendeSaksbehandlere = SignerendeSaksbehandlere("sakperson1", "sakperson2")))
            .let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML(htmlName, it) }
    }

    fun testAdhocPdf(template: LetterTemplate<*, *>, pdfName: String) {
        Letter(template, Any(), Language.Bokmal, Fixtures.fellesAuto.copy(signerendeSaksbehandlere = SignerendeSaksbehandlere("sakperson1", "sakperson2")))
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF(pdfName, it) }
    }

    @Test
    fun `testGjenlevendeFoer1970 pdf`() {
        testAdhocPdf(GjenlevendeInfoFoer1970.template, "ADHOC_GJENLEVENDEINFOFOER1970")
    }
    @Test
    fun `testGjenlevendeFoer1970 html`() {
        testHtml(GjenlevendeInfoFoer1970.template, "ADHOC_GJENLEVENDEINFOFOER1970")
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