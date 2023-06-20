package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*
@Tag(TestTags.PDF_BYGGER)
class ForhaandsvarselEtteroppgjoer {
    @Test
    fun testPdf() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerDto>(),
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
            UngUfoerAuto.template,
            Fixtures.create<ForhaandsvarselEtteroppgjoerDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("UT_EO_FORHAANDSVARSEL_AUTO", it) }
    }
}