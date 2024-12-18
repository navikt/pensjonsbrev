package no.nav.pensjon.brev.template

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import no.nav.pensjon.brev.converters.BrevbakerBrevdataModule
import no.nav.pensjon.brev.converters.BrevkodeModule
import no.nav.pensjon.brev.converters.LetterMarkupModule
import no.nav.pensjon.brev.converters.TemplateModelSpecificationModule
import java.time.format.*
import kotlin.reflect.KClass

fun ObjectMapper.brevbakerConfig() {
    registerModule(JavaTimeModule())
    registerModule(BrevbakerBrevdataModule)
    registerModule(BrevkodeModule)
    registerModule(LetterMarkupModule)
    registerModule(TemplateModelSpecificationModule)
    enable(SerializationFeature.INDENT_OUTPUT)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
}

fun jacksonObjectMapper() = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().apply { brevbakerConfig() }

fun <T : Any> KClass<out T>.findSealedObjects(): Set<T> =
    if (isSealed) {
        sealedSubclasses.flatMap { it.findSealedObjects() }.toSet()
    } else {
        objectInstance?.let { setOf(it) } ?: emptySet()
    }

fun dateFormatter(language: Language, formatStyle: FormatStyle): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(language.locale())

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
