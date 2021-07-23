package no.nav.pensjon.brev.template

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

fun <T : Any> KClass<out T>.findSealedObjects(): Set<T> =
    if (isSealed) {
        sealedSubclasses.flatMap { it.findSealedObjects() }.toSet()
    } else {
        objectInstance?.let { setOf(it) } ?: emptySet()
    }

fun LetterTemplate.validateArgumentExpressions() {
    val requiredParameters = parameters.filterIsInstance<RequiredParameter>().map { it.parameter }

    val nonRequiredUsedAsRequired = outline.flatMap { it.findExpressions() }
        .flatMap { it.requiredArguments() }
        .map { it.parameter }
        .toSet()
        .filterNot { requiredParameters.contains(it) }

    if (nonRequiredUsedAsRequired.isNotEmpty()) {
        val names = nonRequiredUsedAsRequired.joinToString(", ") { it.name }
        throw IllegalArgumentException("Template outline uses optional argument(s) as if it/they were required: $names")
    }
}

internal fun Expression<*>.requiredArguments(): List<Expression.Argument<*, *>> =
    when (this) {
        is Expression.Argument<*, *> ->
            listOf(this)

        is Expression.BinaryInvoke<*, *, *> ->
            first.requiredArguments() + second.requiredArguments()

        is Expression.Literal ->
            emptyList()

        is Expression.OptionalArgument<*, *> ->
            emptyList()

        is Expression.Select<*, *> ->
            value.requiredArguments()

        is Expression.UnaryInvoke<*, *> ->
            value.requiredArguments()
    }

internal fun Element.findExpressions(): List<Expression<*>> =
    when (this) {
        is Element.Conditional ->
            (showIf + showElse).flatMap { it.findExpressions() } + predicate

        is Element.Section ->
            section.flatMap { it.findExpressions() }

        is Element.Text.Expression ->
            listOf(expression)

        is Element.Text.Literal ->
            emptyList()

        is Element.Text.Phrase ->
            emptyList()

        is Element.Title1 ->
            title1.flatMap { it.findExpressions() }
        is Element.Paragraph -> emptyList()
    }
