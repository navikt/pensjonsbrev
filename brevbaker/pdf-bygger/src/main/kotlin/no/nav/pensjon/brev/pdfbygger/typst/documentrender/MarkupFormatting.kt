package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.brevbaker.markup.Markup
import no.nav.pensjon.brevbaker.api.model.LanguageCode

internal fun Markup.Spraak.toLanguageCode(): LanguageCode =
    when (this) {
        Markup.Spraak.BOKMAL -> LanguageCode.BOKMAL
        Markup.Spraak.NYNORSK -> LanguageCode.NYNORSK
        Markup.Spraak.ENGLISH -> LanguageCode.ENGLISH
    }

private val personidentRegex = Regex("([0-9]{6})([0-9]{5})")

internal fun Markup.Personidentifikator.format(): String =
    personidentRegex.replace(value, $$"$1 $2")
