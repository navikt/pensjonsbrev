package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.brev.brevbaker.markup.Brevtype as MarkupBrevtype
import no.nav.brev.brevbaker.markup.LanguageCode as MarkupLanguageCode

internal fun LanguageCode.toMarkup(): MarkupLanguageCode = when (this) {
    LanguageCode.BOKMAL -> MarkupLanguageCode.BOKMAL
    LanguageCode.NYNORSK -> MarkupLanguageCode.NYNORSK
    LanguageCode.ENGLISH -> MarkupLanguageCode.ENGLISH
}

internal fun LetterMetadata.Brevtype.toMarkup(): MarkupBrevtype = when (this) {
    LetterMetadata.Brevtype.VEDTAKSBREV -> MarkupBrevtype.VEDTAKSBREV
    LetterMetadata.Brevtype.INFORMASJONSBREV -> MarkupBrevtype.INFORMASJONSBREV
}
