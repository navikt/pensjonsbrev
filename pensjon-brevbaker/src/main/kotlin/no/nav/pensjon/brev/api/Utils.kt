package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.LanguageCode
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageCombination
import no.nav.pensjon.brev.template.LetterTemplate

fun LetterTemplate<*, *>.description() =
    TemplateDescription(
        name = name,
        base = base::class.qualifiedName!!,
        letterDataClass = letterDataType.java.name,
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