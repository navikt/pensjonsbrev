package no.nav.brev.brevbaker.template.render

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.PDFTittel
import no.nav.brev.brevbaker.markup.dsl.ExtendedContentBuilder
import no.nav.brev.brevbaker.markup.dsl.OutlineBuilder
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
import no.nav.brev.brevbaker.markup.dsl.table
import no.nav.brev.brevbaker.markup.dsl.title1
import no.nav.brev.brevbaker.markup.dsl.title2
import no.nav.brev.brevbaker.markup.dsl.title3
import no.nav.brev.brevbaker.markup.dsl.title4
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.StableHash.Companion.with
import no.nav.pensjon.brev.template.render.LanguageSetting
import no.nav.pensjon.brev.template.render.documentLanguageSettings
import no.nav.pensjon.brev.template.render.fulltNavn
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import java.util.*

data class LetterWithAttachmentsMarkupV2(val letterMarkup: LetterMarkup, val attachments: List<Attachment>)

/**
 * Renderer som bygger [LetterMarkup] (fra `brevbaker/markup`) via modulens utvidede DSL i funksjonell
 * stil. Modulen genererer aldri id-er; renderen regner ut hver id (via [RenderContext]/[StableHash])
 * og sender den eksplisitt inn i hvert DSL-kall.
 */
@OptIn(InterneDataklasser::class)
internal object Letter2MarkupV2 : LetterRenderer<LetterWithAttachmentsMarkupV2>() {
    private val languageSettings = documentLanguageSettings

    /**
     * Lokal mellomrepresentasjon for ett enkelt tekst-uttrykk i core. Trengs fordi produksjonskoden
     * verken kan konstruere modulens [Text]-typer direkte (interne konstruktører) eller lese tilbake
     * tekst som allerede er lagt inn i en append-only DSL-bygger. [RenderedText] brukes derfor kun
     * internt i [textContentToRenderedText]/[mergeLiterals] for å slå sammen nabo-literaler *innenfor
     * ett uttrykk*, før [emitText] skriver resultatet rett inn i DSL-en. Den tres ikke lenger gjennom
     * outline/paragraf/tabell osv.
     */
    private sealed interface RenderedText {
        val id: Int

        data class Literal(override val id: Int, val text: String, val fontType: FontType, val tags: Set<ElementTags>) : RenderedText
        data class Variable(override val id: Int, val text: String, val fontType: FontType) : RenderedText
        data class NewLine(override val id: Int) : RenderedText
    }

    override fun renderLetter(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): LetterWithAttachmentsMarkupV2 =
        LetterWithAttachmentsMarkupV2(
            letterMarkup = renderLetterOnly(scope, template),
            attachments = renderAttachmentsOnly(RenderContext(scope), template),
        )

