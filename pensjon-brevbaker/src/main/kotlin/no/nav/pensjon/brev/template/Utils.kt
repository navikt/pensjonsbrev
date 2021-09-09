package no.nav.pensjon.brev.template

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.io.OutputStream
import java.io.PrintWriter
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

fun jacksonObjectMapper() =
    com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

fun OutputStream.printWriter() =
    PrintWriter(this, true, Charsets.UTF_8)

fun <T : Any> KClass<out T>.findSealedObjects(): Set<T> =
    if (isSealed) {
        sealedSubclasses.flatMap { it.findSealedObjects() }.toSet()
    } else {
        objectInstance?.let { setOf(it) } ?: emptySet()
    }

private val latexEscapePattern = Regex("([_^~$%#&{}])")
fun String.latexEscape(): String =
    // "_", "^", "~", "$", "%", "#", "&", "{", "}"
    latexEscapePattern.replace(this, """\\$1""")

fun <Lang : LanguageCombination> LetterTemplate<Lang>.validateArgumentExpressions() {
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

internal fun <Lang : LanguageCombination> Element<Lang>.findExpressions(): List<Expression<*>> =
    when (this) {
        is Element.Conditional ->
            (showIf + showElse).flatMap { it.findExpressions() } + predicate

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
