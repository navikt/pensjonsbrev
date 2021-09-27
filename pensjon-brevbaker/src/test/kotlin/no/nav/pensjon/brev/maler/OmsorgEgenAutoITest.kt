package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

@Tag(TestTags.PDF_BYGGER)
class OmsorgEgenAutoITest {

    @Test
    fun test() {
        Letter(
            OmsorgEgenAuto.template,
            OmsorgEgenAutoDto(),
            Language.English,
            Fixtures.fellesAuto
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
//            .also { File("test-params.tex").writeBytes(Base64.getDecoder().decode(it.files["params.tex"])) }
            .let { LaTeXCompilerService().producePDF(it) }
//            .also { File("000104-english.pdf").writeBytes(Base64.getDecoder().decode(it)) }
    }

}