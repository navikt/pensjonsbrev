package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.latex.LatexAppendable
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dateFormatter
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.FontType
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val DOCUMENT_PRODUCER = "brevbaker / pdf-bygger med LaTeX"

object LatexDocumentRenderer : DocumentRenderer<LatexDocument> {

    override fun render(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        language: Language,
        felles: Felles,
        brevtype: LetterMetadata.Brevtype,
    ): LatexDocument =
        LatexDocument().apply {
            newLatexFile("params.tex") {
                appendMasterTemplateParameters(attachments, brevtype, felles, language)
            }
            newLatexFile("letter.xmpdata") { appendXmpData(letter, language, felles) }
            newLatexFile("letter.tex") { renderLetterTemplate(letter, attachments) }
            attachments.forEachIndexed { id, attachment ->
                newLatexFile("attachment_${id}.tex") { renderAttachment(attachment) }
            }
        }

    private fun LatexAppendable.appendMasterTemplateParameters(
        attachments: List<LetterMarkup.Attachment>,
        brevtype: LetterMetadata.Brevtype,
        felles: Felles,
        language: Language,
    ) {
        pensjonLatexSettings.writeLanguageSettings(language) { settingName, settingValue ->
            appendNewCmd("felt$settingName") {
                renderTextLiteral(settingValue, FontType.PLAIN)
            }
        }

        appendln("\\def\\pdfcreationdate{\\string ${pdfCreationTime()}}", escape = false)
        appendNewCmd("feltsaksnummer", felles.saksnummer)

        vedleggCommand(attachments)

        with(felles) {
            brukerCommands(bruker)
            saksinfoCommands(vergeNavn)
            navEnhetCommands(avsenderEnhet)
            appendNewCmd("feltdato", dokumentDato.format(dateFormatter(language, FormatStyle.LONG)))
            signaturCommands(signerendeSaksbehandlere, brevtype)
            kopimottakereCommands(kopimottakere)
        }
    }

    private fun LatexAppendable.appendXmpData(letter: LetterMarkup, language: Language, felles: Felles) {
        appenCmd("Title", letter.title)
        appenCmd("Language", language.locale().toLanguageTag())
        appenCmd("Publisher", felles.avsenderEnhet.navn)
        appenCmd("Date", felles.dokumentDato.format(DateTimeFormatter.ISO_LOCAL_DATE))
        appenCmd("Producer", DOCUMENT_PRODUCER)
        appenCmd("Creator", DOCUMENT_PRODUCER)
    }

    private fun LatexAppendable.renderLetterTemplate(letter: LetterMarkup, attachments: List<LetterMarkup.Attachment>) {
        appendln("""\documentclass{pensjonsbrev_v3}""", escape = false)
        appenCmd("begin", "document")
        appenCmd("firstpage")
        appenCmd("tittel", letter.title)
        renderBlocks(letter.blocks)
        appenCmd("closing")
        attachments.indices.forEach { id ->
            appenCmd("input", "attachment_$id", escape = false)
        }
        appenCmd("end", "document")
    }

