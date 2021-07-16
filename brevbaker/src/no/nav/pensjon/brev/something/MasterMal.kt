package no.nav.pensjon.brev.something

import no.nav.pensjon.brev.template.*
import java.io.OutputStream
import java.io.PrintWriter

object PensjonLatex : BaseTemplate() {
    override val parameters: Set<TemplateParameter> = setOf(
        RequiredParameter(ReturAdresse)
    )

    override fun render(letter: Letter, out: OutputStream) {
        PrintWriter(out, false, Charsets.UTF_8).use {
            documentclass(it)
            letterhead(letter, it)
            startLetter(letter, it)
            contents(letter, it)
            endLetter(it)
        }
    }

    private fun documentclass(printer: PrintWriter) =
        printer.println(
            """
            \documentclass[version=last,
                foldmarks=false,
                addrfield=backgroundimage]{scrlttr2}
            \usepackage[utf8]{inputenc}
            \usepackage[norsk]{babel}
            \usepackage{graphicx}
            \usepackage{listings}
            \usepackage{xcolor}
            \usepackage{times}
            \usepackage[T1]{fontenc}

            \pagenumbering{arabic}
            \setlength{\parindent}{0em}
            \setlength{\parskip}{1em}
            \setplength{locwidth}{200pt}
            \setplength{toaddrheight}{170pt}
        """.trimIndent()
        )

    private fun letterhead(letter: Letter, printer: PrintWriter) {
        val returAdresse = letter.requiredArg(ReturAdresse)

        printer.println(
            """
            \setkomavar{date}{\today}
            \setkomavar{fromname}[description ]{\small Returadresse: ${returAdresse.navEnhetsNavn}}
            \setkomavar{fromaddress}[description ]{\small ${returAdresse.adresseLinje1}, ${returAdresse.postNr} ${returAdresse.postNr} }
            \setkomavar{toaddress}[description ]{
                \uppercase{\feltgatenavnmottaker \space \felthusnummermottaker
                \\ \feltpoststedmottaker, \feltpostnummermottaker }}
            \setkomavar{addresseeimage}[description ]{ \includegraphics[width=2cm]{nav} }
        """.trimIndent()
        )
    }

    private fun startLetter(letter: Letter, printer: PrintWriter) =
        printer.println(
            """
            \begin{document}
            \begin{letter}{%
                Alexander Hoem \space Rosbach \newline 
                \uppercase{Agmund Bolts vei \space 2 } \newline
                0664 \space Oslo%
            }
        """.trimIndent()
        )

    private fun contents(letter: Letter, printWriter: PrintWriter) =
        letter.template.outline.forEach { renderElement(letter, it, printWriter) }

    private fun endLetter(printer: PrintWriter) =
        printer.println(
            """
            \end{letter}
            \end{document}
        """.trimIndent()
        )

    private fun renderElement(letter: Letter, element: Element, printWriter: PrintWriter): Unit =
        when (element) {
            is Element.Title1 ->
                with(printWriter) {
                    println("""{\large """)
                    element.title1.forEach { child -> renderElement(letter, child, printWriter) }
                    println("}")
                }
            is Element.Conditional ->
                with(element) {
                    val toRender = if (predicate.eval(letter)) element.showIf else element.showElse
                    toRender.forEach { renderElement(letter, it, printWriter) }
                }
            is Element.Section -> TODO("NOT Implemented rendering of: ${element.schema}")
            is Element.Text.Literal -> printWriter.println(element.text)
            is Element.Text.Phrase -> printWriter.println(element.phrase)
            is Element.Text.Expression -> printWriter.print(element.expression.eval(letter))
        }

}