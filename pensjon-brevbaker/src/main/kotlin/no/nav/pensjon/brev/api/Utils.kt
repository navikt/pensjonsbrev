package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.*

fun LetterTemplate<*, *>.description() =
    TemplateDescription(
        name = name,
        letterDataClass = letterDataType.java.name,
        languages = language.all().map { it.toCode() },
        metadata = letterMetadata,
    )

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