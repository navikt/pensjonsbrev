package no.nav.brev.brevbaker

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.brev.brevbaker.markup.Markup

internal fun LanguageCode.toMarkup(): Markup.Spraak = when (this) {
    LanguageCode.BOKMAL -> Markup.Spraak.BOKMAL
    LanguageCode.NYNORSK -> Markup.Spraak.NYNORSK
    LanguageCode.ENGLISH -> Markup.Spraak.ENGLISH
}

internal fun LetterMetadata.Brevtype.toMarkup(): Markup.Brevtype = when (this) {
    LetterMetadata.Brevtype.VEDTAKSBREV -> Markup.Brevtype.VEDTAKSBREV
    LetterMetadata.Brevtype.INFORMASJONSBREV -> Markup.Brevtype.INFORMASJONSBREV
}
