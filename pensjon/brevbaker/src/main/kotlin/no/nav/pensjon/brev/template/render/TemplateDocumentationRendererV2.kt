package no.nav.pensjon.brev.template.render

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.Expr
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.Expr.*
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

private typealias AssignedReplacementsV2 = Map<Expression.FromScope.Assigned<*>, Expression<*>>

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

    // --- Expr-rendering ---------------------------------------------------

    private fun renderExpr(
        expr: Expression<*>,
        assignments: AssignedReplacementsV2,
        forEachDepth: Int,
        lang: Language,
    ): Expr =
        when (expr) {
            is Expression.Literal -> Literal(expr.value.toString(), inferScalarKind(expr.value))

            is Expression.FromScope.Language -> FieldPath(TemplateDocumentationV2.DataSource.Scope("language"), emptyList(), leafType = null)
            is Expression.FromScope.Felles -> FieldPath(TemplateDocumentationV2.DataSource.Scope("felles"), emptyList(), leafType = null)
            is Expression.FromScope.Argument -> FieldPath(TemplateDocumentationV2.DataSource.Scope("argument"), emptyList(), leafType = null)
            is Expression.FromScope.Assigned ->
                assignments[expr]
                    ?.let { renderExpr(it, assignments, forEachDepth, lang) }
                    // Løkkevariabelen i den nærmeste omsluttende forEach. Uten reflection over
                    // Item-typen kan vi ikke gi et semantisk navn, men depth gjør at nøstede
                    // løkker i det minste kan skilles fra hverandre (til forskjell fra v1 der
                    // alle heter "X").
                    ?: FieldPath(TemplateDocumentationV2.DataSource.ForEachVar("item", forEachDepth), emptyList(), leafType = null)

            is Expression.UnaryInvoke<*, *> -> renderUnaryInvoke(expr.value, expr.operation, assignments, forEachDepth, lang)
            is Expression.BinaryInvoke<*, *, *> -> renderBinaryInvoke(expr.first, expr.second, expr.operation, assignments, forEachDepth, lang)
            is Expression.NullSafeApplication<*, *> -> renderExpr(
                expr = expr.application,
                assignments = assignments + (expr.assigned to expr.input),
                forEachDepth = forEachDepth,
                lang = lang,
            )
        }

    private fun inferScalarKind(value: Any?): TemplateModelSpecification.FieldType.Scalar.Kind? =
        when (value) {
            is Boolean -> TemplateModelSpecification.FieldType.Scalar.Kind.BOOLEAN
            is Double -> TemplateModelSpecification.FieldType.Scalar.Kind.DOUBLE
            is Int, is Long -> TemplateModelSpecification.FieldType.Scalar.Kind.NUMBER
            is java.time.LocalDate -> TemplateModelSpecification.FieldType.Scalar.Kind.DATE
            is String -> TemplateModelSpecification.FieldType.Scalar.Kind.STRING
            else -> null
        }

    private val valueClassUnwrapCache = java.util.concurrent.ConcurrentHashMap<String, Boolean>()

    /**
     * Avgjør om et `Select` er en utpakking av en semantisk énverdi-wrapper (f.eks.
     * `BrevbakerType.Kroner`, `Year`, `Percent`, `Postnummer`) — ikke et "ekte" domenefelt.
     * To tilfeller dekkes:
     *  - `selector.className` er faktisk et Kotlin `value class` (avgjøres via refleksjon;
     *    `TemplateModelSelector.className` lagrer den punktum-separerte Kotlin-navnet, mens
     *    JVM-ets faktiske (binære) klassenavn bruker `$` for nøstede klasser, så vi prøver oss
     *    fra høyre mot venstre til klassen faktisk lar seg laste).
     *  - `selector.propertyName == "value"` — dekker i tillegg det delte `IntValue`-grensesnittet
     *    (`Kroner`/`Year`/`Percent`/`Months`/`Days` sin felles `.value`-utpakking brukt internt
     *    av bl.a. `equalTo`/`greaterThan`/`plus`), som er et grensesnitt og ikke en value class,
     *    så refleksjonssjekket alene ikke fanger opp dette tilfellet. I hele kodebasen er
     *    property-navnet "value" utelukkende brukt til nettopp denne typen wrapper-utpakking
     *    (aldri et reelt domenefelt), så heuristikken er trygg.
     */
    private fun isValueClassUnwrap(selector: TemplateModelSelector<*, *>): Boolean =
        selector.propertyName == "value" ||
            valueClassUnwrapCache.getOrPut(selector.className) { loadKotlinClass(selector.className)?.isValue == true }

    private fun loadKotlinClass(qualifiedName: String): kotlin.reflect.KClass<*>? {
        var candidate = qualifiedName
        while (true) {
            try {
                return Class.forName(candidate).kotlin
            } catch (_: ClassNotFoundException) {
                val lastDot = candidate.lastIndexOf('.')
                if (lastDot < 0) return null
                candidate = candidate.substring(0, lastDot) + "$" + candidate.substring(lastDot + 1)
            }
        }
    }

    @OptIn(BrevbakerDSLInternal::class)
    private fun renderUnaryInvoke(
        value: Expression<*>,
        operation: UnaryOperation<*, *>,
        assignments: AssignedReplacementsV2,
        forEachDepth: Int,
        lang: Language,
    ): Expr {
        // `?` i safeCall bæres ikke videre til dokumentasjonen (samme forenkling som v1).
        if (operation is UnaryOperation.SafeCall<*, *>) {
            return renderUnaryInvoke(value, operation.operation, assignments, forEachDepth, lang)
        }

        // Representerer `!(a == b)` som en egen NOT_EQUAL-comparison i stedet for Not(Equal(..)).
        if (operation is UnaryOperation.Not && value is Expression.BinaryInvoke<*, *, *> && value.operation is BinaryOperation.Equal<*>) {
            return Comparison(
                left = renderExpr(value.first, assignments, forEachDepth, lang),
                op = TemplateDocumentationV2.CompareOp.NOT_EQUAL,
                right = renderExpr(value.second, assignments, forEachDepth, lang),
            )
        }

        if (operation is UnaryOperation.Select<*, *>) {
            val selector = operation.selector
            val base = renderExpr(value, assignments, forEachDepth, lang)
            // Value classes (f.eks. Kroner, Year, Percent, Postnummer) er for oss bare en
            // semantisk innpakning rundt én verdi — å vise utpakkingen som et eget FieldPath-
            // segment (".value") gir ingen ekstra menneskelig informasjon, kun støy. Behold
            // derfor `base` uendret når selectoren er en value-class-utpakking.
            if (isValueClassUnwrap(selector)) {
                return base
            }
            val segment = selector.propertyName
            // `propertyType` er en fullt kvalifisert Kotlin-type-streng (f.eks.
            // "no.nav.pensjon.brevbaker.api.model.SomeDto?" eller "kotlin.String"), satt av
            // hver Select i kjeden. Siden feltstien bygges innenfra og ut (se renderExpr sin
            // rekursjon), er det den siste (ytterste) Select-en som overskriver leafType sist,
            // slik at den til slutt reflekterer typen til det faktiske siste/leaf-segmentet.
            return when (base) {
                is FieldPath -> base.copy(
                    segments = base.segments + segment,
                    leafType = selector.propertyType,
                    leafOwnerType = selector.className,
                )
                // Feltaksess på et beregnet uttrykk (f.eks. `getOrNull(...).felt`,
                // `(a ?: b).felt`) — DataSource.Computed lar oss fortsatt bygge en lesbar
                // FieldPath-kjede i stedet for en bakvendt FunctionCall(".felt", [base]).
                else -> FieldPath(
                    TemplateDocumentationV2.DataSource.Computed(base),
                    listOf(segment),
                    leafType = selector.propertyType,
                    leafOwnerType = selector.className,
                )
            }
        }

        val rendered = renderExpr(value, assignments, forEachDepth, lang)
        return when (operation) {
            is UnaryOperation.Not -> Not(rendered)
            is UnaryOperation.AbsoluteValue -> FunctionCall("abs", listOf(rendered))
            is UnaryOperation.AbsoluteValueKroner -> FunctionCall("abs", listOf(rendered))
            is UnaryOperation.LocalDateNow -> FunctionCall("today", emptyList())
            is UnaryOperation.SizeOf -> FunctionCall("size", listOf(rendered))
            is UnaryOperation.ToString -> FunctionCall("str", listOf(rendered))
            is UnaryOperation.IsEmpty -> FunctionCall("isEmpty", listOf(rendered))
            is UnaryOperation.FunksjonsbryterEnabled -> FunctionCall("enabled", listOf(rendered))
            is UnaryOperation.BrukerFulltNavn -> FunctionCall("fulltNavn", listOf(rendered))
            is UnaryOperation.MapValue<*, *> -> FunctionCall(operation.mapper.name, listOf(rendered))
            is UnaryOperation.MapCollection<*, *> -> FunctionCall("map", listOf(rendered))
            is UnaryOperation.RedigerbarData -> EditableField(TemplateDocumentationV2.EditableKind.REDIGERBAR_DATA, value = rendered, fallback = null)
            is UnaryOperation.Fritekst -> EditableField(TemplateDocumentationV2.EditableKind.FRITEKST, value = rendered, fallback = null)
            is UnaryOperation.Select -> error("handled above")
            is UnaryOperation.SafeCall -> error("handled above")
        }
    }

    @OptIn(BrevbakerDSLInternal::class)
    private fun renderBinaryInvoke(
        first: Expression<*>,
        second: Expression<*>,
        operation: BinaryOperation<*, *, *>,
        assignments: AssignedReplacementsV2,
        forEachDepth: Int,
        lang: Language,
    ): Expr {
        // `?` i safeCall bæres ikke videre til dokumentasjonen (samme forenkling som v1).
        if (operation is BinaryOperation.SafeCall<*, *, *>) {
            return renderBinaryInvoke(first, second, operation.operation, assignments, forEachDepth, lang)
        }
        if (operation is BinaryOperation.Flip<*, *, *>) {
            return renderBinaryInvoke(second, first, operation.operation, assignments, forEachDepth, lang)
        }

        if (operation is LocalizedFormatter<*>) {
            return Format(
                value = renderExpr(first, assignments, forEachDepth, lang),
                formatterName = operation::class.simpleName ?: "format",
                exampleText = formatterExampleText(operation, lang),
            )
        }

        if (operation is BinaryOperation.And) {
            return AssociativeOp(TemplateDocumentationV2.AssocOp.AND, (flattenAssociative(first) { it is BinaryOperation.And } + flattenAssociative(second) { it is BinaryOperation.And })
                .map { renderExpr(it, assignments, forEachDepth, lang) })
        }
        if (operation is BinaryOperation.Or) {
            return AssociativeOp(TemplateDocumentationV2.AssocOp.OR, (flattenAssociative(first) { it is BinaryOperation.Or } + flattenAssociative(second) { it is BinaryOperation.Or })
                .map { renderExpr(it, assignments, forEachDepth, lang) })
        }
        if (operation is BinaryOperation.IntPlus) {
            return AssociativeOp(TemplateDocumentationV2.AssocOp.PLUS, (flattenAssociative(first) { it is BinaryOperation.IntPlus } + flattenAssociative(second) { it is BinaryOperation.IntPlus })
                .map { renderExpr(it, assignments, forEachDepth, lang) })
        }
        if (operation is BinaryOperation.Concat) {
            @Suppress("UNCHECKED_CAST")
            val pieces = (flattenAssociative(first) { it is BinaryOperation.Concat } + flattenAssociative(second) { it is BinaryOperation.Concat })
                .map { it as StringExpression }
                .mergeLiterals()
            return if (pieces.size == 1) renderExpr(pieces.first(), assignments, forEachDepth, lang)
            else AssociativeOp(TemplateDocumentationV2.AssocOp.CONCAT, pieces.map { renderExpr(it, assignments, forEachDepth, lang) })
        }

        if (operation is BinaryOperation.Equal<*>) return Comparison(renderExpr(first, assignments, forEachDepth, lang), TemplateDocumentationV2.CompareOp.EQUAL, renderExpr(second, assignments, forEachDepth, lang))
        if (operation is BinaryOperation.GreaterThan<*>) return Comparison(renderExpr(first, assignments, forEachDepth, lang), TemplateDocumentationV2.CompareOp.GREATER_THAN, renderExpr(second, assignments, forEachDepth, lang))
        if (operation is BinaryOperation.GreaterThanOrEqual<*>) return Comparison(renderExpr(first, assignments, forEachDepth, lang), TemplateDocumentationV2.CompareOp.GREATER_THAN_OR_EQUAL, renderExpr(second, assignments, forEachDepth, lang))
        if (operation is BinaryOperation.LessThan<*>) return Comparison(renderExpr(first, assignments, forEachDepth, lang), TemplateDocumentationV2.CompareOp.LESS_THAN, renderExpr(second, assignments, forEachDepth, lang))
        if (operation is BinaryOperation.LessThanOrEqual<*>) return Comparison(renderExpr(first, assignments, forEachDepth, lang), TemplateDocumentationV2.CompareOp.LESS_THAN_OR_EQUAL, renderExpr(second, assignments, forEachDepth, lang))

        if (operation is BinaryOperation.IfNull<*>) {
            return NullCoalesce(renderExpr(first, assignments, forEachDepth, lang), renderExpr(second, assignments, forEachDepth, lang))
        }

        if (operation is BinaryOperation.BrevdataEllerFritekst) {
            return EditableField(
                kind = TemplateDocumentationV2.EditableKind.BREVDATA_ELLER_FRITEKST,
                value = renderExpr(first, assignments, forEachDepth, lang),
                fallback = renderExpr(second, assignments, forEachDepth, lang),
            )
        }

        // `IfElse(pred, Tuple(ifTrue, ifElse))`: løft ut de to grenene som egen Conditional-node.
        if (operation is BinaryOperation.IfElse<*> && second is Expression.BinaryInvoke<*, *, *> && second.operation is BinaryOperation.Tuple<*, *>) {
            return Conditional(
                predicate = renderExpr(first, assignments, forEachDepth, lang),
                ifTrue = renderExpr(second.first, assignments, forEachDepth, lang),
                ifElse = renderExpr(second.second, assignments, forEachDepth, lang),
            )
        }

        if (operation is BinaryOperation.EnumInList<*> || operation is BinaryOperation.IsOneOf<*>) {
            return FunctionCall("isOneOf", listOf(renderExpr(first, assignments, forEachDepth, lang), renderExpr(second, assignments, forEachDepth, lang)))
        }
        if (operation is BinaryOperation.GetElementOrNull<*>) {
            return FunctionCall("getOrNull", listOf(renderExpr(first, assignments, forEachDepth, lang), renderExpr(second, assignments, forEachDepth, lang)))
        }
        if (operation is BinaryOperation.MapCollection<*, *, *>) {
            return FunctionCall("map", listOf(renderExpr(first, assignments, forEachDepth, lang), renderExpr(second, assignments, forEachDepth, lang)))
        }

        return FunctionCall(
            name = operation.doc?.name ?: operation::class.simpleName ?: "undocumentedOperation",
            args = listOf(renderExpr(first, assignments, forEachDepth, lang), renderExpr(second, assignments, forEachDepth, lang)),
        )
    }

    /**
     * Genererer et 100% korrekt eksempel på hvordan en [LocalizedFormatter] faktisk ville
     * formattert en verdi i et ferdig brev, ved å kalle den ekte `apply`-metoden direkte med en
     * syntetisk eksempelverdi og riktig språk — se drøfting i template-documentation-v2-design.md
     * seksjon om Format-visning. Vi reimplementerer bevisst IKKE formatteringslogikk her (ville
     * garantert drifte fra sannheten i brevrendering over tid); i stedet velger vi kun en
     * plausibel input-verdi av riktig type per kjent formatter og lar formatteren gjøre jobben.
     * Returnerer `null` for ukjente/fremtidige formatterer (frontend faller da tilbake til
     * fraseviisning "formatert med <formatterName>"), og ved uventede feil under kallet.
     */
    @Suppress("UNCHECKED_CAST")
    private fun formatterExampleText(operation: LocalizedFormatter<*>, lang: Language): String? =
        try {
            when (operation) {
                is LocalizedFormatter.ShortDateFormat -> operation.apply(EXAMPLE_DATE, lang)
                is LocalizedFormatter.DateFormat -> operation.apply(EXAMPLE_DATE, lang)
                is LocalizedFormatter.MonthYearFormatter -> operation.apply(EXAMPLE_DATE, lang)
                is LocalizedFormatter.MonthFormatter -> operation.apply(EXAMPLE_MONTH, lang)
                is LocalizedFormatter.MonthFormatterShort -> operation.apply(EXAMPLE_MONTH, lang)
                is LocalizedFormatter.DoubleFormat -> operation.apply(EXAMPLE_DOUBLE, lang)
                is LocalizedFormatter.IntFormat -> operation.apply(EXAMPLE_INT, lang)
                is LocalizedFormatter.CurrencyFormat -> operation.apply(EXAMPLE_INT, lang)
                is LocalizedFormatter.CurrencyFormatKroner -> operation.apply(no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner(EXAMPLE_INT), lang)
                is LocalizedFormatter.TelefonnummerFormat -> operation.apply(no.nav.pensjon.brevbaker.api.model.BrevbakerType.Telefonnummer(EXAMPLE_TELEFONNUMMER), lang)
                is LocalizedFormatter.FoedselsnummerFormat -> operation.apply(no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer(EXAMPLE_FOEDSELSNUMMER), lang)
                is LocalizedFormatter.CollectionFormat -> operation.apply(EXAMPLE_COLLECTION, lang)
                is LocalizedFormatter.LandnavnFormat -> operation.apply(EXAMPLE_LANDKODE, lang)
                // Ukjent/fremtidig formatter-type: vi kjenner ikke en trygg syntetisk verdi å
                // kalle `apply` med, så vi lar frontend falle tilbake til fraseviisning i stedet
                // for å risikere en kastet exception eller en misvisende "tilfeldig" verdi.
                else -> null
            }
        } catch (_: Exception) {
            null
        }

    private val EXAMPLE_DATE: java.time.LocalDate = java.time.LocalDate.of(2024, 3, 17)
    private val EXAMPLE_MONTH: java.time.Month = java.time.Month.MARCH
    private const val EXAMPLE_DOUBLE: Double = 1234.5
    private const val EXAMPLE_INT: Int = 12345
    private const val EXAMPLE_TELEFONNUMMER: String = "12345678"
    private const val EXAMPLE_FOEDSELSNUMMER: String = "12345678901"
    private val EXAMPLE_COLLECTION: List<String> = listOf("Ola", "Kari", "Per")
    private val EXAMPLE_LANDKODE: no.nav.brev.BrevLandmodell.Landkode = no.nav.brev.BrevLandmodell.Landkode("NO")


    /**
     * Flater ut en venstre-assosiativ kjede av [expr] slik at f.eks. `a and b and c`, som i
     * DSL-en bygges som `BinaryInvoke(BinaryInvoke(a, b, And), c, And)`, blir `[a, b, c]` i
     * stedet for et nøstet tre. Generaliserer v1s `flattenLiteralConcat` til å gjelde alle
     * assosiative operasjoner (AND, OR, CONCAT, PLUS).
     */
    private fun flattenAssociative(expr: Expression<*>, isSameOperation: (Operation) -> Boolean): List<Expression<*>> =
        if (expr is Expression.BinaryInvoke<*, *, *> && isSameOperation(expr.operation)) {
            flattenAssociative(expr.first, isSameOperation) + flattenAssociative(expr.second, isSameOperation)
        } else {
            listOf(expr)
        }

    fun List<StringExpression>.mergeLiterals(): List<StringExpression> =
        fold(emptyList()) { acc, current ->
            val previous = acc.lastOrNull()
            if (acc.isEmpty()) {
                listOf(current)
            } else if (previous is Expression.Literal<String> && current is Expression.Literal<String>) {
                acc.subList(0, acc.size - 1) + (previous.value + current.value).expr()
            } else {
                acc + current
            }
        }
}

