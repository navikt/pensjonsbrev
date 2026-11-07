package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

/**
 * Dokumentasjonsrendereren. Den gir bedre menneskelig lesbarhet av predikater
 * (showIf/forEach) og tekstuttrykk ved å:
 *  - flate ut assosiative kjeder (AND/OR/CONCAT/PLUS) i stedet for nøstede binærinvokes,
 *  - samle feltkjeder (a.b.c) i én FieldPath-node i stedet for nøstede unære invokes,
 *  - gi komparasjon, negasjon, null-coalescing og if/else egne noder i stedet for
 *    pattern-matching-hacks i rendereren,
 *  - bevare formatteringsinformasjon (LocalizedFormatter),
 *  - gi Skribenten-spesifikke operasjoner (Fritekst/RedigerbarData/BrevdataEllerFritekst) en
 *    egen, eksplisitt node fremfor å fremstå som anonyme funksjonskall.
 *
 * Denne filen dekker traverseringen av selve innholdsstrukturen (titler, avsnitt, tabeller,
 * lister, tekst) — oversettelsen av [Expression] (predikater og tekstuttrykk) til [Expr] ligger i
 * `TemplateDocumentationRendererExpr.kt` i samme pakke, og selve datamodellen ligger i
 * `TemplateDocumentation.kt`.
 */
object TemplateDocumentationRenderer {

    private class RenderContext {
        private var nextIndex = 0
        fun nextIndex(): Int = nextIndex++
    }

    fun render(template: LetterTemplate<*, *>, lang: Language, modelSpecification: TemplateModelSpecification): TemplateDocumentation =
        RenderContext().let { context ->
            TemplateDocumentation(
                title = renderText(template.title, lang),
                outline = renderOutline(template.outline, lang, context),
                attachments = template.attachments.map { renderAttachment(it, lang, context) },
                templateModelSpecification = modelSpecification,
            )
        }

    private fun renderAttachment(attachment: IncludeAttachment<*, *>, lang: Language, context: RenderContext): TemplateDocumentation.Attachment =
        TemplateDocumentation.Attachment(
            title = renderText(attachment.template.title, lang),
            outline = renderOutline(attachment.template.outline, lang, context),
            include = renderExpr(attachment.predicate, emptyMap(), forEachDepth = 0, lang = lang),
            attachmentData = renderExpr(attachment.data, emptyMap(), forEachDepth = 0, lang = lang),
        )

    private fun <T : Element<*>, R : TemplateDocumentation.Element> renderContentOrStructure(
        contentOrStructure: List<ContentOrControlStructure<*, T>>,
        forEachDepth: Int,
        lang: Language,
        mapper: (T) -> List<R>,
    ): List<TemplateDocumentation.ContentOrControlStructure<R>> =
        contentOrStructure.flatMap { el -> renderContentOrStructure(el, forEachDepth, lang) { mapper(it) } }

    private fun <T : Element<*>, R : TemplateDocumentation.Element> renderContentOrStructure(
        contentOrStructure: ContentOrControlStructure<*, T>,
        forEachDepth: Int,
        lang: Language,
        mapper: (T) -> List<R>,
    ): List<TemplateDocumentation.ContentOrControlStructure<R>> =
        when (contentOrStructure) {
            is ContentOrControlStructure.Content -> mapper(contentOrStructure.content).map { TemplateDocumentation.ContentOrControlStructure.Content(it) }

            is ContentOrControlStructure.Conditional -> {
                val elseIf = liftNestedIfElse(contentOrStructure.showElse, forEachDepth, lang, mapper)
                listOf(
                    TemplateDocumentation.ContentOrControlStructure.Conditional(
                        predicate = renderExpr(contentOrStructure.predicate, emptyMap(), forEachDepth, lang),
                        showIf = renderContentOrStructure(contentOrStructure.showIf, forEachDepth, lang, mapper),
                        elseIf = elseIf.first,
                        showElse = elseIf.second,
                    )
                )
            }

            is ContentOrControlStructure.ForEach<*, T, *> -> listOf(
                TemplateDocumentation.ContentOrControlStructure.ForEach(
                    items = renderExpr(contentOrStructure.items, emptyMap(), forEachDepth, lang),
                    body = renderContentOrStructure(contentOrStructure.body.toList(), forEachDepth + 1, lang, mapper),
                )
            )
        }

    private fun <T : Element<*>, R : TemplateDocumentation.Element> liftNestedIfElse(
        showElse: List<ContentOrControlStructure<*, T>>,
        forEachDepth: Int,
        lang: Language,
        mapper: (T) -> List<R>,
    ): Pair<List<TemplateDocumentation.ContentOrControlStructure.Conditional.ElseIf<R>>, List<TemplateDocumentation.ContentOrControlStructure<R>>> {
        val first = showElse.firstOrNull()
        return if (showElse.size == 1 && first is ContentOrControlStructure.Conditional) {
            liftNestedIfElse(first.showElse, forEachDepth, lang, mapper).let { (nestedIfElse, nestedElse) ->
                listOf(
                    TemplateDocumentation.ContentOrControlStructure.Conditional.ElseIf(
                        renderExpr(first.predicate, emptyMap(), forEachDepth, lang),
                        renderContentOrStructure(first.showIf, forEachDepth, lang, mapper)
                    )
                ).plus(nestedIfElse) to nestedElse
            }
        } else {
            emptyList<TemplateDocumentation.ContentOrControlStructure.Conditional.ElseIf<R>>() to renderContentOrStructure(showElse, forEachDepth, lang, mapper)
        }
    }

