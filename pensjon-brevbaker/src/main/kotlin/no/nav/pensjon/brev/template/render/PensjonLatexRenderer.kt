package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.api.model.Bruker
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.NAVEnhet
import no.nav.pensjon.brev.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brev.latex.LatexAppendable
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val DOCUMENT_PRODUCER = "brevbaker / pdf-bygger med LaTeX"

object PensjonLatexRenderer : LetterRenderer<RenderedLatexLetter>() {
    private val letterResourceFiles: List<RenderedFile> = listOf(
        RenderedFile.Binary("nav-logo.pdf", getResource("latex/nav-logo.pdf")),
        RenderedFile.PlainText("nav-logo.pdf_tex", getResource("latex/nav-logo.pdf_tex").toString(Charsets.UTF_8)),
        RenderedFile.PlainText(
            "pensjonsbrev_v3.cls",
            getResource("latex/pensjonsbrev_v3.cls").toString(Charsets.UTF_8)
        ),
        RenderedFile.PlainText("firstpage.tex", getResource("latex/firstpage.tex").toString(Charsets.UTF_8)),
        RenderedFile.PlainText("attachment.tex", getResource("latex/attachment.tex").toString(Charsets.UTF_8)),
        RenderedFile.PlainText("closing.tex", getResource("latex/closing.tex").toString(Charsets.UTF_8)),
        RenderedFile.PlainText("content.tex", getResource("latex/content.tex").toString(Charsets.UTF_8)),
    )

    override fun renderLetter(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>): RenderedLatexLetter =
        RenderedLatexLetter().apply {
            newLatexFile("params.tex") {
                appendMasterTemplateParameters(scope, template.attachments, template.letterMetadata.brevtype)
            }
            newLatexFile("letter.xmpdata") { appendXmpData(scope, template) }
            newLatexFile("letter.tex") { renderLetterTemplate(scope, template) }
            render(scope, template.attachments) { attachmentScope, id, attachment ->
                newLatexFile("attachment_${id}.tex") { renderAttachment(attachmentScope, attachment) }
            }
            addFiles(letterResourceFiles)
        }

    private fun LatexAppendable.appendMasterTemplateParameters(
        scope: ExpressionScope<*, *>,
        attachments: List<IncludeAttachment<*, *>>,
        brevtype: LetterMetadata.Brevtype,
    ) {
        pensjonLatexSettings.writeLanguageSettings { settingName, settingValue ->
            appendNewCmd("felt$settingName") {
                renderText(scope, settingValue)
            }
        }

        appendln("\\def\\pdfcreationdate{\\string ${pdfCreationTime()}}", escape = false)
        appendNewCmd("feltsaksnummer", scope.felles.saksnummer)

        vedleggCommand(scope, attachments)

        with(scope.felles) {
            brukerCommands(bruker)
            saksinfoCommands(vergeNavn)
            navEnhetCommands(avsenderEnhet)
            appendNewCmd("feltdato", dokumentDato.format(dateFormatter(scope.language, FormatStyle.LONG)))
            signaturCommands(signerendeSaksbehandlere, brevtype)
        }
    }

    private fun LatexAppendable.appendXmpData(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>) {
        appenCmd("Title") { arg { renderText(scope, template.title) } }
        appenCmd("Language", scope.language.locale().toLanguageTag())
        appenCmd("Publisher", scope.felles.avsenderEnhet.navn)
        appenCmd("Date", scope.felles.dokumentDato.format(DateTimeFormatter.ISO_LOCAL_DATE))
        appenCmd("Producer", DOCUMENT_PRODUCER)
        appenCmd("Creator", DOCUMENT_PRODUCER)
    }

    private fun LatexAppendable.renderLetterTemplate(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>) {
        appendln("""\documentclass{pensjonsbrev_v3}""", escape = false)
        appenCmd("begin", "document")
        appenCmd("firstpage")
        appenCmd("tittel") { arg { renderText(scope, template.title) } }
        renderOutline(scope, template.outline)
        appenCmd("closing")
        render(scope, template.attachments) { _, id, _ ->
            appenCmd("input", "attachment_$id", escape = false)
        }
        appenCmd("end", "document")
    }

