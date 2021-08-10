package no.nav.pensjon.brev.something

import no.nav.pensjon.brev.dto.PdfCompilationInput
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import java.io.OutputStream
import java.util.*


object PensjonLatex : BaseTemplate() {
    private val laTeXCompilerService = LaTeXCompilerService()
    override val parameters: Set<TemplateParameter> = setOf(
        RequiredParameter(ReturAdresse),
        RequiredParameter(Mottaker),
        RequiredParameter(NorskIdentifikator),
        RequiredParameter(SaksNr),
        RequiredParameter(LetterTitle),
    )

    private val encoder = Base64.getEncoder()
    var dict = ResourceBundle.getBundle("mastertemplate", Locale.forLanguageTag("no"))

    override fun render(letter: Letter, out: OutputStream) {
        masterTemplateParameters(letter)

        out.write(
            laTeXCompilerService.producePDF(
                PdfCompilationInput(
                    mapOf(
                        "letter.tex" to renderLetter(letter),
                        "pensjonsbrev_v2.cls" to getResource("pensjonsbrev_v2.cls"),
                        "nav-logo.pdf" to getResource("nav-logo.pdf"),
                        "params.tex" to masterTemplateParameters(letter),
                        "nav-logo.pdf_tex" to getResource("nav-logo.pdf_tex"),
                    )
                )
            ).toByteArray()
        )
    }

    private fun masterTemplateParameters(letter: Letter) =
        with(letter.requiredArg(Mottaker)) {
            """
            \newcommand{\feltfornavnmottaker}{$fornavn}
            \newcommand{\feltetternavnmottaker}{$etternavn}
            \newcommand{\feltgatenavnmottaker}{$gatenavn}
            \newcommand{\felthusnummermottaker}{$husnummer}
            \newcommand{\feltpostnummermottaker}{$postnummer}
            \newcommand{\feltpoststedmottaker}{$poststed}
            \newcommand{\feltfoedselsnummer}{${letter.requiredArg(NorskIdentifikator)}}
            \newcommand{\feltsaksnummer}{${letter.requiredArg(SaksNr)}}
            ${getTextParameters()}
            ${generateVedlegg()}
        """.encodeTemplate()
        }

    private fun getTextParameters(): String {
        val stringBuilder = StringBuilder()
        dict.keySet().forEach {
            stringBuilder.append("""\newcommand{\felt${it}}{${dict.getString(it)}}""")
        }
        return stringBuilder.toString()
    }

    private fun generateVedlegg() = """\newcommand{\feltclosingvedlegg}{
            \begin{itemize}
                \item test1
                \item test2
            \end{itemize}    
        }""".trimMargin() //TODO generer vedlegg

    private fun renderLetter(letter: Letter) =
        """
            \documentclass[12pt]{pensjonsbrev_v2}
            \begin{document}
                \begin{letter}{\brevparameter}
                \tittel{${letter.requiredArg(LetterTitle)}}
                ${contents(letter)}
                \closing
                \end{letter}
            \end{document}
        """.encodeTemplate()

    private fun contents(letter: Letter): StringBuilder {
        val stringBuilder = StringBuilder()
        letter.template.outline.forEach { renderElement(letter, it, stringBuilder) }
        return stringBuilder
    }

    private fun renderElement(letter: Letter, element: Element, stringBuilder: StringBuilder) {
        when (element) {
            is Element.Title1 ->
                with(stringBuilder) {
                    append("""\lettersectiontitle{""")
                    element.title1.forEach { child -> renderElement(letter, child, stringBuilder) }
                    appendLine("}")
                }
            is Element.Conditional ->
                with(element) {
                    val toRender = if (predicate.eval(letter)) element.showIf else element.showElse
                    toRender.forEach { renderElement(letter, it, stringBuilder) }
                }
            is Element.Section -> TODO("NOT Implemented rendering of: ${element.schema}")
            is Element.Text.Literal -> stringBuilder.append(element.text)
            is Element.Text.Phrase -> stringBuilder.append(element.phrase.text())
            is Element.Text.Expression -> stringBuilder.append(element.expression.eval(letter))
            is Element.Paragraph ->
                with(stringBuilder) {
                    append("""\letterparagraph{""")
                    element.title1.forEach { child -> renderElement(letter, child, stringBuilder) }
                    appendLine("}")
                }
        }
    }

    private fun getResource(fileName: String): String {
        val classPath = """/$fileName"""
        val file = this::class.java.getResourceAsStream(classPath)?.readAllBytes()
            ?: throw IllegalStateException("""Could not find class resource $classPath""")
        return encoder.encodeToString(file)
    }

    private fun String.encodeTemplate() = encoder.encodeToString(this.trimIndent().toByteArray())

}

