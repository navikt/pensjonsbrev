package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.intValueSelector
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Telefonnummer

fun Telefonnummer.format() =
    "([0-9][0-9])".toRegex().replace(value, "$1 ").trim()

fun Foedselsnummer.format() =
    "([0-9]{6})([0-9]{5})".toRegex().replace(value, "$1 $2")

fun Expression<Telefonnummer>.format() = format(formatter = LocalizedFormatter.TelefonnummerFormat)

@JvmName("formatKroner")
fun Expression<Kroner>.format() = select(intValueSelector).format(formatter = LocalizedFormatter.CurrencyFormat)

@JvmName("formatIntValue")
fun Expression<IntValue>.format() = select(intValueSelector).format()