    private fun LatexAppendable.signaturCommands(saksbehandlere: SignerendeSaksbehandlere?, brevtype: LetterMetadata.Brevtype) {
        appendNewCmd("closingbehandlet") {
            if (saksbehandlere != null) {
                saksbehandlere.attesterendeSaksbehandler?.takeIf { brevtype == LetterMetadata.Brevtype.VEDTAKSBREV }
                    ?.let { attestant ->
                        appenCmd("doublesignature") {
                            arg { append(attestant) }
                            arg { append(saksbehandlere.saksbehandler) }
                        }
                    } ?: append("""${saksbehandlere.saksbehandler} \\ \feltclosingsaksbehandlersuffix""",escape = false)

                appenCmd("par")
                appenCmd("vspace*{12pt}")
                appenCmd("feltnavenhet")
            } else {
                appenCmd("feltnavenhet")
                appenCmd("par")
                appenCmd("vspace*{12pt}")
                if (brevtype == LetterMetadata.Brevtype.VEDTAKSBREV) {
                    appenCmd("feltclosingautomatisktextvedtaksbrev")
                } else {
                    appenCmd("feltclosingautomatisktextinfobrev")
                }
            }
        }
    }

    private fun LatexAppendable.brukerCommands(bruker: Bruker) =
        with(bruker) {
            appendNewCmd("feltfoedselsnummerbruker", foedselsnummer.format())
            appendNewCmd("feltnavnbruker", fulltNavn())
        }

    private fun LatexAppendable.saksinfoCommands(verge: String?) {
        verge?.also { appendNewCmd("feltvergenavn", it) }
        appendNewCmd("saksinfomottaker") {
            appenCmd("begin", "saksinfotable", "")
            verge?.let {
                appendln("""\feltvergenavnprefix & \feltvergenavn \\""", escape = false)
                appendln(
                    """\felt${LanguageSetting.Sakspart.gjelderNavn} & \feltnavnbruker \\""",
                    escape = false
                )
            } ?: appendln(
                """\felt${LanguageSetting.Sakspart.navn} & \feltnavnbruker \\""",
                escape = false
            )
            appendln(
                """\felt${LanguageSetting.Sakspart.foedselsnummer} & \feltfoedselsnummerbruker \\""",
                escape = false
            )
            appendln(
                """\felt${LanguageSetting.Sakspart.saksnummer} & \feltsaksnummer \hfill \letterdate\\""",
                escape = false
            )

            appenCmd("end", "saksinfotable")
        }
    }

    private fun LatexAppendable.navEnhetCommands(navEnhet: NAVEnhet) =
        with(navEnhet) {
            appendNewCmd("feltnavenhet", navn)
            appendNewCmd("feltnavenhettlf", telefonnummer.format())
            appendNewCmd("feltnavenhetnettside", nettside)
        }

    private fun pdfCreationTime(): String {
        val now = ZonedDateTime.now()
        val formattedTime = now.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssxxx"))
        return "D:${formattedTime.replace(":", "'")}'"
    }

    private fun LatexAppendable.vedleggCommand(scope: ExpressionScope<*, *>, attachments: List<IncludeAttachment<*, *>>) {
        appendNewCmd("feltclosingvedlegg") {
            val includedAttachments = attachments.filter { it.predicate.eval(scope) }
            if (includedAttachments.isNotEmpty()) {
                appenCmd("begin", "attachmentList")

                render(scope, attachments) { attachmentScope, _, attachment ->
                    append("""\item """, escape = false)
                    renderText(attachmentScope, listOf(attachment.title))
                }
                appenCmd("end", "attachmentList")
            }
        }
    }

    private fun LatexAppendable.renderAttachment(scope: ExpressionScope<*, *>, attachment: AttachmentTemplate<*, *>) {
        appenCmd("startvedlegg") {
            arg { renderText(scope, listOf(attachment.title)) }
            arg { if (attachment.includeSakspart) appenCmd("sakspart") }
        }
        renderOutline(scope, attachment.outline)
        appenCmd("sluttvedlegg")
    }

    //
    // Element rendering
    //
    private fun LatexAppendable.renderOutline(scope: ExpressionScope<*, *>, elements: List<OutlineElement<*>>): Unit =
        render(scope, elements) { outlineScope, element -> renderOutlineContent(outlineScope, element) }

    private fun LatexAppendable.renderText(scope: ExpressionScope<*, *>, elements: List<TextElement<*>>): Unit =
        render(scope, elements) { inner, text -> renderTextContent(inner, text) }

