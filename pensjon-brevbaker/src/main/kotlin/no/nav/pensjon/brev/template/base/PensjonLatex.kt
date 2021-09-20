package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.api.dto.Mottaker
import no.nav.pensjon.brev.api.dto.NAVEnhet
import no.nav.pensjon.brev.api.dto.SignerendeSaksbehandlere
import no.nav.pensjon.brev.latex.LatexPrintWriter
import no.nav.pensjon.brev.template.*
import java.io.InputStream
import java.io.PrintWriter
import java.time.LocalDate


object PensjonLatex : BaseTemplate() {
    override val languageSettings: LanguageSettings = LanguageSettings.LatexCommands(
        "navnprefix" to newText(
            Language.Bokmal to "Navn:",
            Language.Nynorsk to "Namn:",
            Language.English to "Name:",
        ),
        "saksnummerprefix" to newText(
            Language.Bokmal to "NAVs saksnummer:",
            Language.Nynorsk to "NAVs saksnummer:",
            Language.English to "NAV’s case number:",
        ),
        "foedselsnummerprefix" to newText(
            Language.Bokmal to "Fødselsnummer:",
            Language.Nynorsk to "Fødselsnummer:",
            Language.English to "National identity number:",
        ),
        "returadresseenhetprefix" to newText(
            Language.Bokmal to "Returadresse:",
            Language.Nynorsk to "Returadresse:",
            Language.English to "Return address:",
        ),
        "datoprefix" to newText(
            Language.Bokmal to "Dato:",
            Language.Nynorsk to "Dato:",
            Language.English to "Date:",
        ),
        "postadresseprefix" to newText(
            Language.Bokmal to "Postadresse:",
            Language.Nynorsk to "Postadresse:",
            Language.English to "Mailing address:",
        ),
        "sideprefix" to newText(
            Language.Bokmal to "Side",
            Language.Nynorsk to "Side",
            Language.English to "Page",
        ),
        "sideinfix" to newText(
            Language.Bokmal to "av",
            Language.Nynorsk to "av",
            Language.English to "of",
        ),
        "navenhettlfprefix" to newText(
            Language.Bokmal to "Telefon:",
            Language.Nynorsk to "Telefon:",
            Language.English to "Phone number:",
        ),
        "closingspoersmaal" to newText(
            Language.Bokmal to "Har du spørsmål?",
            Language.Nynorsk to "Har du spørsmål?",
            Language.English to "Do you have questions?",
        ),
        //TODO: Endre slik at vi kan bruke telefonnummer og nettside fra letter.felles
        "closingkontaktoss" to newText(
            Language.Bokmal to "Kontakt oss gjerne på nav.no eller på telefon 55553334. Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask og god hjelp.",
            Language.Nynorsk to "Kontakt oss gjerne på nav.no eller på telefon 55553334. Dersom du gir opp fødselsnummeret ditt når du kontaktar NAV, kan vi lettare gi deg rask og god hjelp.",
            Language.English to "You will find further information at nav.no. You can also contact us by phone 55553334",
        ),
        "closinggreeting" to newText(
            Language.Bokmal to "Med vennlig hilsen",
            Language.Nynorsk to "Med vennleg helsing",
            Language.English to "Yours sincerely",
        ),
        "closingsaksbehandlersuffix" to newText(
            Language.Bokmal to "saksbehandler",
            Language.Nynorsk to "saksbehandlar",
            Language.English to "Executive Officer",
        ),
        "closingautomatisktext" to newText(
            Language.Bokmal to "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
            Language.Nynorsk to "Brevet er produsert automatisk og er difor ikkje underskrive av saksbehandler.",
            Language.English to "This letter has been processed automatically and is therefore not signed by an assessor.",
        ),
        "closingvedleggprefix" to newText(
            Language.Bokmal to "Vedlegg:",
            Language.Nynorsk to "Vedlegg:",
            Language.English to "Attachments:",
        ),
    )

    override fun render(letter: Letter<*>): RenderedLetter =
        RenderedLatexLetter().apply {
            newFile("params.tex").use { masterTemplateParameters(letter, LatexPrintWriter(it)) }
            newFile("letter.tex").use { renderLetterV2(letter, LatexPrintWriter(it)) }
            newFile("nav-logo.pdf").use { getResource("nav-logo.pdf").transferTo(it) }
            newFile("nav-logo.pdf_tex").use { getResource("nav-logo.pdf_tex").transferTo(it) }
            newFile("pensjonsbrev_v2.cls").use { getResource("pensjonsbrev_v2.cls").transferTo(it) }
            letter.template.attachments.forEachIndexed { index, attachment ->
                newFile("attachment_$index.tex").use { renderAttachment(letter, attachment, LatexPrintWriter(it)) }
            }
        }

    private fun renderAttachment(letter: Letter<*>, attachment: AttachmentTemplate<*, *>, printWriter: LatexPrintWriter) =
        with(printWriter) {
            printCmd("startvedlegg", attachment.title.text(letter.language))
            if (attachment.includeSakspart) {
                printCmd("sakspart")
            }
            attachment.outline.forEach { renderElement(letter, it, printWriter) }
            printCmd("sluttvedlegg")
        }

    private fun renderLetterV2(letter: Letter<*>, printWriter: LatexPrintWriter): Unit =
        with(printWriter) {
            println("""\documentclass[12pt]{pensjonsbrev_v2}""", escape = false)
            printCmd("begin", "document")
            printCmd("begin", "letter", """\brevparameter""")
            printCmd("tittel", letter.template.title.text(letter.language))
            contents(letter, printWriter)
            printCmd("closing")
            printCmd("end", "letter")
            letter.template.attachments.forEachIndexed { index, _ ->
                printCmd(
                    "input",
                    "attachment_$index",
                    escape = false
                )
            }
            printCmd("end", "document")
        }

