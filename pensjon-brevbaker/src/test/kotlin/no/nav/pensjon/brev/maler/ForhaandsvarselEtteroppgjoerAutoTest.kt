package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDto

import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*
@Tag(TestTags.PDF_BYGGER)
class ForhaandsvarselEtteroppgjoerAutoTest {
    @Test
    fun testPdf() {
        Letter(
            ForhaandsvarselEtteroppgjoerAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UT_EO_FORHAANDSVARSEL_AUTO", it) }
    }
    @Test
    fun testHtml() {
        Letter(
            ForhaandsvarselEtteroppgjoerAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("UT_EO_FORHAANDSVARSEL_AUTO", it) }
    }
}