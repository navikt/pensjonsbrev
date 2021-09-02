package no.nav.pensjon.brev.something

import no.nav.pensjon.brev.template.*
import java.io.InputStream


object PensjonLatex : BaseTemplate() {
    override val parameters: Set<TemplateParameter> = setOf(
        RequiredParameter(ReturAdresse),
        RequiredParameter(Mottaker),
        RequiredParameter(NorskIdentifikator),
        RequiredParameter(SaksNr),
        RequiredParameter(LetterTitle),
    )

    override fun render(letter: Letter): RenderedLetter =
        RenderedLatexLetter().apply {
            newFile("params.tex").use { it.write(masterTemplateParameters(letter).toByteArray()) }
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
            ${getTextParameters(letter.language)}
            ${generateVedlegg()}
            \newcommand{\closingbehandlet}{\closingsaksbehandlet}
        """.trimIndent() // TODO velg mellom \closingsaksbehandlet velg mellom \closingautomatiskbehandlet
        }

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
                \tittel{${letter.requiredArg(LetterTitle)}}
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

data class PensjonLatexLanguageSettings(
    val navnprefix: String,
    val saksnummerprefix: String,
    val foedselsnummerprefix: String,
    val returadresseenhetprefix: String,
    val navenhet: String,
    val returadressepostnrsted: String,
    val returadresse: String,
    val datoprefix: String,
    val dato: String,
    val postadresseprefix: String,
    val postadressepostnrsted: String,
    val postadresse: String,
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
            navenhet = "NAV Familie- og pensjonsytelser Steinkjer",
            returadressepostnrsted = "0607 OSLO",
            returadresse = "Postboks 6600 Etterstad,",
            datoprefix = "Dato:",
            dato = "6. mai 2021",
            postadresseprefix = "Postadresse:",
            postadressepostnrsted = "0607 OSLO",
            postadresse = "Postboks 6600 Etterstad",
            sideprefix = "Side",
            sideinfix = "av",
            navenhettlfprefix = "Telefon:",
            navenhettlf = "55553334",
            navenhetnettside = "nav.no",
            closingspoersmaal = "Har du spørsmål?",
            closingkontaktoss = "Kontakt oss gjerne på nav.no eller på telefon 55553334. Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask og god hjelp.",
            closinggreeting = "Med vennelig hilsen",
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
            navenhet = "NAV Familie- og pensjonsytelser Steinkjer",
            returadressepostnrsted = "0607 OSLO",
            returadresse = "Postboks 6600 Etterstad,",
            datoprefix = "Dato:",
            dato = "6. mai 2021",
            postadresseprefix = "Postadresse:",
            postadressepostnrsted = "0607 OSLO",
            postadresse = "Postboks 6600 Etterstad",
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
            navenhet = "NAV Familie- og pensjonsytelser Steinkjer",
            returadressepostnrsted = "0607 OSLO",
            returadresse = "Postboks 6600 Etterstad,",
            datoprefix = "Date:",
            dato = "6. mai 2021",
            postadresseprefix = "Mailing address:",
            postadressepostnrsted = "0607 OSLO",
            postadresse = "Postboks 6600 Etterstad",
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