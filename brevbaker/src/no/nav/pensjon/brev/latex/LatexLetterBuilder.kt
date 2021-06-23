package no.nav.pensjon.brev.latex

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.dto.PdfCompilationInput
import no.nav.pensjon.brev.dto.Letter
import no.nav.pensjon.brev.dto.PDFCompilationOutput

class LatexLetterBuilder(private val laTeXCompilerService: LaTeXCompilerService) {
    fun buildLatex(letter: Letter): PDFCompilationOutput {
        val template = letter.letterTemplate.template
        return runBlocking {
            laTeXCompilerService.producePDF(
                PdfCompilationInput(
                    mapOf(
                        "letter.tex" to """
                            \documentclass{article}
                            \begin{document}
                            $template
                            \end{document}
                        """.trimIndent().toByteArray()
                    )
                )
            )
        }

    }
}
