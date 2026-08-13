package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

/**
 * Versjon 2 av dokumentasjonsrendereren. Målet er bedre menneskelig lesbarhet av predikater
 * (showIf/forEach) og tekstuttrykk enn TemplateDocumentationRenderer (v1) gir, ved å:
 *  - flate ut assosiative kjeder (AND/OR/CONCAT/PLUS) i stedet for nøstede binærinvokes,
 *  - samle feltkjeder (a.b.c) i én FieldPath-node i stedet for nøstede unære invokes,
 *  - gi komparasjon, negasjon, null-coalescing og if/else egne noder i stedet for
 *    pattern-matching-hacks i rendereren,
 *  - bevare formatteringsinformasjon (LocalizedFormatter) som i v1 kastes bort,
 *  - gi Skribenten-spesifikke operasjoner (Fritekst/RedigerbarData/BrevdataEllerFritekst) en
 *    egen, eksplisitt node fremfor å fremstå som anonyme funksjonskall.
 *
 * v1 (TemplateDocumentationRenderer/TemplateDocumentation) beholdes uendret ved siden av denne,
 * slik at eksisterende konsumenter ikke påvirkes før migrering.
 *
 * Denne filen dekker traverseringen av selve innholdsstrukturen (titler, avsnitt, tabeller,
 * lister, tekst) — oversettelsen av [Expression] (predikater og tekstuttrykk) til [Expr] ligger i
 * `TemplateDocumentationRendererV2Expr.kt` i samme pakke, og selve datamodellen ligger i
 * `TemplateDocumentationV2.kt`.
 */
object TemplateDocumentationRendererV2 {

    fun render(template: LetterTemplate<*, *>, lang: Language, modelSpecification: TemplateModelSpecification): TemplateDocumentationV2 =
        TemplateDocumentationV2(
            title = renderText(template.title, lang),
            outline = renderOutline(template.outline, lang),
            attachments = template.attachments.map { renderAttachment(it, lang) },
            templateModelSpecification = modelSpecification,
        )

    private fun renderAttachment(attachment: IncludeAttachment<*, *>, lang: Language): TemplateDocumentationV2.Attachment =
        TemplateDocumentationV2.Attachment(
            title = renderText(attachment.template.title, lang),
            outline = renderOutline(attachment.template.outline, lang),
            include = renderExpr(attachment.predicate, emptyMap(), forEachDepth = 0, lang = lang),
            attachmentData = renderExpr(attachment.data, emptyMap(), forEachDepth = 0, lang = lang),
        )

    private fun <T : Element<*>, R : TemplateDocumentationV2.Element> renderContentOrStructure(
        contentOrStructure: List<ContentOrControlStructure<*, T>>,
        forEachDepth: Int,
        lang: Language,
        mapper: (T) -> List<R>,
    ): List<TemplateDocumentationV2.ContentOrControlStructure<R>> =
        contentOrStructure.flatMap { el -> renderContentOrStructure(el, forEachDepth, lang) { mapper(it) } }

    private fun <T : Element<*>, R : TemplateDocumentationV2.Element> renderContentOrStructure(
        contentOrStructure: ContentOrControlStructure<*, T>,
        forEachDepth: Int,
        lang: Language,
        mapper: (T) -> List<R>,
    ): List<TemplateDocumentationV2.ContentOrControlStructure<R>> =
        when (contentOrStructure) {
            is ContentOrControlStructure.Content -> mapper(contentOrStructure.content).map { TemplateDocumentationV2.ContentOrControlStructure.Content(it) }

            is ContentOrControlStructure.Conditional -> {
                val elseIf = liftNestedIfElse(contentOrStructure.showElse, forEachDepth, lang, mapper)
                listOf(
                    TemplateDocumentationV2.ContentOrControlStructure.Conditional(
                        predicate = renderExpr(contentOrStructure.predicate, emptyMap(), forEachDepth, lang),
                        showIf = renderContentOrStructure(contentOrStructure.showIf, forEachDepth, lang, mapper),
                        elseIf = elseIf.first,
                        showElse = elseIf.second,
                    )
                )
            }

            is ContentOrControlStructure.ForEach<*, T, *> -> listOf(
                TemplateDocumentationV2.ContentOrControlStructure.ForEach(
                    items = renderExpr(contentOrStructure.items, emptyMap(), forEachDepth, lang),
                    body = renderContentOrStructure(contentOrStructure.body.toList(), forEachDepth + 1, lang, mapper),
                )
            )
        }

    private fun <T : Element<*>, R : TemplateDocumentationV2.Element> liftNestedIfElse(
        showElse: List<ContentOrControlStructure<*, T>>,
        forEachDepth: Int,
        lang: Language,
        mapper: (T) -> List<R>,
    ): Pair<List<TemplateDocumentationV2.ContentOrControlStructure.Conditional.ElseIf<R>>, List<TemplateDocumentationV2.ContentOrControlStructure<R>>> {
        val first = showElse.firstOrNull()
        return if (showElse.size == 1 && first is ContentOrControlStructure.Conditional) {
            liftNestedIfElse(first.showElse, forEachDepth, lang, mapper).let { (nestedIfElse, nestedElse) ->
                listOf(
                    TemplateDocumentationV2.ContentOrControlStructure.Conditional.ElseIf(
                        renderExpr(first.predicate, emptyMap(), forEachDepth, lang),
                        renderContentOrStructure(first.showIf, forEachDepth, lang, mapper)
                    )
                ).plus(nestedIfElse) to nestedElse
            }
        } else {
            emptyList<TemplateDocumentationV2.ContentOrControlStructure.Conditional.ElseIf<R>>() to renderContentOrStructure(showElse, forEachDepth, lang, mapper)
        }
    }

