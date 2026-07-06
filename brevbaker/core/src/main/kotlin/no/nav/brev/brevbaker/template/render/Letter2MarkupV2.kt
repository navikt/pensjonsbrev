package no.nav.brev.brevbaker.template.render

import no.nav.brev.InterneDataklasser
import no.nav.brev.Listetype
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.LanguageSetting
import no.nav.pensjon.brev.template.render.documentLanguageSettings
import no.nav.pensjon.brev.template.render.fulltNavn
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Saksnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text.FontType
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Text.Literal
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.TextImpl.NewLineImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl.TextImpl.VariableImpl
import no.nav.pensjon.brevbaker.api.model.PDFTittelV2
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

data class LetterWithAttachmentsMarkupV2(val letterMarkup: LetterMarkupV2, val attachments: List<LetterMarkupV2.Attachment>)

/**
 * Renders a LetterTemplate directly to LetterMarkupV2. This is an independent renderer from
 * Letter2Markup (v1) - it does not go through v1's LetterMarkup at any point. The only stable,
 * well-defined direction for converting between the two markup shapes is v1 -> v2 (v2 -> v1 is
 * ambiguous, since v1 nests tables/lists inside a paragraph while v2 has them as sibling blocks).
 * That v1 -> v2 conversion is a separate future concern (used by pdf-bygger integration), not this
 * renderer.
 */
@OptIn(InterneDataklasser::class)
internal object Letter2MarkupV2 : LetterRenderer<LetterWithAttachmentsMarkupV2>() {
    private val languageSettings = documentLanguageSettings

    override fun renderLetter(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): LetterWithAttachmentsMarkupV2 =
        LetterWithAttachmentsMarkupV2(
            letterMarkup = renderLetterOnly(RenderContext(scope), template),
            attachments = renderAttachmentsOnly(RenderContext(scope), template),
        )