data class TemplateDocumentationV2(
    val title: List<ContentOrControlStructure<Element.ParagraphContent.Text>>,
    val outline: List<ContentOrControlStructure<Element.OutlineContent>>,
    val attachments: List<Attachment>,
    val templateModelSpecification: TemplateModelSpecification,
) {
    data class Attachment(
        val title: List<ContentOrControlStructure<Element.ParagraphContent.Text>>,
        val outline: List<ContentOrControlStructure<Element.OutlineContent>>,
        val include: Expr,
        val attachmentData: Expr,
    )

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "controlStructureType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(ContentOrControlStructure.Conditional::class, name = "CONDITIONAL"),
        JsonSubTypes.Type(ContentOrControlStructure.Content::class, name = "CONTENT"),
        JsonSubTypes.Type(ContentOrControlStructure.ForEach::class, name = "FOR_EACH"),
    )
    @JsonPropertyOrder("controlStructureType")
    sealed class ContentOrControlStructure<E : Element> {
        data class Content<E : Element>(val content: E) : ContentOrControlStructure<E>()
        data class Conditional<E : Element>(
            val predicate: Expr,
            val showIf: List<ContentOrControlStructure<E>>,
            val elseIf: List<ElseIf<E>>,
            val showElse: List<ContentOrControlStructure<E>>,
        ) : ContentOrControlStructure<E>() {
            data class ElseIf<E : Element>(val predicate: Expr, val showIf: List<ContentOrControlStructure<E>>)
        }

        data class ForEach<E : Element>(
            val items: Expr,
            val body: List<ContentOrControlStructure<E>>,
        ) : ContentOrControlStructure<E>()
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "elementType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(Element.OutlineContent.Title1::class, name = "TITLE1"),
        JsonSubTypes.Type(Element.OutlineContent.Title2::class, name = "TITLE2"),
        JsonSubTypes.Type(Element.OutlineContent.Title3::class, name = "TITLE3"),
        JsonSubTypes.Type(Element.OutlineContent.Paragraph::class, name = "PARAGRAPH"),
        JsonSubTypes.Type(Element.ParagraphContent.Text.Literal::class, name = "PARAGRAPH_TEXT_LITERAL"),
        JsonSubTypes.Type(Element.ParagraphContent.Text.Expression::class, name = "PARAGRAPH_TEXT_EXPRESSION"),
        JsonSubTypes.Type(Element.ParagraphContent.ItemList::class, name = "PARAGRAPH_ITEMLIST"),
        JsonSubTypes.Type(Element.ParagraphContent.ItemList.Item::class, name = "PARAGRAPH_ITEMLIST_ITEM"),
        JsonSubTypes.Type(Element.ParagraphContent.Table::class, name = "PARAGRAPH_TABLE"),
        JsonSubTypes.Type(Element.ParagraphContent.Table.Row::class, name = "PARAGRAPH_TABLE_ROW"),
    )
    @JsonPropertyOrder("elementType")
    sealed class Element {
        sealed class OutlineContent : Element() {
            data class Title1(val text: List<ContentOrControlStructure<ParagraphContent.Text>>) : OutlineContent()
            data class Title2(val text: List<ContentOrControlStructure<ParagraphContent.Text>>) : OutlineContent()
            data class Title3(val text: List<ContentOrControlStructure<ParagraphContent.Text>>) : OutlineContent()
            data class Paragraph(val paragraph: List<ContentOrControlStructure<ParagraphContent>>) : OutlineContent()
        }

        sealed class ParagraphContent : Element() {
            sealed class Text : ParagraphContent() {
                data class Literal(val text: String) : Text()
                data class Expression(val expression: Expr) : Text()
            }

            data class ItemList(val items: List<ContentOrControlStructure<Item>>) : ParagraphContent() {
                data class Item(val text: List<ContentOrControlStructure<Text>>) : Element()
            }

            data class Table(val header: Row, val rows: List<ContentOrControlStructure<Row>>) : ParagraphContent() {
                data class Row(val cells: List<Cell>) : Element()
                data class Cell(val text: List<ContentOrControlStructure<Text>>)
            }
        }
    }

    /**
     * Generalisert uttrykksmodell — dekker både predikater (showIf/ForEach.items) og
     * tekstuttrykk skrevet ut i brevet, siden begge er strukturelt samme problem
     * (se designnotat "template-documentation-v2-design.md"). Et "predikat" er ganske enkelt
     * en [Expr] man vet evaluerer til Boolean (typisk [Comparison], [AssociativeOp] med
     * AND/OR, [Not] eller enkelte [FunctionCall]).
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "exprType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(Literal::class, name = "LITERAL"),
        JsonSubTypes.Type(FieldPath::class, name = "FIELD_PATH"),
        JsonSubTypes.Type(AssociativeOp::class, name = "ASSOCIATIVE_OP"),
        JsonSubTypes.Type(Comparison::class, name = "COMPARISON"),
        JsonSubTypes.Type(Not::class, name = "NOT"),
        JsonSubTypes.Type(FunctionCall::class, name = "FUNCTION_CALL"),
        JsonSubTypes.Type(Format::class, name = "FORMAT"),
        JsonSubTypes.Type(Conditional::class, name = "CONDITIONAL_EXPR"),
        JsonSubTypes.Type(NullCoalesce::class, name = "NULL_COALESCE"),
        JsonSubTypes.Type(EditableField::class, name = "EDITABLE_FIELD"),
    )
    @JsonPropertyOrder("exprType")
    sealed class Expr {
        data class Literal(val value: String, val kind: TemplateModelSpecification.FieldType.Scalar.Kind?) : Expr()

        data class FieldPath(val source: DataSource, val segments: List<String>, val leafType: String?) : Expr()

        data class AssociativeOp(val op: AssocOp, val operands: List<Expr>) : Expr()

        data class Comparison(val left: Expr, val op: CompareOp, val right: Expr) : Expr()

        data class Not(val term: Expr) : Expr()

        data class FunctionCall(val name: String, val args: List<Expr>) : Expr()

        /**
         * [exampleText] er et syntetisk, men 100% korrekt formattert eksempel (produsert ved å
         * kalle den faktiske [no.nav.pensjon.brev.template.LocalizedFormatter.apply] med en
         * fast eksempelverdi og riktig språk) — f.eks. "17. mars 2024" for et datofelt formattert
         * med `DateFormat`. Frontend bør vise dette som primær, kompakt tekst, og legge selve
         * uttrykksstrukturen (value + formatterName) bak en detaljvisning (popover). `null` når
         * formatteren er ukjent for oss eller eksempel-genereringen feiler — frontend faller da
         * tilbake til dagens fraseviisning ("formatert med <formatterName>").
         */
        data class Format(val value: Expr, val formatterName: String, val exampleText: String?) : Expr()

        /** Erstatter v1s `Invoke(IfElse, first, Tuple(ifTrue, ifElse))`-innpakking. */
        data class Conditional(val predicate: Expr, val ifTrue: Expr, val ifElse: Expr) : Expr()

        /** `?:` / `IfNull` — vist eksplisitt fremfor skjult slik v1 gjorde for `?: false`. */
        data class NullCoalesce(val value: Expr, val fallback: Expr) : Expr()

        @JsonInclude(JsonInclude.Include.NON_NULL)
        data class EditableField(val kind: EditableKind, val value: Expr?, val fallback: Expr?) : Expr()
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "dataSourceType", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(DataSource.Scope::class, name = "SCOPE"),
        JsonSubTypes.Type(DataSource.ForEachVar::class, name = "FOR_EACH_VAR"),
        JsonSubTypes.Type(DataSource.Computed::class, name = "COMPUTED"),
    )
    @JsonPropertyOrder("dataSourceType")
    sealed class DataSource {
        data class Scope(val name: String) : DataSource()
        data class ForEachVar(val label: String, val depth: Int) : DataSource()

        /**
         * Feltaksess på et vilkårlig beregnet uttrykk, f.eks. `getOrNull(liste, 0).felt` eller
         * `(a ?: b).felt`. Uten denne varianten havnet slike kjeder i en bakvendt
         * `FunctionCall(".felt", [base])`-representasjon (se `renderUnaryInvoke`s
         * `UnaryOperation.Select`-håndtering) — [Computed] gjør at [FieldPath.segments] kan
         * bygges videre på toppen av hvilken som helst [Expr], ikke bare `Scope`/`ForEachVar`.
         */
        data class Computed(val expr: Expr) : DataSource()
    }

    enum class AssocOp { AND, OR, CONCAT, PLUS }
    enum class CompareOp { EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL }
    enum class EditableKind { FRITEKST, REDIGERBAR_DATA, BREVDATA_ELLER_FRITEKST }
}