    private fun renderOutline(
        outline: List<OutlineElement<*>>,
        lang: Language,
    ): List<TemplateDocumentationV2.ContentOrControlStructure<TemplateDocumentationV2.Element.OutlineContent>> =
        renderContentOrStructure(outline, forEachDepth = 0, lang) { listOf(renderOutline(it, lang)) }

    private fun renderOutline(element: Element.OutlineContent<*>, lang: Language): TemplateDocumentationV2.Element.OutlineContent =
        when (element) {
            is Element.OutlineContent.Title1 -> TemplateDocumentationV2.Element.OutlineContent.Title1(renderText(element.text, lang))
            is Element.OutlineContent.Title2 -> TemplateDocumentationV2.Element.OutlineContent.Title2(renderText(element.text, lang))
            is Element.OutlineContent.Title3 -> TemplateDocumentationV2.Element.OutlineContent.Title3(renderText(element.text, lang))
            is Element.OutlineContent.Paragraph -> TemplateDocumentationV2.Element.OutlineContent.Paragraph(
                renderContentOrStructure(element.paragraph, forEachDepth = 0, lang) {
                    renderParagraphContent(it, lang)
                }
            )
        }

    private fun renderParagraphContent(
        element: Element.OutlineContent.ParagraphContent<*>,
        lang: Language,
    ): List<TemplateDocumentationV2.Element.ParagraphContent> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Form -> listOf(TemplateDocumentationV2.Element.ParagraphContent.Text.Literal("## missing documentation ##"))
            is Element.OutlineContent.ParagraphContent.ItemList -> listOf(
                TemplateDocumentationV2.Element.ParagraphContent.ItemList(
                    renderContentOrStructure(element.items, forEachDepth = 0, lang) { listOf(renderItem(it, lang)) }
                )
            )

            is Element.OutlineContent.ParagraphContent.Table -> listOf(renderTable(element, lang))
            is Element.OutlineContent.ParagraphContent.Text -> renderText(element, lang)
        }

    private fun renderTable(table: Element.OutlineContent.ParagraphContent.Table<*>, lang: Language): TemplateDocumentationV2.Element.ParagraphContent {
        return TemplateDocumentationV2.Element.ParagraphContent.Table(
            header = renderRow(table.header.colSpec.map { it.headerContent }, lang),
            rows = renderContentOrStructure(table.rows, forEachDepth = 0, lang) { listOf(renderRow(it.cells, lang)) }
        )
    }

    private fun renderRow(
        cells: List<Element.OutlineContent.ParagraphContent.Table.Cell<*>>,
        lang: Language,
    ): TemplateDocumentationV2.Element.ParagraphContent.Table.Row =
        TemplateDocumentationV2.Element.ParagraphContent.Table.Row(cells.map {
            TemplateDocumentationV2.Element.ParagraphContent.Table.Cell(
                renderText(
                    it.text,
                    lang
                )
            )
        })

    private fun renderText(
        text: List<TextElement<*>>,
        lang: Language,
    ): List<TemplateDocumentationV2.ContentOrControlStructure<TemplateDocumentationV2.Element.ParagraphContent.Text>> =
        renderContentOrStructure(text, forEachDepth = 0, lang) { renderText(it, lang) }

    private fun renderText(
        element: Element.OutlineContent.ParagraphContent.Text<*>,
        lang: Language,
    ): List<TemplateDocumentationV2.Element.ParagraphContent.Text> =
        when (element) {
            is Element.OutlineContent.ParagraphContent.Text.Literal -> listOf(TemplateDocumentationV2.Element.ParagraphContent.Text.Literal(element.text(lang)))
            is Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage -> renderTextExpression(element.expr(lang), lang)
            is Element.OutlineContent.ParagraphContent.Text.Expression -> renderTextExpression(element.expression, lang)
            is Element.OutlineContent.ParagraphContent.Text.NewLine -> emptyList()
        }

    private fun renderItem(
        item: Element.OutlineContent.ParagraphContent.ItemList.Item<*>,
        lang: Language,
    ): TemplateDocumentationV2.Element.ParagraphContent.ItemList.Item =
        TemplateDocumentationV2.Element.ParagraphContent.ItemList.Item(renderText(item.text, lang))

    private fun renderTextExpression(
        expr: Expression<String>,
        lang: Language,
    ): List<TemplateDocumentationV2.Element.ParagraphContent.Text> {
        @Suppress("UNCHECKED_CAST") // For Concat så vet vi at operandene er StringExpression
        val pieces = flattenAssociative(expr) { it is BinaryOperation.Concat }.map { it as StringExpression }.mergeLiterals()
        return pieces.map {
            if (it is Expression.Literal<String>) {
                TemplateDocumentationV2.Element.ParagraphContent.Text.Literal(it.value)
            } else {
                TemplateDocumentationV2.Element.ParagraphContent.Text.Expression(renderExpr(it, emptyMap(), forEachDepth = 0, lang))
            }
        }
    }
}