    private fun LatexAppendable.signaturCommands(
        saksbehandlere: SignerendeSaksbehandlere?,
        brevtype: LetterMetadata.Brevtype,
    ) {
        appendNewCmd("closingbehandlet") {
            if (saksbehandlere != null) {
                val attestant =
                    saksbehandlere.attesterendeSaksbehandler?.takeIf { brevtype == LetterMetadata.Brevtype.VEDTAKSBREV }
                if (attestant != null) {
                    appenCmd("doublesignature") {
                        arg { append(attestant) }
                        arg { append(saksbehandlere.saksbehandler) }
                    }
                } else {
                    append(saksbehandlere.saksbehandler)
                    appendln(""" \\ \feltclosingsaksbehandlersuffix """, escape = false)
                }
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

    private fun LatexAppendable.kopimottakereCommands(kopimottakere: List<String>?) {
        appendNewCmd("feltclosingkopimottakere") {
            if (!kopimottakere.isNullOrEmpty()) {
                appenCmd("begin", "kopimottakere")

                kopimottakere.forEach { kopimottaker ->
                    append("""\item """, escape = false)
                    append(kopimottaker)
                }

                appenCmd("end", "kopimottakere")
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

    private fun LatexAppendable.vedleggCommand(attachments: List<LetterMarkup.Attachment>) {
        appendNewCmd("feltclosingvedlegg") {
            if (attachments.isNotEmpty()) {
                appenCmd("begin", "attachmentList")

                attachments.forEach { attachment ->
                    append("""\item """, escape = false)
                    renderText(attachment.title)
                }
                appenCmd("end", "attachmentList")
            }
        }
    }

    private fun LatexAppendable.renderAttachment(attachment: LetterMarkup.Attachment) {
        appenCmd("startvedlegg") {
            arg { renderText(attachment.title) }
            arg { if (attachment.includeSakspart) appenCmd("sakspart") }
        }
        renderBlocks(attachment.blocks)
        appenCmd("sluttvedlegg")
    }

    //
    // Element rendering
    //
    private fun LatexAppendable.renderBlocks(blocks: List<LetterMarkup.Block>): Unit =
        blocks.forEach { renderBlock(it) }

    private fun LatexAppendable.renderText(elements: List<ParagraphContent.Text>): Unit =
        elements.forEach { renderTextContent(it) }

    private fun LatexAppendable.renderBlock(block: LetterMarkup.Block): Unit =
        when (block) {
            is LetterMarkup.Block.Paragraph -> renderParagraph(block)
            is LetterMarkup.Block.Title1 -> appenCmd("lettersectiontitleone") {
                arg { renderText(block.content) }
            }

            is LetterMarkup.Block.Title2 -> appenCmd("lettersectiontitletwo") {
                arg { renderText(block.content) }
            }
        }

    private fun LatexAppendable.renderParagraph(
        element: LetterMarkup.Block.Paragraph
    ): Unit =
        appenCmd("templateparagraph") {
            arg {
                element.content.forEach {
                    renderParagraphContent(it)
                }
            }
        }

    private fun LatexAppendable.renderParagraphContent(element: ParagraphContent): Unit =
        when (element) {
            is ParagraphContent.Form -> renderForm(element)
            is ParagraphContent.ItemList -> renderList(element)
            is ParagraphContent.Table -> renderTable(element)
            is ParagraphContent.Text -> renderTextContent(element)
        }

    private fun LatexAppendable.renderList(
        list: ParagraphContent.ItemList
    ) {
        if (list.items.isNotEmpty()) {
            appenCmd("begin", "itemize")
            list.items.forEach { item ->
                append("""\item """, escape = false)
                renderText(item.content)
            }
            appenCmd("end", "itemize")
        }
    }

    private fun LatexAppendable.renderTable(table: ParagraphContent.Table) {
        if (table.rows.isNotEmpty()) {
            val columnSpec = table.header.colSpec

            appenCmd("begin", "letterTable", columnHeadersLatexString(columnSpec))

            renderTableCells(columnSpec.map { it.headerContent }, columnSpec)

            table.rows.forEach {
                renderTableCells(it.cells, table.header.colSpec)
            }

            appenCmd("end", "letterTable")
        }
    }

    private fun LatexAppendable.renderTableCells(
        cells: List<ParagraphContent.Table.Cell>,
        colSpec: List<ParagraphContent.Table.ColumnSpec>
    ) {
        cells.forEachIndexed { index, cell ->
            val columnSpan = colSpec[index].span
            if (columnSpan > 1) {
                append("\\SetCell[c=$columnSpan]{}", escape = false)
            }
            renderText(cell.text)
            if (columnSpan > 1) {
                append(" ${"& ".repeat(columnSpan - 1)}", escape = false)
            }
            if (index < cells.lastIndex) {
                append("&", escape = false)
            }
        }
        append("""\\""", escape = false)
    }

    private fun columnHeadersLatexString(columnSpec: List<ParagraphContent.Table.ColumnSpec>): String =
        columnSpec.joinToString("") {
            ("X" +
                    when (it.alignment) {
                        ColumnAlignment.LEFT -> "[l]"
                        ColumnAlignment.RIGHT -> "[r]"
                    }).repeat(it.span)
        }

    private fun LatexAppendable.renderTextContent(element: ParagraphContent.Text): Unit =
        when (element) {
            is ParagraphContent.Text.Literal -> renderTextLiteral(element.text, element.fontType)
            is ParagraphContent.Text.Variable -> renderTextLiteral(element.text, element.fontType)
            is ParagraphContent.Text.NewLine -> appenCmd("newline")
        }

    private fun LatexAppendable.renderTextLiteral(text: String, fontType: FontType): Unit =
        when (fontType) {
            FontType.PLAIN -> append(text)
            FontType.BOLD -> appenCmd("textbf") { arg { append(text) } }
            FontType.ITALIC -> appenCmd("textit") { arg { append(text) } }
        }

    private fun LatexAppendable.renderForm(element: ParagraphContent.Form): Unit =
        when (element) {
            is ParagraphContent.Form.MultipleChoice -> {
                if (element.vspace) {
                    appenCmd("formvspace")
                }

                appenCmd("begin") {
                    arg { append("formChoice") }
                    arg { renderText(element.prompt) }
                }

                element.choices.forEach {
                    appenCmd("item")
                    renderText(it.text)
                }

                appenCmd("end", "formChoice")
            }

            is ParagraphContent.Form.Text -> {
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
                        renderText(element.prompt)
                        append(" ${".".repeat(size)}")
                    }
                }
            }
        }
}