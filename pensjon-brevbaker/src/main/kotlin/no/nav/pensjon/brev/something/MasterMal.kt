package no.nav.pensjon.brev.something

import no.nav.pensjon.brev.template.*
import java.io.InputStream
import java.io.PrintWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


object PensjonLatex : BaseTemplate() {
    override val parameters: Set<TemplateParameter> = setOf(
        RequiredParameter(NorskIdentifikator),
        RequiredParameter(SaksNr),
        RequiredParameter(Felles)
    )

    override fun render(letter: Letter): RenderedLetter =
        RenderedLatexLetter().apply {
            newFile("params.tex").use { masterTemplateParameters(letter, PrintWriter(it, true, Charsets.UTF_8)) }
            newFile("letter.tex").use { it.write(renderLetter(letter).toByteArray()) }
            newFile("nav-logo.pdf").use { getResource("nav-logo.pdf").transferTo(it) }
            newFile("nav-logo.pdf_tex").use { getResource("nav-logo.pdf_tex").transferTo(it) }
            newFile("pensjonsbrev_v2.cls").use { getResource("pensjonsbrev_v2.cls").transferTo(it) }
        }

    private fun getLanguageSettings(language: Language): PensjonLatexLanguageSettings =
        when (language) {
            Language.Bokmal -> PensjonLatexLanguageSettings.bokmal
            Language.English -> PensjonLatexLanguageSettings.english
            Language.Nynorsk -> PensjonLatexLanguageSettings.nynorsk
        }

    private fun masterTemplateParameters(letter: Letter, printWriter: PrintWriter) {
        with(printWriter) {
            println("""\newcommand{\feltfoedselsnummer}{${letter.requiredArg(NorskIdentifikator)}}""")
            println("""\newcommand{\feltsaksnummer}{${letter.requiredArg(SaksNr)}}""")
            println("""\newcommand{\closingbehandlet}{${closingCommand(letter)}}""")
            println(getTextParameters(letter.language))
            println(generateVedlegg())
        }
        with(letter.requiredArg(Felles)) {
            mottakerCommands(mottaker, printWriter)
            returAdresseCommands(returAdresse, printWriter)
            datoCommand(dokumentDato, letter.language, printWriter)
        }
    }

    private fun datoCommand(dato: LocalDate, language: Language, printWriter: PrintWriter) {
        val locale = when(language) {
            Language.Bokmal -> Locale.forLanguageTag("NO")
            Language.English -> Locale.UK //TODO: Finn ut om det skal brukes ENGLISH, UK eller US
            Language.Nynorsk -> Locale.forLanguageTag("NO")
        }
        printWriter.println("""\newcommand{\feltdato}{${dato.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale))}}""")
    }

    private fun mottakerCommands(mottaker: Fagdelen.Mottaker, printWriter: PrintWriter) =
        with(mottaker) {
            printWriter.println("""\newcommand{\feltfornavnmottaker}{$fornavn}""")
            printWriter.println("""\newcommand{\feltetternavnmottaker}{$etternavn}""")
            printWriter.println("""\newcommand{\feltgatenavnmottaker}{$gatenavn}""")
            printWriter.println("""\newcommand{\felthusnummermottaker}{$husnummer}""")
            printWriter.println("""\newcommand{\feltpostnummermottaker}{$postnummer}""")
            printWriter.println("""\newcommand{\feltpoststedmottaker}{$poststed}""")
        }


    private fun returAdresseCommands(returAdresse: Fagdelen.ReturAdresse, printWriter: PrintWriter) =
        with(returAdresse) {
            printWriter.println("""\newcommand{\feltnavenhet}{$navEnhetsNavn}""")
            printWriter.println("""\newcommand{\feltreturadressepostnrsted}{$postNr $postSted}""")
            printWriter.println("""\newcommand{\feltreturadresse}{$adresseLinje1}""")
            printWriter.println("""\newcommand{\feltpostadressepostnrsted}{$postNr $postSted}""")
            printWriter.println("""\newcommand{\feltpostadresse}{$adresseLinje1}""")
        }

    // TODO velg mellom \closingsaksbehandlet velg mellom \closingautomatiskbehandlet
    private fun closingCommand(letter: Letter) =
        """\closingsaksbehandlet"""

    private fun getTextParameters(language: Language): String =
        getLanguageSettings(language).toMap()
            .entries
            .joinToString("\n") { """\newcommand{\felt${it.key}}{${it.value}}""" }

    private fun generateVedlegg(): String =
        """\newcommand{\feltclosingvedlegg}{
            \begin{itemize}
                \item test1
                \item test2
            \end{itemize}    
        }""".trimMargin() //TODO generer vedlegg

    private fun renderLetter(letter: Letter): String =
        """
            \documentclass[12pt]{pensjonsbrev_v2}
            \begin{document}
                \begin{letter}{\brevparameter}
                \tittel{${letter.template.title.text(letter.language)}}
                ${contents(letter)}
                \closing
                \end{letter}
            \end{document}
        """

