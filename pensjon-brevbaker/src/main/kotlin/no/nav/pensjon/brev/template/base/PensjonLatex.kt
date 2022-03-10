package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.api.model.Mottaker
import no.nav.pensjon.brev.api.model.NAVEnhet
import no.nav.pensjon.brev.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brev.latex.LatexPrintWriter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.Text.FontType.*
import no.nav.pensjon.brev.template.base.pensjonlatex.pensjonLatexSettings
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object PensjonLatex : BaseTemplate() {
    val letterResourceFiles: Map<String, ByteArray> = hashMapOf(
        "nav-logo.pdf" to getResource("latex/nav-logo.pdf"),
        "nav-logo.pdf_tex" to getResource("latex/nav-logo.pdf_tex"),
        "pensjonsbrev_v3.cls" to getResource("latex/pensjonsbrev_v3.cls"),
        "firstpage.tex" to getResource("latex/firstpage.tex"),
        "attachment.tex" to getResource("latex/attachment.tex"),
        "closing.tex" to getResource("latex/closing.tex"),
        "content.tex" to getResource("latex/content.tex"),
        "tabularray.sty" to getResource("latex/tabularray.sty"),
    )

    private fun getResource(fileName: String): ByteArray =
        this::class.java.getResourceAsStream("/$fileName")
            ?.use { it.readAllBytes() }
            ?: throw IllegalStateException("""Could not find latex resource /$fileName""")

    override val languageSettings: LanguageSettings = pensjonLatexSettings

    override fun render(letter: Letter<*>): RenderedLetter =
        RenderedLatexLetter().apply {
            newFile("params.tex").use { masterTemplateParameters(letter, LatexPrintWriter(it)) }
            newFile("letter.xmpdata").use { xmpData(letter, LatexPrintWriter(it)) }
            newFile("letter.tex").use { renderLetterV2(letter, LatexPrintWriter(it)) }
            letter.template.attachments.forEachIndexed { index, attachment ->
                newFile("attachment_$index.tex").use { renderAttachment(letter, attachment, LatexPrintWriter(it)) }
            }
            addFiles(letterResourceFiles)
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
        attachment: IncludeAttachment<*, *>,
        printWriter: LatexPrintWriter
    ) =
        with(printWriter) {
            printCmd("startvedlegg", attachment.template.title.text(letter.language))
            if (attachment.template.includeSakspart) {
                printCmd("sakspart")
            }
            val scope = letter.toScope().let {
                ExpressionScope(attachment.data.eval(it), it.felles, it.language)
            }
            attachment.template.outline.forEach { renderElement(scope, it, printWriter) }
            printCmd("sluttvedlegg")
        }

    private fun renderLetterV2(letter: Letter<*>, printWriter: LatexPrintWriter): Unit =
        with(printWriter) {
            println("""\documentclass{pensjonsbrev_v3}""", escape = false)
            pdfMetadata(letter, printWriter)
            printCmd("begin", "document")
            printCmd("firstpage")
            printCmd("tittel", letter.template.title.text(letter.language))
            contents(letter, printWriter)
            printCmd("closing")
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

    private fun pdfCreationTime(): String {
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
            printWriter.printNewCmd("closingbehandlet", """\closingautomatiskbehandlet""", escape = false)
        }
    }

    private fun datoCommand(dato: LocalDate, language: Language, printWriter: LatexPrintWriter) {
        printWriter.printNewCmd("feltdato", dato.format(dateFormatter(language, FormatStyle.LONG)))
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
                    bodyWriter.println(it.template.title.text(letter.language))
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

            is Element.IncludePhrase<*, *> -> {
                val newScope = ExpressionScope(element.data.eval(scope), scope.felles, scope.language)
                element.phrase.elements.forEach { renderElement(newScope, it, printWriter) }
            }

            is Element.Conditional ->
                with(element) {
                    val toRender = if (predicate.eval(scope)) showIf else showElse
                    toRender.forEach { renderElement(scope, it, printWriter) }
                }

            is Element.Text.Literal -> {
                val textLiteral = element.text(scope.language)
                with(printWriter) {
                    when (element.fontType) {
                        PLAIN -> print(textLiteral)
                        BOLD -> printCmd("textbf") { arg { print(textLiteral) } }
                        ITALIC -> printCmd("textit") { arg { print(textLiteral) } }
                    }
                }

            }

            is Element.Text.Expression ->
                printWriter.print(element.expression.eval(scope))

            is Element.Text.Expression.ByLanguage ->
                printWriter.print(element.expr(scope.language).eval(scope))

            is Element.ItemList ->
                with(printWriter) {
                    val items = element.items.filter { it.condition == null || it.condition.eval(scope) }
                    if (items.isEmpty()) return
                    printCmd("begin") {
                        arg { print("itemize") }
                    }
                    items.forEach { item ->
                        print("""\item """, escape = false)
                        item.elements.forEach {
                            renderElement(scope, it, this)
                        }
                    }
                    printCmd("end") {
                        arg { print("itemize") }
                    }
                }
            is Element.Paragraph ->
                printWriter.printCmd("templateparagraph") {
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
            is Element.Table ->
                with(printWriter) {
                    val rows = element.rows.filter { it.condition == null || it.condition.eval(scope) }
                    if (rows.isEmpty()) return

                    val tableWidth = element.width
                    printCmd("begin") {
                        arg { print("letterTable") }
                        arg { print("X".repeat(tableWidth)) }
                    }

                    val header = element.columnHeader
                    listOf(header)
                    rows.forEach { row ->
                        printCells(row.cells, scope, printWriter)
                        print("""\\""", escape = false)
                    }
                    printCmd("end") {
                        arg { print("letterTable") }
                    }
                }
        }

    private fun LatexPrintWriter.printCells(
        cells: List<Element.Table.Cell<out LanguageSupport>>,
        scope: ExpressionScope<*, *>,
        printWriter: LatexPrintWriter
    ) {
        cells.forEachIndexed { index, cell ->
            if (cell.cellColumns > 1) {
                print("\\SetCell[c=${cell.cellColumns}]{}", escape = false)
            }
            cell.elements.forEach { cellElement ->
                renderElement(scope, cellElement, printWriter)
            }
            if (cell.cellColumns > 1) {
                print(" ${"& ".repeat(cell.cellColumns - 1)}", escape = false)
            }
            if (index < cells.lastIndex) {
                print("&", escape = false)
            }
        }
    }
}
