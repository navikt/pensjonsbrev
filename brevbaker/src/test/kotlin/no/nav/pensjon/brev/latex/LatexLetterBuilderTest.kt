package no.nav.pensjon.brev.latex

import no.nav.pensjon.brev.dto.Letter
import no.nav.pensjon.brev.dto.LetterTemplate
import no.nav.pensjon.brev.dto.PdfCompilationInput
import no.nav.pensjon.brev.dto.StandardFields
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import kotlin.test.assertNotNull

class LatexLetterBuilderTest {
    private val laTeXCompilerService = LaTeXCompilerService()
    private val latexLetterBuilder = LatexLetterBuilder(laTeXCompilerService)

    private val standardFields = StandardFields(
        returAdresse = "test1",
        postnummer = "test2",
        poststed = "test3",
        land = "test4",
        mottakerNavn = "test5",
        verge = "test6",
        adresseLinje1 = "test7",
        adresseLinje2 = "test8",
        adresseLinje3 = "test9",
        dokumentDato = "test10",
        saksnummer = "test11",
        sakspartNavn = "test12",
        sakspartId = "test13",
        kontakTelefonnummer = "test1",
    )

    @Tag("integration")
    @Test
    fun `basic integration with container`() {
        val result = latexLetterBuilder.buildLatex(
            Letter(
                standardFields,
                LetterTemplate("test")
            )
        )
        assertNotNull(result.pdf)
    }

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
//        val file = File("test.pdf")
//        file.writeBytes(decoder.decode(pdfCompilationOutput.pdf))
    }

    private fun getResource(fileName: String): String {
        val classPath = """/$fileName"""
        val file = LatexLetterBuilderTest::class.java.getResourceAsStream(classPath)?.readAllBytes()
            ?: throw IllegalStateException("""Could not find class resource $classPath""")
        return encoder.encodeToString(file)
    }

}