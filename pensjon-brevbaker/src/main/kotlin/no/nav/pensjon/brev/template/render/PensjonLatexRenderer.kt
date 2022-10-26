package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.api.model.Bruker
import no.nav.pensjon.brev.api.model.NAVEnhet
import no.nav.pensjon.brev.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brev.latex.LatexAppendable
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object PensjonLatexRenderer : LetterRenderer<RenderedLatexLetter>() {
    private val letterResourceFiles: List<RenderedFile> = listOf(
        RenderedFile.Binary("nav-logo.pdf", getResource("latex/nav-logo.pdf")),
        RenderedFile.PlainText("nav-logo.pdf_tex", getResource("latex/nav-logo.pdf_tex").toString(Charsets.UTF_8)),
        RenderedFile.PlainText("pensjonsbrev_v3.cls", getResource("latex/pensjonsbrev_v3.cls").toString(Charsets.UTF_8)),
        RenderedFile.PlainText("firstpage.tex", getResource("latex/firstpage.tex").toString(Charsets.UTF_8)),
        RenderedFile.PlainText("attachment.tex", getResource("latex/attachment.tex").toString(Charsets.UTF_8)),
        RenderedFile.PlainText("closing.tex", getResource("latex/closing.tex").toString(Charsets.UTF_8)),
        RenderedFile.PlainText("content.tex", getResource("latex/content.tex").toString(Charsets.UTF_8)),
    )

    override fun renderLetter(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>): RenderedLatexLetter =
        RenderedLatexLetter().apply {
            newLatexFile("params.tex") { appendMasterTemplateParameters(scope, template.attachments) }
            newLatexFile("letter.xmpdata") { appendXmpData(scope, template) }
            newLatexFile("letter.tex") { renderLetterTemplate(scope, template) }
            render(scope, template.attachments) { attachmentScope, id, attachment ->
                newLatexFile("attachment_${id}.tex") { renderAttachment(attachmentScope, attachment) }
            }
            addFiles(letterResourceFiles)
        }

    private fun LatexAppendable.appendMasterTemplateParameters(scope: ExpressionScope<*, *>, attachments: List<IncludeAttachment<*, *>>) {
        pensjonLatexSettings.writeLanguageSettings { settingName, settingValue ->
            printNewCmd("felt$settingName") {
                renderText(scope, settingValue)
            }
        }

        println("\\def\\pdfcreationdate{\\string ${pdfCreationTime()}}", escape = false)
        printNewCmd("feltsaksnummer", scope.felles.saksnummer)

        vedleggCommand(scope, attachments)

        with(scope.felles) {
            brukerCommands(bruker)
            saksinfoCommands(vergeNavn)
            navEnhetCommands(avsenderEnhet)
            printNewCmd("feltdato", dokumentDato.format(dateFormatter(scope.language, FormatStyle.LONG)))
            saksbehandlerCommands(signerendeSaksbehandlere)
        }
    }

    private fun LatexAppendable.appendXmpData(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>) {
        printCmd("Title") { arg { renderText(scope, template.title) } }
        printCmd("Language", scope.language.locale().toLanguageTag())
        printCmd("Publisher", scope.felles.avsenderEnhet.navn)
        printCmd("Date", scope.felles.dokumentDato.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    private fun LatexAppendable.appendPdfMetadata(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>) {
        val title = StringBuilder().apply { LatexAppendable(this).apply { renderText(scope, template.title) } }.toString()
        print(
            """
               \pdfinfo{
                    /Creator (${scope.felles.avsenderEnhet.navn.latexEscape()})
                    /Title  (${title})
                    /Language (${scope.language.locale().toLanguageTag().latexEscape()})
                    /Producer (${scope.felles.avsenderEnhet.navn.latexEscape()})
                }
            """.trimIndent(), escape = false
        )
    }

    private fun LatexAppendable.renderLetterTemplate(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>) {
        println("""\documentclass{pensjonsbrev_v3}""", escape = false)
        appendPdfMetadata(scope, template)
        printCmd("begin", "document")
        printCmd("firstpage")
        printCmd("tittel") { arg { renderText(scope, template.title) } }
        renderOutline(scope, template.outline)
        printCmd("closing")
        render(scope, template.attachments) { _, id, _ ->
            printCmd("input", "attachment_$id", escape = false)
        }
        printCmd("end", "document")
    }

    private fun LatexAppendable.saksbehandlerCommands(saksbehandlere: SignerendeSaksbehandlere?) {
        if (saksbehandlere != null) {
            printNewCmd("closingbehandlet", """\closingsaksbehandlet""", escape = false)
            printNewCmd("feltclosingsaksbehandlerfirst", saksbehandlere.saksbehandler)
            printNewCmd("feltclosingsaksbehandlersecond", saksbehandlere.attesterendeSaksbehandler)
        } else {
            printNewCmd("closingbehandlet", """\closingautomatiskbehandlet""", escape = false)
        }
    }

    private fun LatexAppendable.brukerCommands(bruker: Bruker) =
        with(bruker) {
            printNewCmd("feltfoedselsnummerbruker", foedselsnummer.format())
            printNewCmd("feltnavnbruker", listOfNotNull(fornavn, mellomnavn).joinToString(" "))
            printNewCmd("feltetternavnbruker", etternavn)
        }

    private fun LatexAppendable.saksinfoCommands(verge: String?) {
        verge?.also { printNewCmd("feltvergenavn", it) }
        printNewCmd("saksinfomottaker") {
            printCmd("begin", "saksinfotable", "")
            verge?.let {
                println("""\feltvergenavnprefix & \feltvergenavn \\""", escape = false)
                println(
                    """\felt${LanguageSetting.Sakspart.gjelderNavn} & \feltfornavnbruker \space \feltetternavnbruker \\""",
                    escape = false
                )
            } ?: println(
                """\felt${LanguageSetting.Sakspart.navn} & \feltfornavnbruker \space \feltetternavnbruker \\""",
                escape = false
            )
            println(
                """\felt${LanguageSetting.Sakspart.foedselsnummer} & \feltfoedselsnummerbruker \\""",
                escape = false
            )
            println(
                """\felt${LanguageSetting.Sakspart.saksnummer} & \feltsaksnummer \hfill \letterdate\\""",
                escape = false
            )

            printCmd("end", "saksinfotable")
        }
    }

    private fun LatexAppendable.navEnhetCommands(navEnhet: NAVEnhet) =
        with(navEnhet) {
            printNewCmd("feltnavenhet", navn)
            printNewCmd("feltnavenhettlf", telefonnummer.format())
            printNewCmd("feltnavenhetnettside", nettside)
        }

    private fun pdfCreationTime(): String {
        val now = ZonedDateTime.now()
        val formattedTime = now.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssxxx"))
        return "D:${formattedTime.replace(":", "’")}’"
    }

    private fun LatexAppendable.vedleggCommand(scope: ExpressionScope<*, *>, attachments: List<IncludeAttachment<*, *>>) {
        printNewCmd("feltclosingvedlegg") {
            val includedAttachments = attachments.filter { it.predicate.eval(scope) }
            if (includedAttachments.isNotEmpty()) {
                printCmd("begin", "attachmentList")

                render(scope, attachments) { attachmentScope, _, attachment ->
                    print("""\item """, escape = false)
                    renderText(attachmentScope, listOf(attachment.title))
                }
                printCmd("end", "attachmentList")
            }
        }
    }

    private fun LatexAppendable.renderAttachment(scope: ExpressionScope<*, *>, attachment: AttachmentTemplate<*, *>) {
        printCmd("startvedlegg") {
            arg { renderText(scope, listOf(attachment.title)) }
            arg { if (attachment.includeSakspart) printCmd("sakspart") }
        }
        renderOutline(scope, attachment.outline)
        printCmd("sluttvedlegg")
    }

    //
    // Element rendering
    //
    private fun LatexAppendable.renderOutline(scope: ExpressionScope<*, *>, elements: List<OutlineElement<*>>): Unit =
        render(scope, elements) { outlineScope, element -> renderOutlineContent(outlineScope, element) }

    private fun LatexAppendable.renderText(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>): Unit =
        render(scope, elements) { inner, text -> renderTextContent(inner, text) }

    private fun LatexAppendable.renderOutlineContent(
        scope: ExpressionScope<*, *>,
        element: Element.OutlineContent<*>,
    ): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent -> renderParagraphContent(scope, element)
            is Element.OutlineContent.Paragraph -> renderParagraph(scope, element)
            is Element.OutlineContent.Title1 -> printCmd("lettersectiontitle") { arg { renderText(scope, element.text) } }
        }

    private fun LatexAppendable.renderParagraph(scope: ExpressionScope<*, *>, element: Element.OutlineContent.Paragraph<*>): Unit =
        printCmd("templateparagraph") {
            arg {
                render(scope, element.paragraph) { pScope, element ->
                    renderParagraphContent(pScope, element)
                }
            }
        }

    private fun LatexAppendable.renderParagraphContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent<*>): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Form -> renderForm(scope, element)
            is Element.OutlineContent.ParagraphContent.Text -> renderTextContent(scope, element)
            is Element.OutlineContent.ParagraphContent.ItemList -> renderList(scope, element)
            is Element.OutlineContent.ParagraphContent.Table -> renderTable(scope, element)
        }

    private fun LatexAppendable.renderList(scope: ExpressionScope<*, *>, list: Element.OutlineContent.ParagraphContent.ItemList<*>) {
        if (hasAnyContent(scope, list.items)) {
            printCmd("begin", "itemize")
            render(scope, list.items) { itemScope, item ->
                print("""\item """, escape = false)
                renderText(itemScope, item.text)
            }
            printCmd("end", "itemize")
        }
    }

    private fun LatexAppendable.renderTable(scope: ExpressionScope<*, *>, table: Element.OutlineContent.ParagraphContent.Table<*>) {
        if (hasAnyContent(scope, table.rows)) {
            val columnSpec = table.header.colSpec

            printCmd("begin", "letterTable", columnHeadersLatexString(columnSpec))

            renderTableCells(scope, columnSpec.map { it.headerContent }, columnSpec)
            render(scope, table.rows) { rowScope, row ->
                renderTableCells(rowScope, row.cells, columnSpec)
            }

            printCmd("end", "letterTable")
        }
    }

    private fun LatexAppendable.renderTableCells(
        scope: ExpressionScope<*, *>,
        cells: List<Element.OutlineContent.ParagraphContent.Table.Cell<LanguageSupport>>,
        colSpec: List<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<LanguageSupport>>
    ) {
        cells.forEachIndexed { index, cell ->
            val columnSpan = colSpec[index].columnSpan
            if (columnSpan > 1) {
                print("\\SetCell[c=$columnSpan]{}", escape = false)
            }
            renderText(scope, cell.text)
            if (columnSpan > 1) {
                print(" ${"& ".repeat(columnSpan - 1)}", escape = false)
            }
            if (index < cells.lastIndex) {
                print("&", escape = false)
            }
        }
        print("""\\""", escape = false)
    }

    private fun columnHeadersLatexString(columnSpec: List<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<LanguageSupport>>): String =
        columnSpec.joinToString("") {
            ("X" +
                    when (it.alignment) {
                        Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT -> "[l]"
                        Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT -> "[r]"
                    }).repeat(it.columnSpan)
        }

    private fun LatexAppendable.renderTextContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Text<*>): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> renderTextLiteral(element.expr(scope.language).eval(scope), element.fontType)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> renderTextLiteral(element.expression.eval(scope), element.fontType)
            is Element.OutlineContent.ParagraphContent.Text.Literal -> renderTextLiteral(element.text(scope.language), element.fontType)
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> printCmd("newline")
        }

    private fun LatexAppendable.renderTextLiteral(textLiteral: String, fontType: Element.OutlineContent.ParagraphContent.Text.FontType): Unit =
        when (fontType) {
            Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN -> print(textLiteral)
            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD -> printCmd("textbf") { arg { print(textLiteral) } }
            Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC -> printCmd("textit") { arg { print(textLiteral) } }
        }

    private fun LatexAppendable.renderForm(scope: ExpressionScope<*, *>, element: Element.OutlineContent.ParagraphContent.Form<*>): Unit =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Form.MultipleChoice -> {
                if (element.vspace) {
                    printCmd("formvspace")
                }

                printCmd("begin") {
                    arg { print("formChoice") }
                    arg { renderText(scope, listOf(element.prompt)) }
                }

                element.choices.forEach {
                    printCmd("item")
                    renderTextContent(scope, it)
                }

                printCmd("end", "formChoice")
            }

            is Element.OutlineContent.ParagraphContent.Form.Text -> {
                if (element.vspace) {
                    printCmd("formvspace")
                }

                printCmd("formText") {
                    arg {
                        val size = when (element.size) {
                            Element.OutlineContent.ParagraphContent.Form.Text.Size.NONE -> 0
                            Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT -> 25
                            Element.OutlineContent.ParagraphContent.Form.Text.Size.LONG -> 60
                        }
                        renderText(scope, listOf(element.prompt))
                        print(" ${".".repeat(size)}")
                    }
                }
            }
        }
}