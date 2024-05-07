package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.*
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.ParagraphContent.Text
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.ParagraphContent.Text.*
import java.time.format.FormatStyle
import java.util.*

class LetterMarkdownRendererException(msg: String) : Exception(msg)

object LetterMarkdownRenderer : LetterRenderer<RenderedLetterMarkdown>() {
    private val languageSettings = pensjonHTMLSettings

    override fun renderLetter(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): RenderedLetterMarkdown =
        RenderedLetterMarkdown(
            title = renderText(scope, template.title).joinToString { it.text },
            sakspart = Sakspart(
                gjelderNavn = scope.felles.bruker.fulltNavn(),
                gjelderFoedselsnummer = scope.felles.bruker.foedselsnummer.value,
                saksnummer = scope.felles.saksnummer,
                dokumentDato = scope.felles.dokumentDato.format(dateFormatter(scope.language, FormatStyle.SHORT)),
            ),
            blocks = renderOutline(scope, template.outline),
            // TODO: Attesterende saksbehandler må kunne være null for informasjonsskriv som ikke attesteres
            signatur = scope.felles.signerendeSaksbehandlere.let { sign ->
                Signatur(
                    hilsenTekst = renderText(scope, languageSettings.settings[LanguageSetting.Closing.greeting]!!).joinToString { it.text },
                    saksbehandlerRolleTekst = renderText(scope, languageSettings.settings[LanguageSetting.Closing.saksbehandler]!!).joinToString { it.text },
                    saksbehandlerNavn = sign?.saksbehandler ?: "",
                    attesterendeSaksbehandlerNavn = sign?.attesterendeSaksbehandler ?: "",
                    navAvsenderEnhet = scope.felles.avsenderEnhet.navn,
                )
            }
        )

    private fun renderOutline(scope: ExpressionScope<*>, outline: List<OutlineElement<*>>): List<Block> =
        buildList {
            render(scope, outline) { outlineScope, element ->
                add(renderOutlineContent(outlineScope, element))
            }
        }

    private fun renderOutlineContent(scope: ExpressionScope<*>, element: Element.OutlineContent<*>): Block =
        when (element) {
            is Element.OutlineContent.Paragraph -> renderParagraph(scope, element)
            is Element.OutlineContent.Title1 -> Block.Title1(element.stableHashCode(), true, renderText(scope, element.text))
            is Element.OutlineContent.Title2 -> Block.Title2(element.stableHashCode(), true, renderText(scope, element.text))
        }

    private fun renderParagraph(scope: ExpressionScope<*>, paragraph: Element.OutlineContent.Paragraph<*>): Paragraph =
        Paragraph(paragraph.stableHashCode(), true, buildList {
            render(scope, paragraph.paragraph) { pScope, element ->
                addAll(renderParagraphContent(pScope, element))
            }
        })

