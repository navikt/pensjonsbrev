package no.nav.pensjon.brev.template

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.reflect.KClass

fun ObjectMapper.brevbakerConfig() {
    registerModule(JavaTimeModule())
    enable(SerializationFeature.INDENT_OUTPUT)
}

fun jacksonObjectMapper() = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply { brevbakerConfig() }

fun <T : Any> KClass<out T>.findSealedObjects(): Set<T> =
    if (isSealed) {
        sealedSubclasses.flatMap { it.findSealedObjects() }.toSet()
    } else {
        objectInstance?.let { setOf(it) } ?: emptySet()
    }

fun dateFormatter(language: Language): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(language.locale())

val CHARACTER_BLACKLIST =
    hashSetOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 127)

fun String.latexEscape(): String =
    this.map {
        if (CHARACTER_BLACKLIST.contains(it.code)) {
            ""
        } else {
            when (it) {
                '#' -> """\#"""
                '$' -> """\$"""
                '%' -> """\%"""
                '&' -> """\&"""
                '\\' -> """\textbackslash{}"""
                '^' -> """\textasciicircum{}"""
                '_' -> """\_"""
                '{' -> """\{"""
                '}' -> """\}"""
                '~' -> """\textasciitilde{}"""
                else -> it.toString()
            }
        }
    }.joinToString(separator = "")


internal fun <Lang : LanguageCombination> Element<Lang>.findExpressions(): List<Expression<*>> =
    when (this) {
        is Element.Conditional ->
            (showIf + showElse).flatMap { it.findExpressions() } + predicate

        is Element.Text.Expression ->
            listOf(expression)

        is Element.Text.Expression.ByLanguage ->
            expression.values.toList()

        is Element.Text.Literal ->
            emptyList()

        is Element.Title1 ->
            title1.flatMap { it.findExpressions() }

        // TODO will return expressions which operates in another scope.
        is Element.IncludePhrase<*, *> ->
            phrase.elements.flatMap { it.findExpressions() }

        is Element.Paragraph -> paragraph.flatMap { it.findExpressions() }

        is Element.Form.Text -> prompt.findExpressions()

        is Element.Form.MultipleChoice -> prompt.findExpressions() + choices.flatMap { it.findExpressions() }

        is Element.NewLine -> emptyList()
    }
