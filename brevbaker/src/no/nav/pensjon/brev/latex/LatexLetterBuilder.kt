package no.nav.pensjon.brev.latex

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.dto.PdfCompilationInput
import no.nav.pensjon.brev.dto.Letter
import no.nav.pensjon.brev.dto.PDFCompilationOutput
import java.util.*

class LatexLetterBuilder(private val laTeXCompilerService: LaTeXCompilerService) {
    private val encoder = Base64.getEncoder()
    fun buildLatex(letter: Letter): PDFCompilationOutput {
        val template = letter.letterTemplate.template
        return runBlocking {
            laTeXCompilerService.producePDF(
                PdfCompilationInput(
                    mapOf(
                        "letter.tex" to encoder.encodeToString(
                            """
                            \documentclass{article}
                            \begin{document}
                            $template
                            \end{document}
                        """.trimIndent().toByteArray()
                        )
                    )
                )
            )
        }

    }
}
