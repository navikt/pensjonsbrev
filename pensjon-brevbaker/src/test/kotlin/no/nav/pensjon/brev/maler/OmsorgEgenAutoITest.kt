package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.latex.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.PDF_BYGGER)
class OmsorgEgenAutoITest {

    @Test
    fun test() {
        Letter(
            OmsorgEgenAuto.template,
            OmsorgEgenAutoDto(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("OMSORG_EGEN_AUTO_BOKMAL", it) }
    }

}