    private fun renderParagraphContent(scope: ExpressionScope<*>, element: Element.OutlineContent.ParagraphContent<*>): List<ParagraphContent> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text -> renderTextContent(scope, element)
            is Element.OutlineContent.ParagraphContent.ItemList -> listOf(renderItemList(scope, element))
            is Element.OutlineContent.ParagraphContent.Table -> listOf(renderTable(scope, element))
            is Element.OutlineContent.ParagraphContent.Form -> listOf(renderForm(scope, element))
        }

    private fun renderForm(scope: ExpressionScope<*>, form: Element.OutlineContent.ParagraphContent.Form<*>): ParagraphContent.Form =
        when (form) {
            is Element.OutlineContent.ParagraphContent.Form.Text -> ParagraphContent.Form.Text(
                id = form.stableHashCode(),
                prompt = renderText(scope, listOf(form.prompt)),
                size = when (form.size) {
                    Element.OutlineContent.ParagraphContent.Form.Text.Size.NONE -> ParagraphContent.Form.Text.Size.NONE
                    Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT -> ParagraphContent.Form.Text.Size.SHORT
                    Element.OutlineContent.ParagraphContent.Form.Text.Size.LONG -> ParagraphContent.Form.Text.Size.LONG
                },
                vspace = form.vspace,
            )

            is Element.OutlineContent.ParagraphContent.Form.MultipleChoice -> ParagraphContent.Form.MultipleChoice(
                id = form.stableHashCode(),
                prompt = renderText(scope, listOf(form.prompt)),
                choices = form.choices.map { ParagraphContent.Form.MultipleChoice.Choice(it.stableHashCode(), renderTextContent(scope, it)) },
                vspace = form.vspace,
            )
        }

    private fun renderTable(scope: ExpressionScope<*>, table: Element.OutlineContent.ParagraphContent.Table<*>): ParagraphContent.Table =
        ParagraphContent.Table(
            id = table.stableHashCode(),
            rows = renderRows(scope, table.rows),
            header = renderHeader(scope, table.header),
        )

    private fun renderHeader(scope: ExpressionScope<*>, header: Element.OutlineContent.ParagraphContent.Table.Header<*>): ParagraphContent.Table.Header =
        ParagraphContent.Table.Header(header.stableHashCode(), header.colSpec.map { columnSpec ->
            ParagraphContent.Table.ColumnSpec(
                id = columnSpec.stableHashCode(),
                headerContent = renderCell(scope, columnSpec.headerContent),
                alignment = when (columnSpec.alignment) {
                    Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT -> ParagraphContent.Table.ColumnAlignment.LEFT
                    Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT -> ParagraphContent.Table.ColumnAlignment.RIGHT
                },
                span = columnSpec.columnSpan,
            )
        })

    private fun renderRows(scope: ExpressionScope<*>, rows: List<TableRowElement<*>>): List<ParagraphContent.Table.Row> =
        buildList {
            render(scope, rows) { rowScope, row ->
                add(ParagraphContent.Table.Row(row.stableHashCode(), row.cells.map { renderCell(rowScope, it) }))
            }
        }

    private fun renderCell(scope: ExpressionScope<*>, cell: Element.OutlineContent.ParagraphContent.Table.Cell<*>): ParagraphContent.Table.Cell =
        ParagraphContent.Table.Cell(cell.stableHashCode(), renderText(scope, cell.text))

    private fun renderItemList(scope: ExpressionScope<*>, itemList: Element.OutlineContent.ParagraphContent.ItemList<*>): ParagraphContent.ItemList =
        ParagraphContent.ItemList(itemList.stableHashCode(), buildList {
            render(scope, itemList.items) { inner, item ->
                add(ParagraphContent.ItemList.Item(item.stableHashCode(), renderText(inner, item.text)))
            }
        })

    private fun renderTextContent(scope: ExpressionScope<*>, element: Element.OutlineContent.ParagraphContent.Text<*>): List<Text> {
        val fontType = renderFontType(element.fontType)
        return when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> element.expr(scope.language).toContent(scope, fontType)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> element.expression.toContent(scope, fontType)
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(Literal(element.stableHashCode(), element.text(scope.language), fontType))
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> throw LetterMarkdownRendererException("Can't render unsupported element: NewLine")
        }
    }

    private fun renderText(scope: ExpressionScope<*>, elements: List<TextElement<*>>): List<Text> =
        buildList {
            render(scope, elements) { inner, text ->
                addAll(renderTextContent(inner, text))
            }
        }

    private fun renderFontType(fontType: Element.OutlineContent.ParagraphContent.Text.FontType): FontType =
        when (fontType) {
            Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN -> FontType.PLAIN
            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD -> FontType.BOLD
            Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC -> FontType.ITALIC
        }

    private fun StringExpression.toContent(scope: ExpressionScope<*>, fontType: FontType): List<Text> =
        if (this is Expression.Literal) {
            listOf(Literal(stableHashCode(), eval(scope), fontType))
        } else if (this is Expression.BinaryInvoke<*, *, *> && operation is BinaryOperation.Concat) {
            // Since we know that operation is Concat, we also know that `first` and `second` are StringExpression.
            @Suppress("UNCHECKED_CAST")
            (first as StringExpression).toContent(scope, fontType) + (second as StringExpression).toContent(scope, fontType)
        } else {
            listOf(Variable(stableHashCode(), eval(scope), fontType))
        }.mergeLiterals(fontType)

    private fun List<Text>.mergeLiterals(fontType: FontType): List<Text> =
        fold(emptyList()) { acc, current ->
            val previous = acc.lastOrNull()
            if (acc.isEmpty()) {
                listOf(current)
            } else if (previous is Literal && current is Literal) {
                acc.subList(0, acc.size - 1) + Literal(Objects.hash(previous.id, current.id), previous.text + current.text, fontType)
            } else {
                acc + current
            }
        }
}