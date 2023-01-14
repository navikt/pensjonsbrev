package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.PDF_BYGGER)
class EndringOpptjeningAutoTest {

    @Test
    fun testPdf() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<EndringOpptjeningAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UT_ENDR_OPPTJ_AUTO_BOKMAL", it) }
    }

    @Test
    fun testHtml() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<EndringOpptjeningAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("UT_ENDR_OPPTJ_AUTO_BOKMAL", it) }
    }
}