    fun renderLetterOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): LetterMarkupV2 =
        renderLetterOnly(RenderContext(scope), template)

    private fun renderLetterOnly(context: RenderContext, template: LetterTemplate<*, *>): LetterMarkupV2 =
        LetterMarkupV2Impl(
            title1 = renderText(context, template.title),
            saksinformasjon = SaksinformasjonImpl(
                gjelderNavn = context.scope.felles.bruker.fulltNavn(),
                gjelderFoedselsnummer = context.scope.felles.bruker.foedselsnummer,
                annenMottakerNavn = context.scope.felles.annenMottakerNavn,
                saksnummer = Saksnummer(context.scope.felles.saksnummer),
                dokumentDato = context.scope.felles.dokumentDato,
            ),
            blocks = renderOutline(context, template.outline),
            signatur = context.scope.felles.signerendeSaksbehandlere.let { sign ->
                SignaturImpl(
                    hilsenTekst = languageSettings.getSetting(context.scope.language, LanguageSetting.Closing.greeting),
                    saksbehandlerSignatur = sign?.let { SaksbehandlerSignaturImpl(it.saksbehandler, it.attesterendeSaksbehandler) },
                    navAvsenderEnhet = context.scope.felles.avsenderEnhet.navn,
                )
            }
        )

    fun renderAttachmentsOnly(
        scope: ExpressionScope<*>,
        template: LetterTemplate<*, *>,
        redigerteVedlegg: Map<VedleggId, LetterMarkupV2.Attachment> = emptyMap(),
    ): List<LetterMarkupV2.Attachment> =
        renderAttachmentsOnly(RenderContext(scope), template, redigerteVedlegg)

    private fun renderAttachmentsOnly(
        renderContext: RenderContext,
        template: LetterTemplate<*, *>,
        redigerteVedlegg: Map<VedleggId, LetterMarkupV2.Attachment> = emptyMap(),
    ): List<LetterMarkupV2.Attachment> = buildList {
        render(renderContext, template.attachments) { attachmentContext, editableId, attachment ->
            val override = editableId?.let { redigerteVedlegg[it] }
            add(
                if (override != null) {
                    AttachmentImpl(override.title1, override.blocks, override.inkluderSaksinformasjon)
                } else {
                    renderAttachment(attachmentContext, attachment)
                }
            )
        }
    }

    fun renderEditableAttachmentTitles(
        scope: ExpressionScope<*>,
        template: LetterTemplate<*, *>,
    ): Map<VedleggId, List<Text>> = buildMap {
        render(RenderContext(scope), template.attachments) { attachmentContext, editableId, attachment ->
            if (editableId != null) {
                put(editableId, renderText(attachmentContext, attachment.title))
            }
        }
    }

    fun renderEditableAttachment(
        scope: ExpressionScope<*>,
        template: LetterTemplate<*, *>,
        vedleggId: VedleggId,
    ): LetterMarkupV2.Attachment? {
        render(RenderContext(scope), template.attachments) { attachmentContext, editableId, attachment ->
            if (editableId == vedleggId) {
                return renderAttachment(attachmentContext, attachment)
            }
        }
        return null
    }

    private fun renderAttachment(attachmentContext: RenderContext, attachment: AttachmentTemplate<*, *>): LetterMarkupV2.Attachment =
        AttachmentImpl(
            renderText(attachmentContext, attachment.title),
            renderOutline(attachmentContext, attachment.outline),
            attachment.includeSakspart,
        )

    fun renderPDFTitlesOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): List<PDFTittelV2> {
        val context = RenderContext(scope)
        return template.pdfAttachments.map { PDFTittelV2(renderText(context, it.template.title)) }
    }

    private fun renderOutline(context: RenderContext, outline: List<OutlineElement<*>>): List<Block> =
        buildList {
            render(context, outline) { outlineContext, element ->
                addAll(renderOutlineContent(outlineContext, element))
            }
        }

    private fun renderOutlineContent(context: RenderContext, element: Element.OutlineContent<*>): List<Block> =
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraphAsBlocks(context, element)
            // Title renumbering is a pure rename: outline title1/title2/title3 become Title2/Title3/Title4
            // in v2, since the letter's own title (template.title) now occupies the name "title1".
            is Element.OutlineContent.Title1 -> listOf(BlockImpl.Title2Impl(context.stableHash(element), renderText(context, element.text)))
            is Element.OutlineContent.Title2 -> listOf(BlockImpl.Title3Impl(context.stableHash(element), renderText(context, element.text)))
            is Element.OutlineContent.Title3 -> listOf(BlockImpl.Title4Impl(context.stableHash(element), renderText(context, element.text)))
        }

    /**
     * v1 nests tables/lists as ParagraphContent *inside* a Paragraph block. v2 makes them
     * top-level sibling blocks. To preserve reading order without ambiguity, a paragraph
     * containing a table/list mid-stream is split: any accumulated text becomes its own
     * Paragraph block, then the table/list becomes its own block, and text accumulation
     * continues in a fresh Paragraph block afterward.
     */
    private fun renderParagraphAsBlocks(context: RenderContext, paragraph: Element.OutlineContent.Paragraph<*>): List<Block> {
        val blocks = mutableListOf<Block>()
        var currentText = mutableListOf<Text>()
        var fragmentIndex = 0

        fun flushParagraph() {
            if (currentText.isNotEmpty()) {
                blocks.add(BlockImpl.ParagraphImpl(Objects.hash(context.stableHash(paragraph), fragmentIndex), currentText))
                fragmentIndex++
                currentText = mutableListOf()
            }
        }

        render(context, paragraph.paragraph) { paragraphContext, element ->
            when (element) {
                is Element.OutlineContent.ParagraphContent.Text -> currentText.addAll(renderTextContent(paragraphContext, element))
                is Element.OutlineContent.ParagraphContent.ItemList -> {
                    flushParagraph()
                    renderItemList(paragraphContext, element)?.let { blocks.add(it) }
                }
                is Element.OutlineContent.ParagraphContent.Table -> {
                    flushParagraph()
                    renderTable(paragraphContext, element)?.let { blocks.add(it) }
                }
                is Element.OutlineContent.ParagraphContent.Form.Text -> {
                    flushParagraph()
                    blocks.add(renderFormText(paragraphContext, element))
                }
                is Element.OutlineContent.ParagraphContent.Form.MultipleChoice -> {
                    flushParagraph()
                    blocks.add(renderFormChoice(paragraphContext, element))
                }
            }
        }
        flushParagraph()

        return blocks
    }

    private fun renderFormText(context: RenderContext, form: Element.OutlineContent.ParagraphContent.Form.Text<*>): FormText =
        BlockImpl.FormTextImpl(
            id = context.stableHash(form),
            prompt = renderText(context, listOf(form.prompt)),
            size = when (form.size) {
                Element.OutlineContent.ParagraphContent.Form.Text.Size.NONE -> FormText.Size.NONE
                Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT -> FormText.Size.SHORT
                Element.OutlineContent.ParagraphContent.Form.Text.Size.LONG -> FormText.Size.LONG
                Element.OutlineContent.ParagraphContent.Form.Text.Size.FILL -> FormText.Size.FILL
            },
            vspace = form.vspace,
        )

    private fun renderFormChoice(context: RenderContext, form: Element.OutlineContent.ParagraphContent.Form.MultipleChoice<*>): FormChoice =
        BlockImpl.FormChoiceImpl(
            id = context.stableHash(form),
            prompt = renderText(context, listOf(form.prompt)),
            choices = form.choices.map { choice ->
                BlockImpl.FormChoiceImpl.ChoiceImpl(context.stableHash(choice), renderTextContent(context, choice))
            },
            vspace = form.vspace,
        )

    private fun renderTable(context: RenderContext, table: Element.OutlineContent.ParagraphContent.Table<*>): Table? =
        renderRows(context, table.rows).takeIf { it.isNotEmpty() }?.let { rows ->
            BlockImpl.TableImpl(
                id = context.stableHash(table),
                rows = rows,
                header = renderHeader(context, table.header),
            )
        }

    private fun renderHeader(context: RenderContext, header: Element.OutlineContent.ParagraphContent.Table.Header<*>): Table.Header =
        BlockImpl.TableImpl.HeaderImpl(context.stableHash(header), header.colSpec.map { columnSpec ->
            BlockImpl.TableImpl.ColumnSpecImpl(
                id = context.stableHash(columnSpec),
                headerContent = renderCell(context, columnSpec.headerContent),
                alignment = when (columnSpec.alignment) {
                    Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT -> Table.ColumnAlignment.LEFT
                    Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT -> Table.ColumnAlignment.RIGHT
                },
                span = columnSpec.columnSpan,
            )
        })

    private fun renderRows(context: RenderContext, rows: List<TableRowElement<*>>): List<Table.Row> =
        buildList {
            render(context, rows) { rowContext, row ->
                add(BlockImpl.TableImpl.RowImpl(rowContext.stableHash(row), row.cells.map { renderCell(rowContext, it) }))
            }
        }

    private fun renderCell(context: RenderContext, cell: Element.OutlineContent.ParagraphContent.Table.Cell<*>): Table.Cell =
        BlockImpl.TableImpl.CellImpl(context.stableHash(cell), renderText(context, cell.text))

    private fun renderItemList(context: RenderContext, itemList: Element.OutlineContent.ParagraphContent.ItemList<*>): ListContent? =
        buildList {
            render(context, itemList.items) { itemContext, item ->
                add(BlockImpl.ItemImpl(itemContext.stableHash(item), renderText(itemContext, item.text)))
            }
        }.takeIf { it.isNotEmpty() }?.let { items ->
            when (itemList.type) {
                Listetype.PUNKTLISTE -> BlockImpl.ItemListImpl(context.stableHash(itemList), items)
                Listetype.NUMMERERT_LISTE -> BlockImpl.NumberedListImpl(context.stableHash(itemList), items)
            }
        }

    private fun renderTextContent(context: RenderContext, element: Element.OutlineContent.ParagraphContent.Text<*>): List<Text> {
        val fontType = renderFontType(element.fontType)
        return when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> element.expr(context.scope.language).toContent(context, fontType)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> element.expression.toContent(context, fontType)
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(LiteralImpl(context.stableHash(element), element.text(context.scope.language), fontType))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> listOf(NewLineImpl(context.stableHash(element)))
        }
    }

    private fun renderText(context: RenderContext, elements: List<TextElement<*>>): List<Text> =
        buildList {
            render(context, elements) { inner, text ->
                addAll(renderTextContent(inner, text))
            }
        }

    private fun renderFontType(fontType: Element.OutlineContent.ParagraphContent.Text.FontType): FontType =
        when (fontType) {
            Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN -> FontType.PLAIN
            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD -> FontType.BOLD
            Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC -> FontType.ITALIC
        }

    @OptIn(BrevbakerDSLInternal::class)
    private fun StringExpression.toContent(context: RenderContext, fontType: FontType): List<Text> =
        when (this) {
            is Expression.Literal -> lagLiteral(context, fontType)
            is Expression.BinaryInvoke<*, *, *> if operation is BinaryOperation.Concat -> {
                // Since we know that operation is Concat, we also know that `first` and `second` are StringExpression.
                @Suppress("UNCHECKED_CAST")
                (first as StringExpression).toContent(context, fontType) + (second as StringExpression).toContent(context, fontType)
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

    private fun Expression<String>.lagLiteral(context: RenderContext, fontType: FontType, text: String = eval(context.scope), tag: ElementTags? = null) =
        listOf(LiteralImpl(context.stableHash(this), text, fontType, tag?.let { setOf(it) } ?: emptySet()))

    private fun Expression<String>.lagVariabel(context: RenderContext, fontType: FontType, text: String = eval(context.scope)) =
        listOf(VariableImpl(context.stableHash(this), text, fontType))

    private fun List<Text>.mergeLiterals(fontType: FontType): List<Text> =
        fold(emptyList()) { acc, current ->
            val previous = acc.lastOrNull()
            if (acc.isEmpty()) {
                listOf(current)
            } else if (canMergeAsLiterals(previous, current)) {
                acc.subList(0, acc.size - 1) + LiteralImpl(Objects.hash(previous.id, current.id), previous.text + current.text, fontType)
            } else {
                acc + current
            }
        }

    @OptIn(ExperimentalContracts::class)
    private fun canMergeAsLiterals(first: Text?, second: Text): Boolean {
        contract { returns() implies (first != null) }
        return first is Literal && second is Literal && first.tags.isEmpty() && second.tags.isEmpty()
    }
}
