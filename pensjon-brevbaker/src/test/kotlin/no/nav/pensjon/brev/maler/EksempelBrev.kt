package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EksempelBrevDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class EksempelBrevITest {

    @Test
    fun test() {
        Letter(
            EksempelBrev.template,
            EksempelBrevDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("OMSORG_EGEN_AUTO_BOKMAL", it) }
    }

}