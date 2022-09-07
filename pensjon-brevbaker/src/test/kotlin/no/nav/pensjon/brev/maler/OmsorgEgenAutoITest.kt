package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.PDF_BYGGER)
class OmsorgEgenAutoITest {

    @Test
    fun testPdf() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<OmsorgEgenAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("OMSORG_EGEN_AUTO_BOKMAL", it) }
    }

    @Test
    fun testHtml() {
        Letter(
            OmsorgEgenAuto.template,
            Fixtures.create<OmsorgEgenAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("OMSORG_EGEN_AUTO_BOKMAL", it) }
    }
}