package no.nav.pensjon.brev.template

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun dateFormatter(language: Language, formatStyle: FormatStyle): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(language.locale())
