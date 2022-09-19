package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*

@Tag(TestTags.PDF_BYGGER)
class UfoerOmregningEnsligITest {

    @Test
    fun test() {
        Letter(
            UfoerOmregningEnslig.template,
            Fixtures.create<UfoerOmregningEnsligDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UT_DOD_ENSLIG_AUTO_BOKMAL", it) }
    }

    @Test
    fun testHtml() {
        Letter(
            UfoerOmregningEnslig.template,
            Fixtures.create<UfoerOmregningEnsligDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto,
        )
            .let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("UT_DOD_ENSLIG_AUTO_BOKMAL", it) }
    }
}
