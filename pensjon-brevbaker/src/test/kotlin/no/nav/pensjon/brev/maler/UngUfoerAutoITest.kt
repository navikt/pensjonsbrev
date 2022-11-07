package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class UngUfoerAutoITest {

    @Test
    fun pdftest() {
        Letter(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UNG_UFOER_AUTO_BOKMAL", it) }
    }

    @Test
    fun testHtml() {
        Letter(
            UngUfoerAuto.template,
            Fixtures.create<UngUfoerAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML("UNG_UFOER_AUTO_BOKMAL", it) }
    }

}