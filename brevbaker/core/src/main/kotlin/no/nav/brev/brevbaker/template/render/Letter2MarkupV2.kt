package no.nav.brev.brevbaker.template.render

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.PDFTittel
import no.nav.brev.brevbaker.markup.dsl.ExtendedContentBuilder
import no.nav.brev.brevbaker.markup.dsl.OutlineBuilder
import no.nav.brev.brevbaker.markup.dsl.PlainExtendedTextBuilder
import no.nav.brev.brevbaker.markup.dsl.attachmentExtended
import no.nav.brev.brevbaker.markup.dsl.cell
import no.nav.brev.brevbaker.markup.dsl.choice
import no.nav.brev.brevbaker.markup.dsl.column
import no.nav.brev.brevbaker.markup.dsl.formChoice
import no.nav.brev.brevbaker.markup.dsl.formText
import no.nav.brev.brevbaker.markup.dsl.header
import no.nav.brev.brevbaker.markup.dsl.item
import no.nav.brev.brevbaker.markup.dsl.itemList
import no.nav.brev.brevbaker.markup.dsl.LetterMarkupBuilder
import no.nav.brev.brevbaker.markup.dsl.letterMarkupExtended
import no.nav.brev.brevbaker.markup.dsl.numberedList
import no.nav.brev.brevbaker.markup.dsl.paragraph
import no.nav.brev.brevbaker.markup.dsl.pdfTittelExtended
import no.nav.brev.brevbaker.markup.dsl.prompt
import no.nav.brev.brevbaker.markup.dsl.row
import no.nav.brev.brevbaker.markup.dsl.saksinformasjon
import no.nav.brev.brevbaker.markup.dsl.signatur
import no.nav.brev.brevbaker.markup.dsl.table
import no.nav.brev.brevbaker.markup.dsl.title1
import no.nav.brev.brevbaker.markup.dsl.title2
import no.nav.brev.brevbaker.markup.dsl.title3
import no.nav.brev.brevbaker.markup.dsl.title4
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.template.render.text.appendText
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent
import no.nav.pensjon.brev.template.StableHash.Companion.with
import no.nav.pensjon.brev.template.render.LanguageSetting
import no.nav.pensjon.brev.template.render.documentLanguageSettings
import no.nav.pensjon.brev.template.render.fulltNavn
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

data class LetterWithAttachmentsMarkupV2(val letterMarkup: LetterMarkup, val attachments: List<Attachment>)

@OptIn(InterneDataklasser::class)
internal object Letter2MarkupV2 : LetterRenderer<LetterWithAttachmentsMarkupV2>() {
    private val languageSettings = documentLanguageSettings

    override fun renderLetter(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): LetterWithAttachmentsMarkupV2 =
        LetterWithAttachmentsMarkupV2(
            letterMarkup = renderLetterOnly(scope, template),
            attachments = renderAttachmentsOnly(RenderContext(scope), template),
        )

