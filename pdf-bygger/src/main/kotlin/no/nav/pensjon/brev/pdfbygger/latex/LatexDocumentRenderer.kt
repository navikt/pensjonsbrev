package no.nav.pensjon.brev.pdfbygger.latex

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dateFormatter
import no.nav.pensjon.brev.template.render.LanguageSetting
import no.nav.pensjon.brev.template.render.pensjonLatexSettings
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val DOCUMENT_PRODUCER = "brevbaker / pdf-bygger med LaTeX"

internal object LatexDocumentRenderer {

    internal fun render(pdfRequest: PDFRequest): LatexDocument = render(
        letter = pdfRequest.letterMarkup,
        attachments = pdfRequest.attachments,
        language = pdfRequest.language.toLanguage(),
        brevtype = pdfRequest.brevtype,
        pdfVedlegg = pdfRequest.pdfVedlegg,
    )

    @OptIn(InterneDataklasser::class)
    private fun List<PDFVedlegg>.asAttachment(): List<LetterMarkup.Attachment> = this.map {
        LetterMarkupImpl.AttachmentImpl(
            title = listOf(
                LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                    id = it.hashCode(),
                    text = it.type.tittel,
                )
            ),
            blocks = listOf(),
            includeSakspart = false
        )
    }

    private fun render(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        language: Language,
        brevtype: LetterMetadata.Brevtype,
        pdfVedlegg: List<PDFVedlegg>,
    ): LatexDocument =
        LatexDocument().apply {
            newLatexFile("params.tex") {
                appendMasterTemplateParameters(letter, attachments + pdfVedlegg.asAttachment(), brevtype, language)
            }
            newLatexFile("letter.xmpdata") { appendXmpData(letter, language) }
            newLatexFile("letter.tex") { renderLetterTemplate(letter, attachments) }
            attachments.forEachIndexed { id, attachment ->
                newLatexFile("attachment_${id}.tex") { renderAttachment(attachment) }
            }
        }

    private fun LatexAppendable.appendMasterTemplateParameters(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        brevtype: LetterMetadata.Brevtype,
        language: Language,
    ) {
        // TODO: Følgende tekster finnes også i LetterMarkup: LanguageSetting.Closing.greeting, LanguageSetting.Closing.saksbehandler.
        pensjonLatexSettings.writeLanguageSettings(language) { settingName, settingValue ->
            appendNewCmd("felt$settingName") {
                renderTextLiteral(settingValue, Text.FontType.PLAIN)
            }
        }

        appendln("\\def\\pdfcreationdate{\\string ${pdfCreationTime()}}", escape = false)

        vedleggCommand(attachments)
        sakspartCommands(letter.sakspart, language)
        signaturCommands(letter.signatur, brevtype)
    }

    private fun LatexAppendable.appendXmpData(letter: LetterMarkup, language: Language) {
        appendCmd("Title", letter.title)
        appendCmd("Language", language.locale().toLanguageTag())
        appendCmd("Publisher", letter.signatur.navAvsenderEnhet)
        appendCmd("Date", letter.sakspart.dokumentDato.format(DateTimeFormatter.ISO_LOCAL_DATE))
        appendCmd("Producer", DOCUMENT_PRODUCER)
        appendCmd("Creator", DOCUMENT_PRODUCER)
    }

    private fun LatexAppendable.renderLetterTemplate(letter: LetterMarkup, attachments: List<LetterMarkup.Attachment>) {
        appendln("""\documentclass{pensjonsbrev_v4}""", escape = false)
        appendCmd("begin", "document")
        appendCmd("firstpage")
        appendCmd("tittel", letter.title)
        renderBlocks(letter.blocks)
        appendCmd("closing")
        attachments.indices.forEach { id ->
            appendCmd("input", "attachment_$id", escape = false)
        }
        appendCmd("end", "document")
    }

    private fun LatexAppendable.signaturCommands(
        signatur: LetterMarkup.Signatur,
        brevtype: LetterMetadata.Brevtype,
    ) {
        appendNewCmd("feltnavenhet", signatur.navAvsenderEnhet)

        val saksbehandlerNavn = signatur.saksbehandlerNavn
            ?.also { appendNewCmd("feltsaksbehandlernavn", it) }

        val attestantNavn = signatur.attesterendeSaksbehandlerNavn
            ?.takeIf { brevtype == LetterMetadata.Brevtype.VEDTAKSBREV }
            ?.also { appendNewCmd("feltattestantnavn", it) }

        appendNewCmd("closingbehandlet") {
            when {
                saksbehandlerNavn != null && attestantNavn != null -> {
                    appendCmd("closingdoublesignature")
                }
                saksbehandlerNavn != null -> {
                    appendCmd("closingsinglesignature")
                }
                brevtype == LetterMetadata.Brevtype.VEDTAKSBREV -> {
                    appendCmd("closingautosignaturevedtaksbrev")
                }
                else -> {
                    appendCmd("closingautosignatureinfobrev")
                }
            }
        }
    }

    private fun LatexAppendable.sakspartCommands(sakspart: LetterMarkup.Sakspart, language: Language) {
        appendNewCmd("feltdato", sakspart.dokumentDato.format(dateFormatter(language, FormatStyle.LONG)))
        appendNewCmd("feltsaksnummer", sakspart.saksnummer)
        appendNewCmd("feltfoedselsnummerbruker", Foedselsnummer(sakspart.gjelderFoedselsnummer).format())
        appendNewCmd("feltnavnbruker", sakspart.gjelderNavn)
        val verge = sakspart.vergeNavn?.also { appendNewCmd("feltvergenavn", it) }

        appendNewCmd("saksinfomottaker") {
            appendCmd("begin", "saksinfotable", "")

            if (verge != null) {
                appendln("""\felt${LanguageSetting.Sakspart.vergenavn} & \feltvergenavn \\""", escape = false)
                appendln("""\felt${LanguageSetting.Sakspart.gjelderNavn} & \feltnavnbruker \\""", escape = false)
            } else {
                appendln("""\felt${LanguageSetting.Sakspart.navn} & \feltnavnbruker \\""", escape = false)
            }
            appendln(
                """\felt${LanguageSetting.Sakspart.foedselsnummer} & \feltfoedselsnummerbruker \\""",
                escape = false
            )
            appendln(
                """\felt${LanguageSetting.Sakspart.saksnummer} & \feltsaksnummer \hfill \letterdate\\""",
                escape = false
            )

            appendCmd("end", "saksinfotable")
        }
    }

    private fun pdfCreationTime(): String {
        val now = ZonedDateTime.now()
        val formattedTime = now.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssxxx"))
        return "D:${formattedTime.replace(":", "'")}'"
    }

    private fun LatexAppendable.vedleggCommand(attachments: List<LetterMarkup.Attachment>) {
        appendNewCmd("feltclosingvedlegg") {
            if (attachments.isNotEmpty()) {
                appendCmd("begin", "attachmentList")

                attachments.forEach { attachment ->
                    append("""\item """, escape = false)
                    renderText(attachment.title)
                }
                appendCmd("end", "attachmentList")
            }
        }
    }

    private fun LatexAppendable.renderAttachment(attachment: LetterMarkup.Attachment) {
        appendCmd("startvedlegg") {
            arg { renderText(attachment.title) }
            arg { if (attachment.includeSakspart) append("includesakinfo") }
        }
        renderBlocks(attachment.blocks)
        appendCmd("sluttvedlegg")
    }

    private fun LatexAppendable.renderIfNonEmptyText(content: List<Text>, render: LatexAppendable.(String) -> Unit) {
        val text = renderTextsToString(content)
        if (text.isNotEmpty()) {
            render(text)
        }
    }

    private fun LatexAppendable.renderBlocks(blocks: List<LetterMarkup.Block>) {
        blocks.forEachIndexed { index, block ->
            val previous = blocks.getOrNull(index - 1)
            val next = blocks.getOrNull(index + 1)
            renderBlock(block, previous, next)
        }
    }

    private fun LatexAppendable.renderText(elements: List<Text>): Unit =
        elements.forEach { renderTextContent(it) }

    private fun LatexAppendable.renderBlock(
        block: LetterMarkup.Block,
        previous: LetterMarkup.Block?,
        next: LetterMarkup.Block?
    ): Unit =
        when (block) {
            is LetterMarkup.Block.Paragraph -> renderParagraph(block, previous)

            is LetterMarkup.Block.Title1 -> renderIfNonEmptyText(block.content) { titleText ->
                if (!next.startsWithTable()) {
                    appendCmd("lettersectiontitleone", titleText)
                }
            }

            is LetterMarkup.Block.Title2 -> renderIfNonEmptyText(block.content) { titleText ->
                if (!next.startsWithTable()) {
                    appendCmd("lettersectiontitletwo", titleText)
                }
            }
        }

    private fun LetterMarkup.Block?.startsWithTable(): Boolean =
        (this is LetterMarkup.Block.Paragraph) && this.content.firstOrNull() is Table

    private fun LatexAppendable.renderTextParagraph(text: List<Text>): Unit =
        appendCmd("templateparagraph") {
            arg { renderText(text) }
        }

    //TODO depricate table/itemlist/form inside paragraph and make them available outside.
    // there should not be a different space between elements if within/outside paragraphs.
    private fun LatexAppendable.renderParagraph(
        element: LetterMarkup.Block.Paragraph,
        previous: LetterMarkup.Block?,
    ) {
        var continousTextContent = mutableListOf<Text>()

        element.content.forEachIndexed { index, current ->
            if (current !is Text && continousTextContent.isNotEmpty()) {
                renderTextParagraph(continousTextContent)
                continousTextContent = mutableListOf()
            }

            when (current) {
                is Form -> renderForm(current)
                is ItemList -> renderList(current)
                is Table -> renderTable(current, previous.takeIf {  index == 0})
                is Text -> continousTextContent.add(current)
            }
        }
        if (continousTextContent.isNotEmpty()) {
            renderTextParagraph(continousTextContent)
        }
    }

    private fun LatexAppendable.renderList(list: ItemList) {
        if (list.items.isNotEmpty()) {
            appendCmd("begin", "letteritemize")
            list.items.forEach { item ->
                append("""\item """, escape = false)
                renderText(item.content)
            }
            appendCmd("end", "letteritemize")
        }
    }

    private fun LatexAppendable.renderTable(table: Table, previous: LetterMarkup.Block?) {
        if (table.rows.isNotEmpty()) {
            val columnSpec = table.header.colSpec
            appendCmd(
                "begin", "letterTable", columnHeadersLatexString(columnSpec),
                titleTextOrNull(previous)?.let { "\\tabletitle $it" } ?: ""
                , escape = false
            )
            renderTableCells(columnSpec.map { it.headerContent }, columnSpec)

            table.rows.forEach {
                renderTableCells(it.cells, table.header.colSpec)
            }

            appendCmd("end", "letterTable")
        }
    }

    private fun titleTextOrNull(previous: LetterMarkup.Block?): String? =
        when (previous) {
            is LetterMarkup.Block.Title1 -> renderTextsToString(previous.content)
            is LetterMarkup.Block.Title2 -> renderTextsToString(previous.content)
            else -> null
        }?.takeIf { it.isNotBlank() }

    private fun renderTextsToString(texts: List<Text>): String =
        String(StringBuilder().also { LatexAppendable(it).renderText(texts) })

    private fun LatexAppendable.renderTableCells(cells: List<Table.Cell>, colSpec: List<Table.ColumnSpec>) {
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

    private fun columnHeadersLatexString(columnSpec: List<Table.ColumnSpec>): String =
        columnSpec.joinToString("") {
            ("X" +
                    when (it.alignment) {
                        Table.ColumnAlignment.LEFT -> "[l]"
                        Table.ColumnAlignment.RIGHT -> "[r]"
                    }).repeat(it.span)
        }

    private fun LatexAppendable.renderTextContent(element: Text): Unit =
        when (element) {
            is Text.Literal -> renderTextLiteral(element.text, element.fontType)
            is Text.Variable -> renderTextLiteral(element.text, element.fontType)
            is Text.NewLine -> appendCmd("newline")
        }

    private fun LatexAppendable.renderTextLiteral(text: String, fontType: Text.FontType): Unit =
        when (fontType) {
            Text.FontType.PLAIN -> append(text)
            Text.FontType.BOLD -> appendCmd("textbf") { arg { append(text) } }
            Text.FontType.ITALIC -> appendCmd("textit") { arg { append(text) } }
        }

    private fun LatexAppendable.renderForm(element: Form): Unit =
        when (element) {
            is Form.MultipleChoice -> {
                if (element.vspace) {
                    appendCmd("formvspace")
                }

                appendCmd("begin") {
                    arg { append("formChoice") }
                    arg { renderText(element.prompt) }
                }

                element.choices.forEach {
                    appendCmd("formchoiceitem")
                    renderText(it.text)
                }

                appendCmd("end", "formChoice")
            }

            is Form.Text -> {
                if (element.vspace) {
                    appendCmd("formvspace")
                }

                appendCmd("formText") {
                    arg {
                        val size = when (element.size) {
                            Form.Text.Size.NONE -> 0
                            Form.Text.Size.SHORT -> 25
                            Form.Text.Size.LONG -> 60
                        }
                        renderText(element.prompt)
                        append(" ${".".repeat(size)}")
                    }
                }
            }
        }
}