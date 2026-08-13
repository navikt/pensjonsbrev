package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.Expr
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.Expr.*
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

/**
 * Oversettelse av [Expression] (predikater i showIf/ForEach.items, og tekstuttrykk skrevet ut i
 * brevet) til [Expr] — den delen av [TemplateDocumentationRendererV2] som gjør selve uttrykkene
 * menneskelig lesbare (flate assosiative kjeder, samlede feltkjeder, eksplisitte
 * komparasjon/negasjon/null-coalescing-noder, formatterings-eksempler osv). Se klassedokumentasjonen
 * på `TemplateDocumentationRendererV2` for helhetsbildet.
 *
 * `internal`-funksjonene her ([renderExpr], [flattenAssociative], [mergeLiterals]) er
 * implementasjonsdetaljer delt med innholds-traverseringen i `TemplateDocumentationRendererV2.kt`
 * (f.eks. `renderTextExpression`), men ikke ment som offentlig API utenfor modulen.
 */
private typealias AssignedReplacementsV2 = Map<Expression.FromScope.Assigned<*>, Expression<*>>

internal fun renderExpr(
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
        // rekursjon), er det den siste (ytterste) Select-en som overskriver leafType/
        // leafOwnerType sist, slik at de til slutt reflekterer typen og eierklassen til
        // det faktiske siste/leaf-segmentet. `className` er eierklassen feltet er
        // deklarert i, og brukes av frontend til å skille feltnavn som finnes i flere
        // datamodell-klasser fra hverandre ved DataClasses-lenking.
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
internal fun flattenAssociative(expr: Expression<*>, isSameOperation: (Operation) -> Boolean): List<Expression<*>> =
    if (expr is Expression.BinaryInvoke<*, *, *> && isSameOperation(expr.operation)) {
        flattenAssociative(expr.first, isSameOperation) + flattenAssociative(expr.second, isSameOperation)
    } else {
        listOf(expr)
    }

internal fun List<StringExpression>.mergeLiterals(): List<StringExpression> =
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
