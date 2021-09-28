package no.nav.pensjon.brev.api.dto

import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator
import no.nav.pensjon.brev.api.model.LanguageCode
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageCombination
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper

private val schemaGenerator = JsonSchemaGenerator(jacksonObjectMapper())

fun LetterTemplate<*, *>.description() =
    TemplateDescription(
        name = name,
        base = base::class.qualifiedName!!,
        letterDataSchema = schemaGenerator.generateSchema(letterDataType.java).asObjectSchema(),
        languages = language.all().map { it.toCode() },
    )

private fun LanguageCombination.all(): List<Language> =
    when (this) {
        is LanguageCombination.Single<*> -> listOf(first)
        is LanguageCombination.Double<*, *> -> listOf(first, second)
        is LanguageCombination.Triple<*, *, *> -> listOf(first, second, third)
    }


fun LanguageCode.toLanguage(): Language =
    when (this) {
        LanguageCode.BOKMAL -> Language.Bokmal
        LanguageCode.NYNORSK -> Language.Nynorsk
        LanguageCode.ENGLISH -> Language.English
    }


fun Language.toCode(): LanguageCode =
    when (this) {
        Language.Bokmal -> LanguageCode.BOKMAL
        Language.Nynorsk -> LanguageCode.NYNORSK
        Language.English -> LanguageCode.ENGLISH
    }