    private fun renderOutline(
        outline: List<OutlineElement<*>>,
        lang: Language,
        context: RenderContext,
    ): List<TemplateDocumentation.ContentOrControlStructure<TemplateDocumentation.Element.OutlineContent>> =
        renderContentOrStructure(outline, forEachDepth = 0, lang) { listOf(renderOutline(it, lang, context)) }

    private fun renderOutline(
        element: Element.OutlineContent<*>,
        lang: Language,
        context: RenderContext,
    ): TemplateDocumentation.Element.OutlineContent =
        when (element) {
            is Element.OutlineContent.Title1 -> TemplateDocumentation.Element.OutlineContent.Title1(renderText(element.text, lang), context.nextIndex())
            is Element.OutlineContent.Title2 -> TemplateDocumentation.Element.OutlineContent.Title2(renderText(element.text, lang), context.nextIndex())
            is Element.OutlineContent.Title3 -> TemplateDocumentation.Element.OutlineContent.Title3(renderText(element.text, lang), context.nextIndex())
            is Element.OutlineContent.Paragraph -> TemplateDocumentation.Element.OutlineContent.Paragraph(
                renderContentOrStructure(element.paragraph, forEachDepth = 0, lang) {
                    renderParagraphContent(it, lang)
                },
                context.nextIndex(),
            )
        }

    private fun renderParagraphContent(
        element: Element.OutlineContent.ParagraphContent<*>,
        lang: Language,
    ): List<TemplateDocumentation.Element.ParagraphContent> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Form -> listOf(TemplateDocumentation.Element.ParagraphContent.Text.Literal("## missing documentation ##"))
            is Element.OutlineContent.ParagraphContent.ItemList -> listOf(
                TemplateDocumentation.Element.ParagraphContent.ItemList(
                    renderContentOrStructure(element.items, forEachDepth = 0, lang) { listOf(renderItem(it, lang)) },
                    element.type,
                )
            )

            is Element.OutlineContent.ParagraphContent.Table -> listOf(renderTable(element, lang))
            is Element.OutlineContent.ParagraphContent.Text -> renderText(element, lang)
        }

    private fun renderTable(table: Element.OutlineContent.ParagraphContent.Table<*>, lang: Language): TemplateDocumentation.Element.ParagraphContent {
        return TemplateDocumentation.Element.ParagraphContent.Table(
            header = renderRow(table.header.colSpec.map { it.headerContent }, lang),
            rows = renderContentOrStructure(table.rows, forEachDepth = 0, lang) { listOf(renderRow(it.cells, lang)) }
        )
    }

    private fun renderRow(
        cells: List<Element.OutlineContent.ParagraphContent.Table.Cell<*>>,
        lang: Language,
    ): TemplateDocumentation.Element.ParagraphContent.Table.Row =
        TemplateDocumentation.Element.ParagraphContent.Table.Row(cells.map {
            TemplateDocumentation.Element.ParagraphContent.Table.Cell(
                renderText(
                    it.text,
                    lang
                )
            )
        })

    private fun renderText(
        text: List<TextElement<*>>,
        lang: Language,
    ): List<TemplateDocumentation.ContentOrControlStructure<TemplateDocumentation.Element.ParagraphContent.Text>> =
        renderContentOrStructure(text, forEachDepth = 0, lang) { renderText(it, lang) }

    private fun renderText(
        element: Element.OutlineContent.ParagraphContent.Text<*>,
        lang: Language,
    ): List<TemplateDocumentation.Element.ParagraphContent.Text> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(TemplateDocumentation.Element.ParagraphContent.Text.Literal(element.text(lang)))
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> renderTextExpression(element.expr(lang), lang)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> renderTextExpression(element.expression, lang)
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> emptyList()
        }

    private fun renderItem(
        item: Element.OutlineContent.ParagraphContent.ItemList.Item<*>,
        lang: Language,
    ): TemplateDocumentation.Element.ParagraphContent.ItemList.Item =
        TemplateDocumentation.Element.ParagraphContent.ItemList.Item(renderText(item.text, lang))

    private fun renderTextExpression(
        expr: Expression<String>,
        lang: Language,
    ): List<TemplateDocumentation.Element.ParagraphContent.Text> {
        @Suppress("UNCHECKED_CAST") // For Concat så vet vi at operandene er StringExpression
        val pieces = flattenAssociative(expr) { it is BinaryOperation.Concat }.map { it as StringExpression }.mergeLiterals()
        return pieces.map {
            if (it is Expression.Literal<String>) {
                TemplateDocumentation.Element.ParagraphContent.Text.Literal(it.value)
            } else {
                TemplateDocumentation.Element.ParagraphContent.Text.Expression(renderExpr(it, emptyMap(), forEachDepth = 0, lang))
            }
        }
    }
}
