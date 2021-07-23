package no.nav.pensjon.brev.latex

import no.nav.pensjon.brev.dto.PdfCompilationInput
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

@Disabled //pdf compilation integration test
class LatexLetterBuilderTest {
    private val laTeXCompilerService = LaTeXCompilerService()

    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()

    @Test
    fun `master template test`() {
        val masterTemplateName = "pensjonsbrev_v2.cls"
        val pdfCompilationOutput = laTeXCompilerService.producePDF(
            PdfCompilationInput(
                mapOf(
                    masterTemplateName to getResource(masterTemplateName),
                    "letter.tex" to getResource("test.tex"),
                    "nav-logo.pdf" to getResource("nav-logo.pdf"),
                    "params.tex" to getResource("params.tex"),
                    "nav-logo.pdf_tex" to getResource("nav-logo.pdf_tex"),
                )
            )
        )
        // Write to file for visual debugging
        val file = File("test.pdf")
        file.writeBytes(decoder.decode(pdfCompilationOutput))
    }

    private fun getResource(fileName: String): String {
        val classPath = """/$fileName"""
        val file = LatexLetterBuilderTest::class.java.getResourceAsStream(classPath)?.readAllBytes()
            ?: throw IllegalStateException("""Could not find class resource $classPath""")
        return encoder.encodeToString(file)
    }

}