    fun renderLetterOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): LetterMarkup =
        letterMarkupExtended(buildLetter(scope, template))

    /**
     * Byggeblokken for hovedbrevet, delt slik at både [renderLetterOnly] (via [letterMarkupExtended])
     * og data-usage-varianten (via [letterMarkupWithDataUsageExtended]) bruker samme innhold.
     */
    internal fun buildLetter(
        scope: ExpressionScope<*>,
        template: LetterTemplate<*, *>,
    ): LetterMarkupBuilder<ExtendedContentBuilder>.() -> Unit {
        val context = RenderContext(scope)
        return {
            title1 { emitTexts(context, template.title) }
            saksinformasjon(
                gjelderNavn = context.scope.felles.bruker.fulltNavn(),
                gjelderFoedselsnummer = context.scope.felles.bruker.foedselsnummer.value,
                saksnummer = context.scope.felles.saksnummer,
                dokumentDato = context.scope.felles.dokumentDato,
                annenMottakerNavn = context.scope.felles.annenMottakerNavn,
            )
            outline { renderOutline(context, template.outline) }
            val sign = context.scope.felles.signerendeSaksbehandlere
            signatur(
                hilsenTekst = languageSettings.getSetting(context.scope.language, LanguageSetting.Closing.greeting),
                navAvsenderEnhet = context.scope.felles.avsenderEnhet.navn,
                saksbehandlerNavn = sign?.saksbehandler,
                attesterendeSaksbehandlerNavn = sign?.attesterendeSaksbehandler,
            )
        }
    }

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
            title1 { emitTexts(attachmentContext, attachment.title) }
            outline { renderOutline(attachmentContext, attachment.outline) }
        }

    fun renderPDFTitlesOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): List<PDFTittel> {
        val context = RenderContext(scope)
        return template.pdfAttachments.map { pdfTittelExtended { emitTexts(context, it.template.title) } }
    }

    private fun renderTitleTexts(context: RenderContext, elements: List<TextElement<*>>): List<Text> =
        pdfTittelExtended { emitTexts(context, elements) }.title1

    private fun OutlineBuilder<ExtendedContentBuilder>.renderOutline(context: RenderContext, outline: List<OutlineElement<*>>) {
        render(context, outline) { outlineContext, element -> renderOutlineContent(outlineContext, element) }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderOutlineContent(context: RenderContext, element: Element.OutlineContent<*>) {
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraph(context, element)
            is Element.OutlineContent.Title1 -> title2(context.stableHash(element)) { emitTexts(context, element.text) }
            is Element.OutlineContent.Title2 -> title3(context.stableHash(element)) { emitTexts(context, element.text) }
            is Element.OutlineContent.Title3 -> title4(context.stableHash(element)) { emitTexts(context, element.text) }
        }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderParagraph(context: RenderContext, paragraph: Element.OutlineContent.Paragraph<*>) {
        val currentTexts = mutableListOf<Pair<RenderContext, Element.OutlineContent.ParagraphContent.Text<*>>>()
        val currentSources = mutableListOf<StableHash>()

        fun flushParagraph() {
            if (currentTexts.isNotEmpty()) {
                val fragmentHash = StableHash.of(currentSources.toList()).with(paragraph.stableHashModifier)
                val texts = currentTexts.toList()
                paragraph(context.stableHash(fragmentHash)) { texts.forEach { (textContext, text) -> emitText(textContext, text) } }
                currentTexts.clear()
                currentSources.clear()
            }
        }

        render(context, paragraph.paragraph) { paragraphContext, element ->
            when (element) {
                is Element.OutlineContent.ParagraphContent.Text -> {
                    currentTexts.add(paragraphContext to element)
                    currentSources.add(element)
                }
                is Element.OutlineContent.ParagraphContent.ItemList -> {
                    flushParagraph()
                    renderItemList(paragraphContext, element)
                }
                is Element.OutlineContent.ParagraphContent.Table -> {
                    flushParagraph()
                    renderTable(paragraphContext, element)
                }
                is Element.OutlineContent.ParagraphContent.Form.Text -> {
                    flushParagraph()
                    renderFormText(paragraphContext, element)
                }
                is Element.OutlineContent.ParagraphContent.Form.MultipleChoice -> {
                    flushParagraph()
                    renderFormChoice(paragraphContext, element)
                }
            }
        }
        flushParagraph()
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderItemList(context: RenderContext, element: Element.OutlineContent.ParagraphContent.ItemList<*>) {
        val items = buildList {
            render(context, element.items) { itemContext, item -> add(itemContext to item) }
        }
        if (items.isEmpty()) return
        when (element.type) {
            Listetype.PUNKTLISTE -> itemList(context.stableHash(element)) {
                items.forEach { (itemContext, listItem) -> item(itemContext.stableHash(listItem)) { emitTexts(itemContext, listItem.text) } }
            }
            Listetype.NUMMERERT_LISTE -> numberedList(context.stableHash(element)) {
                items.forEach { (itemContext, listItem) -> item(itemContext.stableHash(listItem)) { emitTexts(itemContext, listItem.text) } }
            }
        }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderTable(context: RenderContext, element: Element.OutlineContent.ParagraphContent.Table<*>) {
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
                        alignment = mapAlignment(columnSpec.alignment),
                        span = columnSpec.columnSpan,
                    ) { emitTexts(context, columnSpec.headerContent.text) }
                }
            }
            rows.forEach { (rowContext, row) ->
                row(rowContext.stableHash(row)) {
                    row.cells.forEach { tableCell -> cell(rowContext.stableHash(tableCell)) { emitTexts(rowContext, tableCell.text) } }
                }
            }
        }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderFormText(context: RenderContext, element: Element.OutlineContent.ParagraphContent.Form.Text<*>) {
        formText(context.stableHash(element), mapFormSize(element.size), element.vspace) {
            emitTexts(context, listOf(element.prompt))
        }
    }

    private fun OutlineBuilder<ExtendedContentBuilder>.renderFormChoice(context: RenderContext, element: Element.OutlineContent.ParagraphContent.Form.MultipleChoice<*>) {
        formChoice(context.stableHash(element), element.vspace) {
            prompt { emitTexts(context, listOf(element.prompt)) }
            element.choices.forEach { choiceElement ->
                choice(context.stableHash(choiceElement)) { emitText(context, choiceElement) }
            }
        }
    }

    private fun ExtendedContentBuilder.emitTexts(context: RenderContext, elements: List<TextElement<*>>) {
        render(context, elements) { inner, text -> emitText(inner, text) }
    }

    private fun ExtendedContentBuilder.emitText(context: RenderContext, element: Element.OutlineContent.ParagraphContent.Text<*>) {
        textContentToRenderedText(context, element).forEach { rendered ->
            when (rendered) {
                is RenderedText.Literal -> text(rendered.id, rendered.text, rendered.fontType, rendered.tags)
                is RenderedText.Variable -> variable(rendered.id, rendered.text, rendered.fontType)
                is RenderedText.NewLine -> newLine(rendered.id)
            }
        }
    }

    private fun textContentToRenderedText(context: RenderContext, element: Element.OutlineContent.ParagraphContent.Text<*>): List<RenderedText> {
        val fontType = renderFontType(element.fontType)
        return when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> element.expr(context.scope.language).toRenderedText(context, fontType)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> element.expression.toRenderedText(context, fontType)
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(RenderedText.Literal(context.stableHash(element), element.text(context.scope.language), fontType, emptySet()))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> listOf(RenderedText.NewLine(context.stableHash(element)))
        }
    }

    private fun renderFontType(fontType: Element.OutlineContent.ParagraphContent.Text.FontType): FontType =
        when (fontType) {
            Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN -> FontType.PLAIN
            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD -> FontType.BOLD
            Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC -> FontType.ITALIC
        }

    @OptIn(BrevbakerDSLInternal::class)
    private fun StringExpression.toRenderedText(context: RenderContext, fontType: FontType): List<RenderedText> =
        when (this) {
            is Expression.Literal -> lagLiteral(context, fontType)
            is Expression.BinaryInvoke<*, *, *> if operation is BinaryOperation.Concat -> {
                // Since we know that operation is Concat, we also know that `first` and `second` are StringExpression.
                @Suppress("UNCHECKED_CAST")
                (first as StringExpression).toRenderedText(context, fontType) + (second as StringExpression).toRenderedText(context, fontType)
            }
            is Expression.BinaryInvoke<*, *, *> if operation is BinaryOperation.BrevdataEllerFritekst -> {
                val (erFritekst, text) = (operation as BinaryOperation.BrevdataEllerFritekst).getResultat(first, second, context.scope)
                if (erFritekst) {
                    lagLiteral(context, fontType, text, ElementTags.FRITEKST)
                } else {
                    lagVariabel(context, fontType, text)
                }
            }
            is Expression.UnaryInvoke<*, *> if operation is UnaryOperation.Fritekst -> lagLiteral(context, fontType, eval(context.scope), ElementTags.FRITEKST)
            is Expression.UnaryInvoke<*, *> if operation is UnaryOperation.RedigerbarData -> lagLiteral(context, fontType, eval(context.scope), ElementTags.REDIGERBAR_DATA)
            else -> lagVariabel(context, fontType)
        }.mergeLiterals(fontType)

    private fun Expression<String>.lagLiteral(context: RenderContext, fontType: FontType, text: String = eval(context.scope), tag: ElementTags? = null): List<RenderedText> =
        listOf(RenderedText.Literal(context.stableHash(this), text, fontType, tag?.let { setOf(it) } ?: emptySet()))

    private fun Expression<String>.lagVariabel(context: RenderContext, fontType: FontType, text: String = eval(context.scope)): List<RenderedText> =
        listOf(RenderedText.Variable(context.stableHash(this), text, fontType))

    private fun List<RenderedText>.mergeLiterals(fontType: FontType): List<RenderedText> =
        fold(emptyList()) { acc, current ->
            val previous = acc.lastOrNull()
            if (acc.isEmpty()) {
                listOf(current)
            } else if (previous is RenderedText.Literal && current is RenderedText.Literal && previous.tags.isEmpty() && current.tags.isEmpty()) {
                acc.subList(0, acc.size - 1) + RenderedText.Literal(Objects.hash(previous.id, current.id), previous.text + current.text, fontType, emptySet())
            } else {
                acc + current
            }
        }

    private fun mapFormSize(size: Element.OutlineContent.ParagraphContent.Form.Text.Size): Block.FormText.Size =
        when (size) {
            Element.OutlineContent.ParagraphContent.Form.Text.Size.NONE -> Block.FormText.Size.NONE
            Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT -> Block.FormText.Size.SHORT
            Element.OutlineContent.ParagraphContent.Form.Text.Size.LONG -> Block.FormText.Size.LONG
            Element.OutlineContent.ParagraphContent.Form.Text.Size.FILL -> Block.FormText.Size.FILL
        }

    private fun mapAlignment(alignment: Element.OutlineContent.ParagraphContent.Table.ColumnAlignment): Block.Table.ColumnAlignment =
        when (alignment) {
            Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT -> Block.Table.ColumnAlignment.LEFT
            Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT -> Block.Table.ColumnAlignment.RIGHT
        }
}