    private fun contents(letter: Letter): StringBuilder {
        val stringBuilder = StringBuilder()
        letter.template.outline.forEach { renderElement(letter, it, stringBuilder) }
        return stringBuilder
    }

    private fun renderElement(letter: Letter, element: Element<*>, stringBuilder: StringBuilder) {
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
            is Element.Text.Literal -> stringBuilder.append(element.text(letter.language))
            is Element.Text.Phrase -> stringBuilder.append(element.phrase.text(letter.language))
            is Element.Text.Expression -> stringBuilder.append(element.expression.eval(letter))
            is Element.Paragraph ->
                with(stringBuilder) {
                    append("""\letterparagraph{""")
                    element.paragraph.forEach { child -> renderElement(letter, child, stringBuilder) }
                    appendLine("}")
                }
        }
    }

    private fun getResource(fileName: String): InputStream {
        return this::class.java.getResourceAsStream("/$fileName")
            ?: throw IllegalStateException("""Could not find class resource /$fileName""")
    }

}

//TODO: Fjern felter som skal komme som argumenter, f.eks navEnhet, navenhettlf osv.
data class PensjonLatexLanguageSettings(
    val navnprefix: String,
    val saksnummerprefix: String,
    val foedselsnummerprefix: String,
    val returadresseenhetprefix: String,
    val datoprefix: String,
    val postadresseprefix: String,
    val sideprefix: String,
    val sideinfix: String,
    val navenhettlfprefix: String,
    val navenhettlf: String,
    val navenhetnettside: String,
    val closingspoersmaal: String,
    val closingkontaktoss: String,
    val closinggreeting: String,
    val closingsaksbehandlerfirst: String,
    val closingsaksbehandlersecond: String,
    val closingsaksbehandlersuffix: String,
    val closingvedleggprefix: String,
) : LanguageSettings {
    companion object {
        val bokmal = PensjonLatexLanguageSettings(
            navnprefix = "Navn:",
            saksnummerprefix = "NAVs saksnummer:",
            foedselsnummerprefix = "Fødselsnummer:",
            returadresseenhetprefix = "Returadresse:",
            datoprefix = "Dato:",
            postadresseprefix = "Postadresse:",
            sideprefix = "Side",
            sideinfix = "av",
            navenhettlfprefix = "Telefon:",
            navenhettlf = "55553334",
            navenhetnettside = "nav.no",
            closingspoersmaal = "Har du spørsmål?",
            closingkontaktoss = "Kontakt oss gjerne på nav.no eller på telefon 55553334. Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask og god hjelp.",
            closinggreeting = "Med vennlig hilsen",
            closingsaksbehandlerfirst = "Saksbehandler Saksbehandlerson",
            closingsaksbehandlersecond = "Ola Attesterende Saksbehandlerson",
            closingsaksbehandlersuffix = "saksbehandler",
            closingvedleggprefix = "Vedlegg:",
        )

        val nynorsk = PensjonLatexLanguageSettings(
            navnprefix = "Namn:",
            saksnummerprefix = "NAVs saksnummer:",
            foedselsnummerprefix = "Fødselsnummer:",
            returadresseenhetprefix = "Returadresse:",
            datoprefix = "Dato:",
            postadresseprefix = "Postadresse:",
            sideprefix = "Side",
            sideinfix = "av",
            navenhettlfprefix = "Telefon:",
            navenhettlf = "55553334",
            navenhetnettside = "nav.no",
            closingspoersmaal = "Har du spørsmål?",
            closingkontaktoss = "Kontakt oss gjerne på nav.no eller på telefon 55553334. Dersom du gir opp fødselsnummeret ditt når du kontaktar NAV, kan vi lettare gi deg rask og god hjelp.",
            closinggreeting = "Med vennleg helsing",
            closingsaksbehandlerfirst = "Saksbehandler Saksbehandlerson",
            closingsaksbehandlersecond = "Ola Attesterende Saksbehandlerson",
            closingsaksbehandlersuffix = "saksbehandler",
            closingvedleggprefix = "Vedlegg:",
        )

        val english = PensjonLatexLanguageSettings(
            navnprefix = "Name:",
            saksnummerprefix = "NAV’s case number:",
            foedselsnummerprefix = "National identity number:",
            returadresseenhetprefix = "Return address:",
            datoprefix = "Date:",
            postadresseprefix = "Mailing address:",
            sideprefix = "Page",
            sideinfix = "of",
            navenhettlfprefix = "Phone number:",
            navenhettlf = "55553334",
            navenhetnettside = "nav.no",
            closingspoersmaal = "Do you have questions?",
            closingkontaktoss = "You will find further information at nav.no. You can also contact us by phone 55553334",
            closinggreeting = "Yours sincerely",
            closingsaksbehandlerfirst = "Saksbehandler Saksbehandlerson",
            closingsaksbehandlersecond = "Ola Attesterende Saksbehandlerson",
            closingsaksbehandlersuffix = "saksbehandler",
            closingvedleggprefix = "Attachments:",
        )
    }
}