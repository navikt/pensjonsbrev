@file:Suppress("unused")

package no.nav.pensjon.brevbaker.api.model

import java.util.Locale

enum class LanguageCode {
    BOKMAL, NYNORSK, ENGLISH;

    fun locale(): Locale = when (this) {
        BOKMAL -> Locale.forLanguageTag("no")
        NYNORSK -> Locale.forLanguageTag("no")
        LanguageCode.ENGLISH -> Locale.UK
    }
}