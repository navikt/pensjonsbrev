package no.nav.brev.brevbaker.template.render

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.LanguageSetting
import no.nav.pensjon.brev.template.render.fulltNavn
import no.nav.pensjon.brev.template.render.documentLanguageSettings
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class LetterWithAttachmentsMarkup(val letterMarkup: LetterMarkup, val attachments: List<Attachment>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LetterWithAttachmentsMarkup

        if (letterMarkup != other.letterMarkup) return false
        if (attachments != other.attachments) return false

        return true
    }

    override fun hashCode(): Int {
        var result = letterMarkup.hashCode()
        result = 31 * result + attachments.hashCode()
        return result
    }
}

@OptIn(InterneDataklasser::class)
internal object Letter2Markup : LetterRenderer<LetterWithAttachmentsMarkup>() {
    private val languageSettings = documentLanguageSettings

    override fun renderLetter(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): LetterWithAttachmentsMarkup =
        LetterWithAttachmentsMarkup(
            letterMarkup = renderLetterOnly(RenderContext(scope), template),
            attachments = renderAttachmentsOnly(RenderContext(scope), template)
        )

    fun renderLetterOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): LetterMarkup =
        renderLetterOnly(RenderContext(scope), template)

    private fun renderLetterOnly(context: RenderContext, template: LetterTemplate<*, *>): LetterMarkup =
        LetterMarkupImpl(
            title = renderText(context, template.title),
            sakspart = SakspartImpl(
                gjelderNavn = context.scope.felles.bruker.fulltNavn(),
                gjelderFoedselsnummer = context.scope.felles.bruker.foedselsnummer,
                annenMottakerNavn = context.scope.felles.annenMottakerNavn,
                saksnummer = context.scope.felles.saksnummer,
                dokumentDato = context.scope.felles.dokumentDato,
            ),
            blocks = renderOutline(context, template.outline),
            signatur = context.scope.felles.signerendeSaksbehandlere.let { sign ->
                SignaturImpl(
                    hilsenTekst = languageSettings.getSetting(context.scope.language, LanguageSetting.Closing.greeting),
                    saksbehandlerNavn = sign?.saksbehandler,
                    attesterendeSaksbehandlerNavn = sign?.attesterendeSaksbehandler,
                    navAvsenderEnhet = context.scope.felles.avsenderEnhet.navn,
                )
            }
        )

    fun renderAttachmentsOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): List<Attachment> =
        renderAttachmentsOnly(RenderContext(scope), template)

    private fun renderAttachmentsOnly(context: RenderContext, template: LetterTemplate<*, *>): List<Attachment> = buildList {
        render(context, template.attachments) { attachmentContext, _, attachment ->
            add(
                AttachmentImpl(
                    renderText(attachmentContext, attachment.title),
                    renderOutline(attachmentContext, attachment.outline),
                    attachment.includeSakspart,
                )
            )
        }
    }

    fun renderPDFTitlesOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): List<PDFTittel> {
        val context = RenderContext(scope)
        return template.pdfAttachments.map {
            renderText(context, it.template.title)
        }.map { PDFTittel(it) }
    }


    private fun renderOutline(context: RenderContext, outline: List<OutlineElement<*>>): List<Block> =
        buildList {
            render(context, outline) { outlineContext, element ->
                add(renderOutlineContent(outlineContext, element))
            }
        }

    private fun renderOutlineContent(context: RenderContext, element: Element.OutlineContent<*>): Block =
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraph(context, element)
            is Element.OutlineContent.Title1 -> BlockImpl.Title1Impl(context.stableHash(element), true, renderText(context, element.text))
            is Element.OutlineContent.Title2 -> BlockImpl.Title2Impl(context.stableHash(element), true, renderText(context, element.text))
            is Element.OutlineContent.Title3 -> BlockImpl.Title3Impl(context.stableHash(element), true, renderText(context, element.text))
        }

    private fun renderParagraph(context: RenderContext, paragraph: Element.OutlineContent.Paragraph<*>): Paragraph =
        BlockImpl.ParagraphImpl(context.stableHash(paragraph), true, buildList {
            render(context, paragraph.paragraph) { paragraphContext, element ->
                addAll(renderParagraphContent(paragraphContext, element))
            }
        })

    private fun renderParagraphContent(context: RenderContext, element: Element.OutlineContent.ParagraphContent<*>): List<ParagraphContent> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text -> renderTextContent(context, element)
            is Element.OutlineContent.ParagraphContent.ItemList -> listOfNotNull(renderItemList(context, element))
            is Element.OutlineContent.ParagraphContent.Table -> listOfNotNull(renderTable(context, element))
            is Element.OutlineContent.ParagraphContent.Form -> listOf(renderForm(context, element))
        }

    private fun renderForm(context: RenderContext, form: Element.OutlineContent.ParagraphContent.Form<*>): ParagraphContent.Form =
        when (form) {
            is Element.OutlineContent.ParagraphContent.Form.Text -> ParagraphContentImpl.Form.TextImpl(
                id = context.stableHash(form),
                prompt = renderText(context, listOf(form.prompt)),
                size = when (form.size) {
                    Element.OutlineContent.ParagraphContent.Form.Text.Size.NONE -> ParagraphContent.Form.Text.Size.NONE
                    Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT -> ParagraphContent.Form.Text.Size.SHORT
                    Element.OutlineContent.ParagraphContent.Form.Text.Size.LONG -> ParagraphContent.Form.Text.Size.LONG
                    Element.OutlineContent.ParagraphContent.Form.Text.Size.FILL -> ParagraphContent.Form.Text.Size.FILL
                },
                vspace = form.vspace,
            )

            is Element.OutlineContent.ParagraphContent.Form.MultipleChoice -> ParagraphContentImpl.Form.MultipleChoiceImpl(
                id = context.stableHash(form),
                prompt = renderText(context, listOf(form.prompt)),
                choices = form.choices.map { ParagraphContentImpl.Form.MultipleChoiceImpl.ChoiceImpl(context.stableHash(it), renderTextContent(context, it)) },
                vspace = form.vspace,
            )
        }

    private fun renderTable(context: RenderContext, table: Element.OutlineContent.ParagraphContent.Table<*>): ParagraphContent.Table? =
        renderRows(context, table.rows).takeIf { it.isNotEmpty() }?.let { rows ->
            ParagraphContentImpl.TableImpl(
                id = context.stableHash(table),
                rows = rows,
                header = renderHeader(context, table.header),
            )
        }

    private fun renderHeader(context: RenderContext, header: Element.OutlineContent.ParagraphContent.Table.Header<*>): ParagraphContent.Table.Header =
        ParagraphContentImpl.TableImpl.HeaderImpl(context.stableHash(header), header.colSpec.map { columnSpec ->
            ParagraphContentImpl.TableImpl.ColumnSpecImpl(
                id = context.stableHash(columnSpec),
                headerContent = renderCell(context, columnSpec.headerContent),
                alignment = when (columnSpec.alignment) {
                    Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT -> ParagraphContent.Table.ColumnAlignment.LEFT
                    Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT -> ParagraphContent.Table.ColumnAlignment.RIGHT
                },
                span = columnSpec.columnSpan,
            )
        })

    private fun renderRows(context: RenderContext, rows: List<TableRowElement<*>>): List<ParagraphContent.Table.Row> =
        buildList {
            render(context, rows) { rowContext, row ->
                add(ParagraphContentImpl.TableImpl.RowImpl(rowContext.stableHash(row), row.cells.map { renderCell(rowContext, it) }))
            }
        }

    private fun renderCell(context: RenderContext, cell: Element.OutlineContent.ParagraphContent.Table.Cell<*>): ParagraphContent.Table.Cell =
        ParagraphContentImpl.TableImpl.CellImpl(context.stableHash(cell), renderText(context, cell.text))

    private fun renderItemList(context: RenderContext, itemList: Element.OutlineContent.ParagraphContent.ItemList<*>): ParagraphContent.ItemList? =
        buildList {
            render(context, itemList.items) { itemContext, item ->
                add(ParagraphContentImpl.ItemListImpl.ItemImpl(itemContext.stableHash(item), renderText(itemContext, item.text)))
            }
        }.takeIf { it.isNotEmpty() }?.let { items ->
            ParagraphContentImpl.ItemListImpl(context.stableHash(itemList), items, listType = itemList.type)
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