    fun renderLetterOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): LetterMarkup =
        letterMarkupExtended(
            saksinformasjon = buildSaksinformasjon(scope),
            signatur = buildSignatur(scope),
            build = buildLetter(scope, template),
        )

    internal fun buildLetter(
        scope: ExpressionScope<*>,
        template: LetterTemplate<*, *>,
    ): LetterMarkupBuilder<ExtendedContentBuilder>.() -> Unit {
        val context = RenderContext(scope)
        return {
            title1 { appendTexts(context, template.title) }
            outline { renderOutline(context, template.outline) }
        }
    }

    internal fun buildSaksinformasjon(scope: ExpressionScope<*>) =
        saksinformasjon(
            gjelderNavn = scope.felles.bruker.fulltNavn(),
            gjelderPersonidentifikator = scope.felles.bruker.foedselsnummer.value,
            saksnummer = scope.felles.saksnummer,
            dokumentDato = scope.felles.dokumentDato,
            annenMottakerNavn = scope.felles.annenMottakerNavn,
        )

    internal fun buildSignatur(scope: ExpressionScope<*>) =
        signatur(
            hilsenTekst = languageSettings.getSetting(scope.language, LanguageSetting.Closing.greeting),
            navAvsenderEnhet = scope.felles.avsenderEnhet.navn,
            saksbehandlerNavn = scope.felles.signerendeSaksbehandlere?.saksbehandler,
            attesterendeSaksbehandlerNavn = scope.felles.signerendeSaksbehandlere?.attesterendeSaksbehandler,
        )

    fun renderAttachmentsOnly(
        scope: ExpressionScope<*>,
        template: LetterTemplate<*, *>,
        redigerteVedlegg: Map<VedleggId, Attachment> = emptyMap(),
    ): List<Attachment> =
        renderAttachmentsOnly(RenderContext(scope), template, redigerteVedlegg)

    private fun renderAttachmentsOnly(
        renderContext: RenderContext,
        template: LetterTemplate<*, *>,
        redigerteVedlegg: Map<VedleggId, Attachment> = emptyMap(),
    ): List<Attachment> = buildList {
        render(renderContext, template.attachments) { attachmentContext, editableId, attachment ->
            val override = editableId?.let { redigerteVedlegg[it] }
            add(override ?: renderAttachment(attachmentContext, attachment))
        }
    }

    fun renderEditableAttachmentTitles(
        scope: ExpressionScope<*>,
        template: LetterTemplate<*, *>,
    ): Map<VedleggId, List<Text>> = buildMap {
        render(RenderContext(scope), template.attachments) { attachmentContext, editableId, attachment ->
            if (editableId != null) {
                put(editableId, renderTitleTexts(attachmentContext, attachment.title))
            }
        }
    }

    fun renderEditableAttachment(
        scope: ExpressionScope<*>,
        template: LetterTemplate<*, *>,
        vedleggId: VedleggId,
    ): Attachment? {
        render(RenderContext(scope), template.attachments) { attachmentContext, editableId, attachment ->
            if (editableId == vedleggId) {
                return renderAttachment(attachmentContext, attachment)
            }
        }
        return null
    }

    private fun renderAttachment(attachmentContext: RenderContext, attachment: AttachmentTemplate<*, *>): Attachment =
        attachmentExtended(inkluderSaksinformasjon = attachment.includeSakspart) {
            title1 { appendTexts(attachmentContext, attachment.title) }
            outline { renderOutline(attachmentContext, attachment.outline) }
        }

    fun renderPDFTitlesOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): List<PDFTittel> {
        val context = RenderContext(scope)
        return template.pdfAttachments.map { pdfTittelExtended { appendTexts(context, it.template.title) } }
    }

    private fun renderTitleTexts(context: RenderContext, elements: List<TextElement<*>>): List<Text> =
        pdfTittelExtended { appendTexts(context, elements) }.title1

    private fun OutlineBuilder<ExtendedContentBuilder>.renderOutline(context: RenderContext, outline: List<OutlineElement<*>>) {
        render(context, outline) { outlineContext, element -> renderOutlineContent(outlineContext, element) }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderOutlineContent(context: RenderContext, element: Element.OutlineContent<*>) {
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraph(context, element)
            is Element.OutlineContent.Title1 -> title2(context.stableHash(element)) { appendTexts(context, element.text) }
            is Element.OutlineContent.Title2 -> title3(context.stableHash(element)) { appendTexts(context, element.text) }
            is Element.OutlineContent.Title3 -> title4(context.stableHash(element)) { appendTexts(context, element.text) }
        }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderParagraph(context: RenderContext, paragraph: Element.OutlineContent.Paragraph<*>) {
        val currentTexts = mutableListOf<Pair<RenderContext, ParagraphContent.Text<*>>>()
        val currentSources = mutableListOf<StableHash>()

        fun flushParagraph() {
            if (currentTexts.isNotEmpty()) {
                val fragmentHash = StableHash.of(currentSources.toList()).with(paragraph.stableHashModifier)
                val texts = currentTexts.toList()
                paragraph(context.stableHash(fragmentHash)) { texts.forEach { (textContext, text) -> appendText(textContext, text) } }
                currentTexts.clear()
                currentSources.clear()
            }
        }

        render(context, paragraph.paragraph) { paragraphContext, element ->
            when (element) {
                is ParagraphContent.Text -> {
                    currentTexts.add(paragraphContext to element)
                    currentSources.add(element)
                }
                is ParagraphContent.ItemList -> {
                    flushParagraph()
                    renderItemList(paragraphContext, element)
                }
                is ParagraphContent.Table -> {
                    flushParagraph()
                    renderTable(paragraphContext, element)
                }
                is ParagraphContent.Form.Text -> {
                    flushParagraph()
                    renderFormText(paragraphContext, element)
                }
                is ParagraphContent.Form.MultipleChoice -> {
                    flushParagraph()
                    renderFormChoice(paragraphContext, element)
                }
            }
        }
        flushParagraph()
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderItemList(context: RenderContext, element: ParagraphContent.ItemList<*>) {
        val items = buildList {
            render(context, element.items) { itemContext, item -> add(itemContext to item) }
        }
        if (items.isEmpty()) return
        when (element.type) {
            Listetype.PUNKTLISTE -> itemList(context.stableHash(element)) {
                items.forEach { (itemContext, listItem) -> item(itemContext.stableHash(listItem)) { appendTexts(itemContext, listItem.text) } }
            }
            Listetype.NUMMERERT_LISTE -> numberedList(context.stableHash(element)) {
                items.forEach { (itemContext, listItem) -> item(itemContext.stableHash(listItem)) { appendTexts(itemContext, listItem.text) } }
            }
        }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderTable(context: RenderContext, element: ParagraphContent.Table<*>) {
        val rows = buildList {
            render(context, element.rows) { rowContext, row -> add(rowContext to row) }
        }
        if (rows.isEmpty()) return
        table(context.stableHash(element)) {
            header(context.stableHash(element.header)) {
                element.header.colSpec.forEach { columnSpec ->
                    column(
                        id = context.stableHash(columnSpec),
                        headerContentId = context.stableHash(columnSpec.headerContent),
                        alignment = columnSpec.alignment.toMarkup(),
                        span = columnSpec.columnSpan,
                    ) { appendTexts(context, columnSpec.headerContent.text) }
                }
            }
            rows.forEach { (rowContext, row) ->
                row(rowContext.stableHash(row)) {
                    row.cells.forEach { tableCell -> cell(rowContext.stableHash(tableCell)) { appendTexts(rowContext, tableCell.text) } }
                }
            }
        }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderFormText(context: RenderContext, element: ParagraphContent.Form.Text<*>) {
        formText(context.stableHash(element), element.size.toMarkup(), element.vspace) {
            appendTexts(context, listOf(element.prompt))
        }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderFormChoice(context: RenderContext, element: ParagraphContent.Form.MultipleChoice<*>) {
        formChoice(context.stableHash(element), element.vspace) {
            prompt { appendTexts(context, listOf(element.prompt)) }
            element.choices.forEach { choiceElement ->
                choice(context.stableHash(choiceElement)) { appendText(context, choiceElement) }
            }
        }
    }

    private fun ExtendedContentBuilder.appendTexts(context: RenderContext, elements: List<TextElement<*>>) {
        render(context, elements) { inner, text -> appendText(inner, text) }
    }

    private fun PlainExtendedTextBuilder.appendTexts(context: RenderContext, elements: List<TextElement<*>>) {
        render(context, elements) { inner, text -> appendText(inner, text) }
    }

}