    private fun LatexAppendable.renderOutlineContent(scope: ExpressionScope<*, *>, element: Element.OutlineContent<*>): Unit =
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraph(scope, element)
            is Element.OutlineContent.Title1 -> appenCmd("lettersectiontitle") {
                arg { renderText(scope, element.text) }
            }
        }

    private fun LatexAppendable.renderParagraph(
        scope: ExpressionScope<*, *>,
        element: Element.OutlineContent.Paragraph<*>
    ): Unit =
        appenCmd("templateparagraph") {
            arg {
                render(scope, element.paragraph) { pScope, element ->
                    renderParagraphContent(pScope, element)
                }
            }
        }

    private fun LatexAppendable.renderParagraphContent(scope: ExpressionScope<*, *>, element: ParagraphContent<*>): Unit =
        when (element) {
            is Form -> renderForm(scope, element)
            is Text -> renderTextContent(scope, element)
            is ItemList -> renderList(scope, element)
            is Table -> renderTable(scope, element)
        }

    private fun LatexAppendable.renderList(
        scope: ExpressionScope<*, *>,
        list: ItemList<*>
    ) {
        if (hasAnyContent(scope, list.items)) {
            appenCmd("begin", "itemize")
            render(scope, list.items) { itemScope, item ->
                append("""\item """, escape = false)
                renderText(itemScope, item.text)
            }
            appenCmd("end", "itemize")
        }
    }

    private fun LatexAppendable.renderTable(scope: ExpressionScope<*, *>, table: Table<*>) {
        if (hasAnyContent(scope, table.rows)) {
            val columnSpec = table.header.colSpec

            appenCmd("begin", "letterTable", columnHeadersLatexString(columnSpec))

            renderTableCells(scope, columnSpec.map { it.headerContent }, columnSpec)
            render(scope, table.rows) { rowScope, row ->
                renderTableCells(rowScope, row.cells, columnSpec)
            }

            appenCmd("end", "letterTable")
        }
    }

    private fun LatexAppendable.renderTableCells(
        scope: ExpressionScope<*, *>,
        cells: List<Table.Cell<LanguageSupport>>,
        colSpec: List<Table.ColumnSpec<LanguageSupport>>
    ) {
        cells.forEachIndexed { index, cell ->
            val columnSpan = colSpec[index].columnSpan
            if (columnSpan > 1) {
                append("\\SetCell[c=$columnSpan]{}", escape = false)
            }
            renderText(scope, cell.text)
            if (columnSpan > 1) {
                append(" ${"& ".repeat(columnSpan - 1)}", escape = false)
            }
            if (index < cells.lastIndex) {
                append("&", escape = false)
            }
        }
        append("""\\""", escape = false)
    }

    private fun columnHeadersLatexString(columnSpec: List<Table.ColumnSpec<LanguageSupport>>): String =
        columnSpec.joinToString("") {
            ("X" +
                    when (it.alignment) {
                        ColumnAlignment.LEFT -> "[l]"
                        ColumnAlignment.RIGHT -> "[r]"
                    }).repeat(it.columnSpan)
        }

    private fun LatexAppendable.renderTextContent(
        scope: ExpressionScope<*, *>,
        element: Text<*>
    ): Unit =
        when (element) {
            is Text.Expression.ByLanguage -> renderTextLiteral(element.expr(scope.language).eval(scope), element.fontType)
            is Text.Expression -> renderTextLiteral(element.expression.eval(scope), element.fontType)
            is Text.Literal -> renderTextLiteral(element.text(scope.language), element.fontType)
            is Text.NewLine -> appenCmd("newline")
        }

    private fun LatexAppendable.renderTextLiteral(
        textLiteral: String,
        fontType: FontType
    ): Unit =
        when (fontType) {
            FontType.PLAIN -> append(textLiteral)
            FontType.BOLD -> appenCmd("textbf") { arg { append(textLiteral) } }
            FontType.ITALIC -> appenCmd("textit") { arg { append(textLiteral) } }
        }

    private fun LatexAppendable.renderForm(scope: ExpressionScope<*, *>, element: Form<*>): Unit =
        when (element) {
            is Form.MultipleChoice -> {
                if (element.vspace) {
                    appenCmd("formvspace")
                }

                appenCmd("begin") {
                    arg { append("formChoice") }
                    arg { renderText(scope, listOf(element.prompt)) }
                }

                element.choices.forEach {
                    appenCmd("item")
                    renderTextContent(scope, it)
                }

                appenCmd("end", "formChoice")
            }

            is Form.Text -> {
                if (element.vspace) {
                    appenCmd("formvspace")
                }

                appenCmd("formText") {
                    arg {
                        val size = when (element.size) {
                            Size.NONE -> 0
                            Size.SHORT -> 25
                            Size.LONG -> 60
                        }
                        renderText(scope, listOf(element.prompt))
                        append(" ${".".repeat(size)}")
                    }
                }
            }
        }
}