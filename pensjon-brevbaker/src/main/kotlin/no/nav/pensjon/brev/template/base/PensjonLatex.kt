package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.something.Fagdelen
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
    override val languageSettings: LanguageSettings = LanguageSettings.LatexCommands(
        "navnprefix" to Element.Text.Literal.create(
            Language.Bokmal to "Navn:",
            Language.Nynorsk to "Namn:",
            Language.English to "Name:",
        ),
        "saksnummerprefix" to Element.Text.Literal.create(
            Language.Bokmal to "NAVs saksnummer:",
            Language.Nynorsk to "NAVs saksnummer:",
            Language.English to "NAV’s case number:",
        ),
        "foedselsnummerprefix" to Element.Text.Literal.create(
            Language.Bokmal to "Fødselsnummer:",
            Language.Nynorsk to "Fødselsnummer:",
            Language.English to "National identity number:",
        ),
        "returadresseenhetprefix" to Element.Text.Literal.create(
            Language.Bokmal to "Returadresse:",
            Language.Nynorsk to "Returadresse:",
            Language.English to "Return address:",
        ),
        "datoprefix" to Element.Text.Literal.create(
            Language.Bokmal to "Dato:",
            Language.Nynorsk to "Dato:",
            Language.English to "Date:",
        ),
        "postadresseprefix" to Element.Text.Literal.create(
            Language.Bokmal to "Postadresse:",
            Language.Nynorsk to "Postadresse:",
            Language.English to "Mailing address:",
        ),
        "sideprefix" to Element.Text.Literal.create(
            Language.Bokmal to "Side",
            Language.Nynorsk to "Side",
            Language.English to "Page",
        ),
        "sideinfix" to Element.Text.Literal.create(
            Language.Bokmal to "av",
            Language.Nynorsk to "av",
            Language.English to "of",
        ),
        "navenhettlfprefix" to Element.Text.Literal.create(
            Language.Bokmal to "Telefon:",
            Language.Nynorsk to "Telefon:",
            Language.English to "Phone number:",
        ),
        //TODO: Burde enhettlf og nettside komme som argument?
        "navenhettlf" to "55553334".let {
            Element.Text.Literal.create(
                Language.Bokmal to it,
                Language.Nynorsk to it,
                Language.English to it,
            )
        },
        "navenhetnettside" to "nav.no".let {
            Element.Text.Literal.create(
                Language.Bokmal to it,
                Language.Nynorsk to it,
                Language.English to it,
            )
        },
        "closingspoersmaal" to Element.Text.Literal.create(
            Language.Bokmal to "Har du spørsmål?",
            Language.Nynorsk to "Har du spørsmål?",
            Language.English to "Do you have questions?",
        ),
        "closingkontaktoss" to Element.Text.Literal.create(
            Language.Bokmal to "Kontakt oss gjerne på nav.no eller på telefon 55553334. Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask og god hjelp.",
            Language.Nynorsk to "Kontakt oss gjerne på nav.no eller på telefon 55553334. Dersom du gir opp fødselsnummeret ditt når du kontaktar NAV, kan vi lettare gi deg rask og god hjelp.",
            Language.English to "You will find further information at nav.no. You can also contact us by phone 55553334",
        ),
        "closinggreeting" to Element.Text.Literal.create(
            Language.Bokmal to "Med vennlig hilsen",
            Language.Nynorsk to "Med vennleg helsing",
            Language.English to "Yours sincerely",
        ),
        "closingsaksbehandlersuffix" to Element.Text.Literal.create(
            Language.Bokmal to "saksbehandler",
            Language.Nynorsk to "saksbehandlar",
            Language.English to "Executive Officer",
        ),
        "closingautomatisktext" to Element.Text.Literal.create(
            Language.Bokmal to "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
            Language.Nynorsk to "Brevet er produsert automatisk og er difor ikkje underskrive av saksbehandler.",
            Language.English to "This letter has been processed automatically and is therefore not signed by an assessor.",
        ),
        "closingvedleggprefix" to Element.Text.Literal.create(
            Language.Bokmal to "Vedlegg:",
            Language.Nynorsk to "Vedlegg:",
            Language.English to "Attachments:",
        ),
    )

    override fun render(letter: Letter): RenderedLetter =
        RenderedLatexLetter().apply {
            newFile("params.tex").use { masterTemplateParameters(letter, PrintWriter(it, true, Charsets.UTF_8)) }
            newFile("letter.tex").use { it.write(renderLetter(letter).toByteArray()) }
            newFile("nav-logo.pdf").use { getResource("nav-logo.pdf").transferTo(it) }
            newFile("nav-logo.pdf_tex").use { getResource("nav-logo.pdf_tex").transferTo(it) }
            newFile("pensjonsbrev_v2.cls").use { getResource("pensjonsbrev_v2.cls").transferTo(it) }
        }


    private fun masterTemplateParameters(letter: Letter, printWriter: PrintWriter) {
        languageSettings.writeLanguageSettings(letter.language, printWriter)

        with(printWriter) {
            println("""\newcommand{\feltfoedselsnummer}{${letter.requiredArg(NorskIdentifikator)}}""")
            println("""\newcommand{\feltsaksnummer}{${letter.requiredArg(SaksNr)}}""")
            println(generateVedlegg())
        }

        with(letter.requiredArg(Felles)) {
            mottakerCommands(mottaker, printWriter)
            returAdresseCommands(returAdresse, printWriter)
            datoCommand(dokumentDato, letter.language, printWriter)
            saksbehandlerCommands(signerendeSaksbehandlere, printWriter)
        }
    }

    private fun saksbehandlerCommands(saksbehandlere: Fagdelen.SignerendeSaksbehandlere?, printWriter: PrintWriter) {
        if (saksbehandlere != null) {
            printWriter.println("""\newcommand{\closingbehandlet}{\closingsaksbehandlet}""")
            printWriter.println("""\newcommand{\feltclosingsaksbehandlerfirst}{${saksbehandlere.saksbehandler}}""")
            printWriter.println("""\newcommand{\feltclosingsaksbehandlersecond}{${saksbehandlere.attesterendeSaksbehandler}}""")
        } else {
            printWriter.println("""\newcommand{\closingbehandlet}{\closingautomatiskbehandlet}""")
        }
    }

    private fun datoCommand(dato: LocalDate, language: Language, printWriter: PrintWriter) {
        val locale = when (language) {
            Language.Bokmal -> Locale.forLanguageTag("NB")
            Language.Nynorsk -> Locale.forLanguageTag("NN")
            Language.English -> Locale.UK
        }
        printWriter.println(
            """\newcommand{\feltdato}{${
                dato.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)
                )
            }}"""
        )
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

    private fun generateVedlegg(): String =
        """\newcommand{\feltclosingvedlegg}{
            \begin{itemize}
                \item test1
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
