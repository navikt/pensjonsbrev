package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.latex.LatexPrintWriter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*
import java.io.InputStream
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object PensjonLatex : BaseTemplate() {
    override val languageSettings: LanguageSettings = languageSettings {
        setting("navnprefix") {
            text(
                Language.Bokmal to "Navn:",
                Language.Nynorsk to "Namn:",
                Language.English to "Name:",
            )
        }

        setting("saksnummerprefix") {
            text(
                Language.Bokmal to "NAVs saksnummer:",
                Language.Nynorsk to "NAVs saksnummer:",
                Language.English to "NAV’s case number:",
            )
        }

        setting("foedselsnummerprefix") {
            text(
                Language.Bokmal to "Fødselsnummer:",
                Language.Nynorsk to "Fødselsnummer:",
                Language.English to "National identity number:",
            )
        }

        setting("returadresseenhetprefix") {
            text(
                Language.Bokmal to "Returadresse:",
                Language.Nynorsk to "Returadresse:",
                Language.English to "Return address:",
            )
        }

        setting("datoprefix") {
            text(
                Language.Bokmal to "Dato:",
                Language.Nynorsk to "Dato:",
                Language.English to "Date:",
            )
        }

        setting("postadresseprefix") {
            text(
                Language.Bokmal to "Postadresse:",
                Language.Nynorsk to "Postadresse:",
                Language.English to "Mailing address:",
            )
        }

        setting("sideprefix") {
            text(
                Language.Bokmal to "Side",
                Language.Nynorsk to "Side",
                Language.English to "Page",
            )
        }

        setting("sideinfix") {
            text(
                Language.Bokmal to "av",
                Language.Nynorsk to "av",
                Language.English to "of",
            )
        }

        setting("navenhettlfprefix") {
            text(
                Language.Bokmal to "Telefon:",
                Language.Nynorsk to "Telefon:",
                Language.English to "Phone number:",
            )
        }

        setting("closingspoersmaal") {
            text(
                Language.Bokmal to "Har du spørsmål?",
                Language.Nynorsk to "Har du spørsmål?",
                Language.English to "Do you have questions?",
            )
        }

        setting("closingkontaktoss") {
            text(
                Language.Bokmal to "Kontakt oss gjerne på ",
                Language.Nynorsk to "Kontakt oss gjerne på ",
                Language.English to "You will find further information at ",
            )
            val avsender = felles().select(Felles::avsenderEnhet)

            eval { avsender.select(NAVEnhet::nettside) }
            text(
                Language.Bokmal to " eller på telefon ",
                Language.Nynorsk to " eller på telefon ",
                Language.English to ". You can also contact us by phone ",
            )
            eval { avsender.select(NAVEnhet::telefonnummer).select(Telefonnummer::format) }
            text(
                Language.Bokmal to ". Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask og god hjelp.",
                Language.Nynorsk to ". Dersom du gir opp fødselsnummeret ditt når du kontaktar NAV, kan vi lettare gi deg rask og god hjelp.",
                Language.English to ".",
            )
        }

        setting("closinggreeting") {
            text(
                Language.Bokmal to "Med vennlig hilsen",
                Language.Nynorsk to "Med vennleg helsing",
                Language.English to "Yours sincerely",
            )
        }

        setting("closingsaksbehandlersuffix") {
            text(
                Language.Bokmal to "saksbehandler",
                Language.Nynorsk to "saksbehandlar",
                Language.English to "Executive Officer",
            )
        }
        setting("closingautomatisktext") {
            text(
                Language.Bokmal to "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
                Language.Nynorsk to "Brevet er produsert automatisk og er difor ikkje underskrive av saksbehandler.",
                Language.English to "This letter has been processed automatically and is therefore not signed by an assessor.",
            )
        }
        setting("closingvedleggprefix") {
            text(
                Language.Bokmal to "Vedlegg:",
                Language.Nynorsk to "Vedlegg:",
                Language.English to "Attachments:",
            )
        }
    }

    override fun render(letter: Letter<*>): RenderedLetter =
        RenderedLatexLetter().apply {
            newFile("params.tex").use { masterTemplateParameters(letter, LatexPrintWriter(it)) }
            newFile("letter.xmpdata").use { xmpData(letter, LatexPrintWriter(it)) }
            newFile("letter.tex").use { renderLetterV2(letter, LatexPrintWriter(it)) }
            newFile("nav-logo.pdf").use { getResource("nav-logo.pdf").transferTo(it) }
            newFile("nav-logo.pdf_tex").use { getResource("nav-logo.pdf_tex").transferTo(it) }
            newFile("pensjonsbrev_v2.cls").use { getResource("pensjonsbrev_v2.cls").transferTo(it) }
            letter.template.attachments.forEachIndexed { index, attachment ->
                newFile("attachment_$index.tex").use { renderAttachment(letter, attachment, LatexPrintWriter(it)) }
            }
        }

    private fun xmpData(letter: Letter<*>, latexPrintWriter: LatexPrintWriter) {
        with(latexPrintWriter) {
            printCmd("Title", letter.template.title.text(letter.language))
            printCmd("Language", letter.language.locale().toLanguageTag())
            printCmd("Publisher", letter.felles.avsenderEnhet.navn)
            printCmd("Date", letter.felles.dokumentDato.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
    }

    private fun pdfMetadata(letter: Letter<*>, latexPrintWriter: LatexPrintWriter) =
        latexPrintWriter.print(
            """
               \pdfinfo{
                    /Creator (${letter.felles.avsenderEnhet.navn.latexEscape()})
                    /Title  (${letter.template.title.text(letter.language).latexEscape()})
                    /Language (${letter.language.locale().toLanguageTag().latexEscape()})
                    /Producer (${letter.felles.avsenderEnhet.navn.latexEscape()})
                }
            """.trimIndent(), escape = false
        )

    private fun renderAttachment(
        letter: Letter<*>,
        attachment: AttachmentTemplate<*, *>,
        printWriter: LatexPrintWriter
    ) =
        with(printWriter) {
            printCmd("startvedlegg", attachment.title.text(letter.language))
            if (attachment.includeSakspart) {
                printCmd("sakspart")
            }
            val scope = letter.toScope()
            attachment.outline.forEach { renderElement(scope, it, printWriter) }
            printCmd("sluttvedlegg")
        }

    private fun renderLetterV2(letter: Letter<*>, printWriter: LatexPrintWriter): Unit =
        with(printWriter) {
            println("""\documentclass[12pt]{pensjonsbrev_v2}""", escape = false)
            pdfMetadata(letter, printWriter)
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
        languageSettings.writeLanguageSettings { settingName, settingValue ->
            printWriter.printNewCmd("felt$settingName") { bodyWriter ->
                val scope = letter.toScope()
                settingValue.forEach { renderElement(scope, it, bodyWriter) }
            }
        }

        with(printWriter) {
            println("\\def\\pdfcreationdate{\\string ${pdfCreationTime()}}", escape = false)
            printNewCmd("feltfoedselsnummer", letter.felles.mottaker.foedselsnummer.format())
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

    fun pdfCreationTime(): String {
        val now = ZonedDateTime.now()
        val formattedTime = now.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssxxx"))
        return "D:${formattedTime.replace(":", "’")}’"
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
            printWriter.printNewCmd("feltmottakeradresselineone", adresse.linje1)
            printWriter.printNewCmd("feltmottakeradresselinetwo", adresse.linje2)
            printWriter.printNewCmd("feltmottakeradresselinethree", adresse.linje3 ?: "")
            printWriter.printNewCmd("feltmottakeradresselinefour", adresse.linje4 ?: "")
            printWriter.printNewCmd("feltmottakeradresselinefive", adresse.linje5 ?: "")
            //TODO: fiks null-case her
        }


    private fun navEnhetCommands(navEnhet: NAVEnhet, printWriter: LatexPrintWriter) =
        with(navEnhet.returAdresse) {
            printWriter.printNewCmd("feltnavenhet", navEnhet.navn)
            printWriter.printNewCmd("feltnavenhettlf", navEnhet.telefonnummer.format())
            printWriter.printNewCmd("feltnavenhetnettside", navEnhet.nettside)
            printWriter.printNewCmd("feltreturadressepostnrsted", "$postNr $postSted")
            printWriter.printNewCmd("feltreturadresse", adresseLinje1)
            printWriter.printNewCmd("feltpostadressepostnrsted", "$postNr $postSted")
            printWriter.printNewCmd("feltpostadresse", adresseLinje1)
        }

    private fun vedleggCommand(letter: Letter<*>, printWriter: LatexPrintWriter) {
        printWriter.printNewCmd("feltclosingvedlegg") { bodyWriter ->
            if (letter.template.attachments.isNotEmpty()) {
                bodyWriter.printCmd("closingvedleggspace")
                bodyWriter.printCmd("begin", "attachmentList")
                letter.template.attachments.forEach {
                    bodyWriter.print("""\item """, escape = false)
                    bodyWriter.println(it.title.text(letter.language))
                }
                bodyWriter.printCmd("end", "attachmentList")
            }
        }
    }

    private fun contents(letter: Letter<*>, printWriter: LatexPrintWriter) {
        val scope = letter.toScope()
        letter.template.outline.forEach { renderElement(scope, it, printWriter) }
    }

    private fun renderElement(scope: ExpressionScope<*, *>, element: Element<*>, printWriter: LatexPrintWriter): Unit =
        when (element) {
            is Element.Title1 ->
                with(printWriter) {
                    printCmd("lettersectiontitle") {
                        arg { element.title1.forEach { child -> renderElement(scope, child, it) } }
                    }
                }

            is Element.IncludePhrase<*,*> -> {
                val newScope = ExpressionScope(element.data.eval(scope), scope.felles, scope.language)
                element.phrase.elements.forEach { renderElement(newScope, it, printWriter) }
            }

            is Element.Conditional ->
                with(element) {
                    val toRender = if (predicate.eval(scope)) showIf else showElse
                    toRender.forEach { renderElement(scope, it, printWriter) }
                }

            is Element.Text.Literal ->
                printWriter.print(element.text(scope.language))

            is Element.Text.Expression ->
                printWriter.print(element.expression.eval(scope))

            is Element.Text.Expression.ByLanguage ->
                printWriter.print(element.expr(scope.language).eval(scope))

            is Element.Paragraph ->
                printWriter.printCmd("paragraph") {
                    arg { element.paragraph.forEach { child -> renderElement(scope, child, it) } }
                }

            is Element.Form.Text ->
                with(printWriter) {
                    if (element.vspace) {
                        printCmd("formvspace")
                    }

                    printCmd("formText") {
                        arg {
                            renderElement(scope, element.prompt, it)
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
                        arg { renderElement(scope, element.prompt, it) }
                    }

                    element.choices.forEach {
                        printCmd("item")
                        renderElement(scope, it, printWriter)
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
