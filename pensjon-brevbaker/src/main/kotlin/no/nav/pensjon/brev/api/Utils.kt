package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.LanguageCode


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