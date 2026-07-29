package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun pdfDateFormatter(language: LanguageCode, formatStyle: FormatStyle): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(language.locale())

private val foedselsnummerRegex = Regex("([0-9]{6})([0-9]{5})")

fun Foedselsnummer.formatPdf(): String = foedselsnummerRegex.replace(value, "$1 $2")