    private fun masterTemplateParameters(letter: Letter<*>, printWriter: LatexPrintWriter) {
        languageSettings.writeLanguageSettings(letter.language, printWriter.printWriter)

        with(printWriter) {
            printNewCmd("feltfoedselsnummer", letter.felles.mottaker.foedselsnummer)
            printNewCmd("feltsaksnummer", letter.felles.saksnummer)
        }
        vedleggCommand(letter, printWriter)

        with(letter.felles) {
            mottakerCommands(mottaker, printWriter)
            navEnhetCommands(avsenderEnhet, printWriter)
            datoCommand(dokumentDato, letter.language, printWriter)
            saksbehandlerCommands(signerendeSaksbehandlere, printWriter)
        }
    }

    private fun saksbehandlerCommands(saksbehandlere: SignerendeSaksbehandlere?, printWriter: LatexPrintWriter) {
        if (saksbehandlere != null) {
            printWriter.printNewCmd("closingbehandlet", """\closingsaksbehandlet""", escape = false)
            printWriter.printNewCmd("feltclosingsaksbehandlerfirst", saksbehandlere.saksbehandler)
            printWriter.printNewCmd("feltclosingsaksbehandlersecond", saksbehandlere.attesterendeSaksbehandler)
        } else {
            printWriter.printNewCmd("closingbehandlet", """\closingautomatiskbehandlet""")
        }
    }

    private fun datoCommand(dato: LocalDate, language: Language, printWriter: LatexPrintWriter) {
        printWriter.printNewCmd("feltdato", dato.format(dateFormatter(language)))
    }

    private fun mottakerCommands(mottaker: Mottaker, printWriter: LatexPrintWriter) =
        with(mottaker) {
            printWriter.printNewCmd("feltfornavnmottaker", fornavn)
            printWriter.printNewCmd("feltetternavnmottaker", etternavn)
            printWriter.printNewCmd("feltmottakeradresselinjeen", adresse.adresselinje1)
            //TODO: fiks null-case her
            printWriter.printNewCmd("feltpostnummermottaker", adresse.postnummer ?: "")
            printWriter.printNewCmd("feltpoststedmottaker", adresse.poststed ?: "")
        }


    private fun navEnhetCommands(navEnhet: NAVEnhet, printWriter: LatexPrintWriter) =
        with(navEnhet.returAdresse) {
            printWriter.printNewCmd("feltnavenhet", navEnhet.navn)
            printWriter.printNewCmd("feltnavenhettlf", navEnhet.telefonnummer)
            printWriter.printNewCmd("feltnavenhetnettside", navEnhet.nettside)
            printWriter.printNewCmd("feltreturadressepostnrsted", "$postNr $postSted")
            printWriter.printNewCmd("feltreturadresse", adresseLinje1)
            printWriter.printNewCmd("feltpostadressepostnrsted", "$postNr $postSted")
            printWriter.printNewCmd("feltpostadresse", adresseLinje1)
        }

    private fun vedleggCommand(letter: Letter<*>, printWriter: LatexPrintWriter): Unit {
        val vedleggListe = letter.template.attachments.map { it.title.text(letter.language) }
            .joinToString("\n") { """\item $it""" }

        printWriter.printNewCmd(
            "feltclosingvedlegg", escape = false, body = """
                \begin{itemize}
                    $vedleggListe
                 \end{itemize}
            """.trimIndent()
        )
    }

    private fun contents(letter: Letter<*>, printWriter: LatexPrintWriter) =
        letter.template.outline.forEach { renderElement(letter, it, printWriter) }

    private fun renderElement(letter: Letter<*>, element: Element<*>, printWriter: LatexPrintWriter): Unit =
        when (element) {
            is Element.Title1 ->
                with(printWriter) {
                    printCmd("lettersectiontitle") {
                        arg { element.title1.forEach { child -> renderElement(letter, child, it) } }
                    }
                }

            is Element.Conditional ->
                with(element) {
                    val toRender = if (predicate.eval(letter)) showIf else showElse
                    toRender.forEach { renderElement(letter, it, printWriter) }
                }

            is Element.Text.Literal ->
                printWriter.print(element.text(letter.language))

            is Element.Text.Phrase ->
                printWriter.print(element.phrase.text(letter.language))

            is Element.Text.Expression ->
                printWriter.print(element.expression.eval(letter))

            is Element.Paragraph ->
                printWriter.printCmd("paragraph") {
                    arg { element.paragraph.forEach { child -> renderElement(letter, child, it) } }
                }

            is Element.Form.Text ->
                with(printWriter) {
                    if (element.vspace) {
                        printCmd("formvspace")
                    }

                    printCmd("formText") {
                        arg {
                            renderElement(letter, element.prompt, it)
                            it.print(" ${".".repeat(element.size)}")
                        }
                    }
                }

            is Element.Form.MultipleChoice ->
                with(printWriter) {
                    if (element.vspace) {
                        printCmd("formvspace")
                    }

                    printCmd("begin") {
                        arg { it.print("formChoice") }
                        arg { renderElement(letter, element.prompt, it) }
                    }

                    element.choices.forEach {
                        printCmd("item")
                        renderElement(letter, it, printWriter)
                    }

                    printCmd("end", "formChoice")
                }

            is Element.NewLine ->
                printWriter.printCmd("newline")
        }

    private fun getResource(fileName: String): InputStream {
        return this::class.java.getResourceAsStream("/$fileName")
            ?: throw IllegalStateException("""Could not find class resource /$fileName""")
    }

}
