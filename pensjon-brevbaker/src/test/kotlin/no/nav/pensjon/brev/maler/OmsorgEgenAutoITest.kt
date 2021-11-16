package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
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
    val pdfBuilderURL = System.getenv("PDF_BYGGER_URL")?: "http://localhost:8081"
    @Test
    fun test() {
        Letter(
            OmsorgEgenAuto.template,
            OmsorgEgenAutoDto(),
            Language.English,
            Fixtures.fellesAuto
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService(pdfBuilderURL).producePDF(it).base64PDF }
            .also {
                val file = File("build/test_pdf/000104-english.pdf")
                file.parentFile.mkdirs()
                file.writeBytes(Base64.getDecoder().decode(it))
                println("Test-file written to: ${file.path}")
            }
    }

}