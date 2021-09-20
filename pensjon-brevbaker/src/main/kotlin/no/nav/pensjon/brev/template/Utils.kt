package no.nav.pensjon.brev.template

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.io.OutputStream
import java.io.PrintWriter
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.reflect.KClass

fun jacksonObjectMapper() =
    com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

fun <T : Any> KClass<out T>.findSealedObjects(): Set<T> =
    if (isSealed) {
        sealedSubclasses.flatMap { it.findSealedObjects() }.toSet()
    } else {
        objectInstance?.let { setOf(it) } ?: emptySet()
    }

fun dateFormatter(language: Language): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(language.locale())

private val latexEscapePattern = Regex("([_^~$%#&{}])")
fun String.latexEscape(): String =
    // "_", "^", "~", "$", "%", "#", "&", "{", "}"
    latexEscapePattern.replace(this, """\\$1""")


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

        is Element.Paragraph -> paragraph.flatMap { it.findExpressions() }

        is Element.Form.Text -> prompt.findExpressions()

        is Element.Form.MultipleChoice -> prompt.findExpressions() + choices.flatMap { it.findExpressions() }

        is Element.NewLine -> emptyList()
    }
